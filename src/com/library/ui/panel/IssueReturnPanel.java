
package com.library.ui.panel;

import com.library.dao.BookDAO;
import com.library.dao.IssueDAO;
import com.library.dao.StudentDAO;
import com.library.model.Book;
import com.library.model.IssueRecord;
import com.library.model.Student;
import com.library.util.DateUtil;
import com.library.util.FineCalculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class IssueReturnPanel extends JPanel {
    private IssueDAO issueDAO = new IssueDAO();
    private BookDAO bookDAO = new BookDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private DefaultTableModel model;
    private JTable table;

    public IssueReturnPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        model = new DefaultTableModel(new Object[]{"IssueID","BookID","StudentID","IssueDate","DueDate","ReturnDate","Status"},0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model);
        table.setRowHeight(28);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Issue / Return");
        title.setFont(title.getFont().deriveFont(Font.BOLD,16f));
        header.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton btnIssue = new JButton("Issue");
        JButton btnReturn = new JButton("Return");
        actions.add(btnIssue); actions.add(btnReturn);
        header.add(actions, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        btnIssue.addActionListener(e -> issueBook());
        btnReturn.addActionListener(e -> returnBook());

        refreshTable();
    }

    private void refreshTable() {
        try {
            model.setRowCount(0);
            List<IssueRecord> list = issueDAO.getAllIssues();
            for (IssueRecord ir: list) {
                model.addRow(new Object[] {ir.getId(), ir.getBookId(), ir.getStudentId(), ir.getIssueDate(), ir.getDueDate(), ir.getReturnDate(), ir.getStatus()});
            }
        } catch (SQLException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void issueBook() {
        String sBookId = JOptionPane.showInputDialog(this, "Enter Book ID to issue:");
        String sStudentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (sBookId == null || sStudentId == null) return;
        try {
            int bookId = Integer.parseInt(sBookId.trim());
            int studentId = Integer.parseInt(sStudentId.trim());
            Book b = bookDAO.getBookById(bookId);
            Student s = studentDAO.getStudentById(studentId);
            if (b==null) { JOptionPane.showMessageDialog(this, "Book not found"); return; }
            if (s==null) { JOptionPane.showMessageDialog(this, "Student not found"); return; }
            if (b.getAvailableQty() <= 0) { JOptionPane.showMessageDialog(this, "No copies available"); return; }

            IssueRecord ir = new IssueRecord();
            LocalDate issue = LocalDate.now();
            LocalDate due = issue.plusDays(14); // 2 weeks
            ir.setBookId(bookId);
            ir.setStudentId(studentId);
            ir.setIssueDate(issue);
            ir.setDueDate(due);
            issueDAO.issueBook(ir);
            JOptionPane.showMessageDialog(this, "Book issued. Due date: " + due);
            refreshTable();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void returnBook() {
        int r = table.getSelectedRow();
        if (r==-1) { JOptionPane.showMessageDialog(this, "Select an issued record to return"); return; }
        int issueId = (int) model.getValueAt(r,0);
        try {
            LocalDate returnDate = LocalDate.now();
            String dueStr = String.valueOf(model.getValueAt(r,4));
            LocalDate due = DateUtil.parse(dueStr);
            double fine = FineCalculator.calculate(due, returnDate);
            int confirm = JOptionPane.showConfirmDialog(this, "Return book? Fine: "+fine, "Confirm Return", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                issueDAO.returnBook(issueId, returnDate, fine);
                JOptionPane.showMessageDialog(this, "Book returned. Fine: "+fine);
                refreshTable();
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
