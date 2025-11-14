
package com.library.ui.glass;

import javax.swing.*;
import java.awt.*;

/**
 * Simple translucent rounded panel (glassmorphism card)
 */
public class GlassPanel extends JPanel {
    private int arc = 20;
    private Color color = new Color(255,255,255,180); // translucent white

    public GlassPanel() {
        setOpaque(false);
        setBorder(null);
    }

    public GlassPanel(Color c, int arc) {
        this();
        this.color = c;
        this.arc = arc;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        // background blur-like gradient
        GradientPaint gp = new GradientPaint(0, 0, new Color(255,255,255,160), w, h, new Color(255,255,255,120));
        g2.setPaint(gp);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        g2.setColor(color);
        g2.fillRoundRect(0, 0, w-1, h-1, arc, arc);
        // subtle border
        g2.setColor(new Color(255,255,255,200));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, w-1, h-1, arc, arc);
        g2.dispose();
        super.paintComponent(g);
    }
}
