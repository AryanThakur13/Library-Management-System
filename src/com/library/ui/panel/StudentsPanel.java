
package com.library.ui.panel;

import com.library.dao.StudentDAO;
import com.library.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StudentsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private StudentDAO dao = new StudentDAO();

    public StudentsPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        model = new DefaultTableModel(new Object[]{"ID","Name","Roll No","Contact"},0) { public boolean isCellEditable(int r,int c){return false;} };
        table = new JTable(model);
        table.setRowHeight(28);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Students");
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

        btnAdd.addActionListener(e -> addStudent());
        btnEdit.addActionListener(e -> editStudent());
        btnDelete.addActionListener(e -> deleteStudent());

        refreshTable();
    }

    private void refreshTable() {
        try {
            model.setRowCount(0);
            List<Student> list = dao.getAllStudents();
            for (Student s: list) {
                model.addRow(new Object[]{s.getId(), s.getName(), s.getRollNo(), s.getContact()});
            }
        } catch (SQLException ex) { ex.printStackTrace(); JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void addStudent() {
        JTextField tName = new JTextField();
        JTextField tRoll = new JTextField();
        JTextField tContact = new JTextField();
        Object[] fields = {"Name:", tName, "Roll No:", tRoll, "Contact:", tContact};
        int ok = JOptionPane.showConfirmDialog(this, fields, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (ok==JOptionPane.OK_OPTION) {
            try {
                Student s = new Student();
                s.setName(tName.getText().trim());
                s.setRollNo(tRoll.getText().trim());
                s.setContact(tContact.getText().trim());
                dao.addStudent(s);
                refreshTable();
            } catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        }
    }

    private void editStudent() {
        int r = table.getSelectedRow(); if (r==-1) { JOptionPane.showMessageDialog(this, "Select student"); return; }
        int id = (int) model.getValueAt(r,0);
        try {
            Student s = dao.getStudentById(id);
            JTextField tName = new JTextField(s.getName());
            JTextField tRoll = new JTextField(s.getRollNo());
            JTextField tContact = new JTextField(s.getContact());
            Object[] fields = {"Name:", tName, "Roll No:", tRoll, "Contact:", tContact};
            int ok = JOptionPane.showConfirmDialog(this, fields, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
            if (ok==JOptionPane.OK_OPTION) {
                s.setName(tName.getText().trim());
                s.setRollNo(tRoll.getText().trim());
                s.setContact(tContact.getText().trim());
                dao.updateStudent(s);
                refreshTable();
            }
        } catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
    }

    private void deleteStudent() {
        int r = table.getSelectedRow(); if (r==-1) { JOptionPane.showMessageDialog(this, "Select student"); return; }
        int id = (int) model.getValueAt(r,0);
        int c = JOptionPane.showConfirmDialog(this, "Delete selected student?","Confirm",JOptionPane.YES_NO_OPTION);
        if (c==JOptionPane.YES_OPTION) {
            try { dao.deleteStudent(id); refreshTable(); } catch (SQLException ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        }
    }
}
