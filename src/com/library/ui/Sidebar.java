
package com.library.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Simple vertical sidebar with buttons
 */
public class Sidebar extends JPanel {
    public JButton btnBooks = new JButton("Books");
    public JButton btnStudents = new JButton("Students");
    public JButton btnIssue = new JButton("Issue/Return");

    public Sidebar() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(180,0));
        setBackground(new Color(20,20,30));
        init();
    }

    private void init() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1; gc.insets = new Insets(10,10,10,10);
        JLabel logo = new JLabel("<html><div style='color:white;font-weight:bold;font-size:14px;'>📚 My Library</div></html>");
        add(logo, gc);

        gc.gridy = 1;
        styleButton(btnBooks); add(btnBooks, gc);
        gc.gridy = 2;
        styleButton(btnStudents); add(btnStudents, gc);
        gc.gridy = 3;
        styleButton(btnIssue); add(btnIssue, gc);

        gc.gridy = 4; gc.weighty = 1; add(Box.createVerticalGlue(), gc);
    }

    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(255,255,255,20));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void addButtonListener(ActionListener l) {
        btnBooks.addActionListener(l);
        btnStudents.addActionListener(l);
        btnIssue.addActionListener(l);
    }
}
