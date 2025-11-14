
package com.library.ui;

import javax.swing.*;
import java.awt.*;

public class TopBar extends JPanel {
    public TopBar() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0,60));
        setBackground(new Color(30,30,30));
        init();
    }

    private void init() {
        JLabel title = new JLabel("Library Management System");
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setBorder(BorderFactory.createEmptyBorder(8,16,8,8));
        add(title, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        right.setOpaque(false);
        JLabel user = new JLabel("Admin");
        user.setForeground(Color.WHITE);
        right.add(user);
        add(right, BorderLayout.EAST);
    }
}
