package com.example.io_aede.ui;

import javax.swing.*;
import java.awt.*;

public class EvolutionVisualizer extends JPanel {
    private double[][] points;
    private String title;
    private final Color pointColor;

    public EvolutionVisualizer(String title, Color pointColor) {
        this.title = title;
        this.pointColor = pointColor;
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    public void updateData(double[][] p, String newTitle) {
        this.points = p;
        this.title = newTitle;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (points == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nagłówek
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString(title, 10, 20);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Rysowanie osobników
        g2.setColor(pointColor);
        for (double[] p : points) {
            // Rzutowanie izometryczne dla 3 wymiarów (p[0], p[1], p[2])
            // Skalowanie dopasowane do dziedziny funkcji (np. -5 do 5)
            int x = centerX + (int)((p[0] - p[1]) * 15);
            int y = centerY + (int)((p[0] + p[1]) * 7 - p[2] * 2);

            g2.fillOval(x, y, 6, 6);
        }
    }
}
