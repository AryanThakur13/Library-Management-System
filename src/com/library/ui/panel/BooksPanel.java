
package com.library.ui.panel;

import com.library.dao.BookDAO;
import com.library.model.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BooksPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private BookDAO dao = new BookDAO();

    public BooksPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        model = new DefaultTableModel(new Object[]{"ID","Title","Author","Available"},0) {
            public boolean isCellEditable(int row,int col){return false;}
        };
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(center);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Books");
        title.setFont(title.getFont().deriveFont(Font.BOLD,16f));
        header.add(title, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setOpaque(false);
        JButton btnAdd = new JButton("Add");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        actions.add(btnAdd); actions.add(btnEdit); actions.add(btnDelete);
        header.add(actions, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> showAddDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnEdit.addActionListener(e -> editSelected());

        refreshTable();
    }

    private void refreshTable() {
        try {
            model.setRowCount(0);
            List<Book> list = dao.getAllBooks();
            for (Book b: list) {
                model.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getAvailableQty()});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading books: "+ex.getMessage());
        }
    }

    private void showAddDialog() {
        JTextField tTitle = new JTextField();
        JTextField tAuthor = new JTextField();
        JTextField tQty = new JTextField("1");
        Object[] fields = {"Title:", tTitle, "Author:", tAuthor, "Total Qty:", tQty};
        int ok = JOptionPane.showConfirmDialog(this, fields, "Add Book", JOptionPane.OK_CANCEL_OPTION);
        if (ok == JOptionPane.OK_OPTION) {
            try {
                Book b = new Book();
                b.setTitle(tTitle.getText().trim());
                b.setAuthor(tAuthor.getText().trim());
                int qty = Integer.parseInt(tQty.getText().trim());
                b.setTotalQty(qty); b.setAvailableQty(qty);
                dao.addBook(b);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: "+ex.getMessage());
            }
        }
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r==-1) { JOptionPane.showMessageDialog(this, "Select a book"); return; }
        int id = (int) model.getValueAt(r,0);
        int c = JOptionPane.showConfirmDialog(this, "Delete selected book?","Confirm",JOptionPane.YES_NO_OPTION);
        if (c==JOptionPane.YES_OPTION) {
            try { dao.deleteBook(id); refreshTable(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        }
    }

    private void editSelected() {
        int r = table.getSelectedRow();
        if (r==-1) { JOptionPane.showMessageDialog(this, "Select a book"); return; }
        int id = (int) model.getValueAt(r,0);
        try {
            Book b = dao.getBookById(id);
            JTextField tTitle = new JTextField(b.getTitle());
            JTextField tAuthor = new JTextField(b.getAuthor());
            JTextField tQty = new JTextField(String.valueOf(b.getTotalQty()));
            Object[] fields = {"Title:", tTitle, "Author:", tAuthor, "Total Qty:", tQty};
            int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);
            if (ok==JOptionPane.OK_OPTION) {
                b.setTitle(tTitle.getText().trim());
                b.setAuthor(tAuthor.getText().trim());
                int qty = Integer.parseInt(tQty.getText().trim());
                int diff = qty - b.getTotalQty();
                b.setTotalQty(qty);
                b.setAvailableQty(b.getAvailableQty() + diff);
                dao.updateBook(b);
                refreshTable();
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }
}
