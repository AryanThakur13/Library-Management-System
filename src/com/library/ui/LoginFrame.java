
package com.library.ui;

import com.library.dao.UserDAO;
import com.library.ui.glass.GlassPanel;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginFrame() {
        setTitle("Library - Admin Login");
        setSize(520,360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(false);
        initUI();
    }

    private void initUI() {
        // background gradient panel
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHints(rh);
                int w = getWidth(), h = getHeight();
                Color c1 = new Color(58,123,213);
                Color c2 = new Color(58,213,195);
                GradientPaint gp = new GradientPaint(0,0,c1,w,h,c2);
                g2.setPaint(gp);
                g2.fillRect(0,0,w,h);
            }
        };
        bg.setLayout(new GridBagLayout());

        GlassPanel card = new GlassPanel(new Color(255,255,255,200), 22);
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(380,260));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(10,10,10,10);
        gc.gridx = 0; gc.gridy = 0;
        JLabel lbl = new JLabel("Admin Login");
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD,20f));
        card.add(lbl, gc);

        gc.gridy = 1;
        card.add(new JLabel("Username:"), gc);
        gc.gridy = 2; txtUser = new JTextField(20); card.add(txtUser, gc);

        gc.gridy = 3;
        card.add(new JLabel("Password:"), gc);
        gc.gridy = 4; txtPass = new JPasswordField(20); card.add(txtPass, gc);

        gc.gridy = 5;
        JButton btn = new JButton("Login");
        btn.setPreferredSize(new Dimension(120,34));
        card.add(btn, gc);

        btn.addActionListener(e -> onLogin());

        bg.add(card);
        add(bg);
    }

    private void onLogin() {
        String u = txtUser.getText().trim();
        String p = new String(txtPass.getPassword());
        try {
            UserDAO dao = new UserDAO();
            if (dao.validate(u,p)) {
                dispose();
                DashboardFrame dash = new DashboardFrame();
                dash.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Error: "+ex.getMessage());
        }
    }
}
