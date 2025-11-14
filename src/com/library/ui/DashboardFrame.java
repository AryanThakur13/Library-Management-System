package com.library.ui;

import com.library.ui.glass.GlassPanel;
import com.library.ui.panel.BooksPanel;
import com.library.ui.panel.IssueReturnPanel;
import com.library.ui.panel.StudentsPanel;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel contentArea = new JPanel(cardLayout);

    public DashboardFrame() {
        setTitle("Library Admin Dashboard");
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        Sidebar sidebar = new Sidebar();
        TopBar top = new TopBar();

        // content area with glass card padding
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(new Color(40, 40, 50));

        GlassPanel inner = new GlassPanel(new Color(255, 255, 255, 160), 24);
        inner.setLayout(new BorderLayout());
        inner.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        // cards / panels
        contentArea.add(new BooksPanel(), "books");
        contentArea.add(new StudentsPanel(), "students");
        contentArea.add(new IssueReturnPanel(), "issue");

        inner.add(contentArea, BorderLayout.CENTER);
        center.add(inner, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        // set default colors for sidebar buttons
        resetSidebarColors(sidebar);
        highlightButton(sidebar.btnBooks);

        // sidebar button actions with dynamic highlight
        sidebar.btnBooks.addActionListener(e -> {
            cardLayout.show(contentArea, "books");
            resetSidebarColors(sidebar);
            highlightButton(sidebar.btnBooks);
        });

        sidebar.btnStudents.addActionListener(e -> {
            cardLayout.show(contentArea, "students");
            resetSidebarColors(sidebar);
            highlightButton(sidebar.btnStudents);
        });

        sidebar.btnIssue.addActionListener(e -> {
            cardLayout.show(contentArea, "issue");
            resetSidebarColors(sidebar);
            highlightButton(sidebar.btnIssue);
        });
    }

    // helper method to reset all sidebar buttons
    private void resetSidebarColors(Sidebar sidebar) {
        Color defaultBg = new Color(30, 30, 40);  // sidebar background
        Color defaultFg = Color.GRAY;           // text color
        sidebar.btnBooks.setBackground(defaultBg);
        sidebar.btnStudents.setBackground(defaultBg);
        sidebar.btnIssue.setBackground(defaultBg);

        sidebar.btnBooks.setForeground(defaultFg);
        sidebar.btnStudents.setForeground(defaultFg);
        sidebar.btnIssue.setForeground(defaultFg);
    }

    // helper method to highlight a button
    private void highlightButton(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // highlight background (steel blue)
        button.setForeground(Color.CYAN);              // highlight text
    }
}
