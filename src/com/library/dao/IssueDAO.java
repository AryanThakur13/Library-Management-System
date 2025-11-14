
package com.library.dao;

import com.library.model.IssueRecord;
import com.library.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class IssueDAO {
    public void issueBook(IssueRecord ir) throws SQLException {
        String insert = "INSERT INTO issues (book_id, student_id, issue_date, due_date, status) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, ir.getBookId());
                ps.setInt(2, ir.getStudentId());
                ps.setDate(3, Date.valueOf(ir.getIssueDate()));
                ps.setDate(4, Date.valueOf(ir.getDueDate()));
                ps.setString(5, "ISSUED");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        ir.setId(keys.getInt(1));
                    }
                }
            }
            String upd = "UPDATE books SET available_qty = available_qty - 1 WHERE id=? AND available_qty>0";
            try (PreparedStatement ps2 = c.prepareStatement(upd)) {
                ps2.setInt(1, ir.getBookId());
                int affected = ps2.executeUpdate();
                if (affected == 0) {
                    c.rollback();
                    throw new SQLException("Book not available to issue");
                }
            }
            c.commit();
        }
    }

    public void returnBook(int issueId, LocalDate returnDate, double fine) throws SQLException {
        String bookIdSql = "SELECT book_id FROM issues WHERE id=?";
        Integer bookId = null;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(bookIdSql)) {
            ps.setInt(1, issueId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) bookId = rs.getInt("book_id");
            }
        }
        String upd = "UPDATE issues SET return_date=?, fine=?, status='RETURNED' WHERE id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(upd)) {
            ps.setDate(1, Date.valueOf(returnDate));
            ps.setDouble(2, fine);
            ps.setInt(3, issueId);
            ps.executeUpdate();
        }
        if (bookId != null) {
            String inc = "UPDATE books SET available_qty = available_qty + 1 WHERE id=?";
            try (Connection c = DBConnection.getConnection(); PreparedStatement ps2 = c.prepareStatement(inc)) {
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
            }
        }
    }

    public List<IssueRecord> getAllIssues() throws SQLException {
        List<IssueRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM issues";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                IssueRecord ir = new IssueRecord();
                ir.setId(rs.getInt("id"));
                ir.setBookId(rs.getInt("book_id"));
                ir.setStudentId(rs.getInt("student_id"));
                ir.setIssueDate(rs.getDate("issue_date").toLocalDate());
                ir.setDueDate(rs.getDate("due_date").toLocalDate());
                Date r = rs.getDate("return_date");
                if (r != null) ir.setReturnDate(r.toLocalDate());
                ir.setFine(rs.getDouble("fine"));
                ir.setStatus(rs.getString("status"));
                list.add(ir);
            }
        }
        return list;
    }
}
