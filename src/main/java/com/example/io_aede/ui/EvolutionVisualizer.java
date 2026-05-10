package com.example.io_aede.ui;

import com.example.io_aede.core.BenchmarkFunction;

import javax.swing.*;
import java.awt.*;

public class EvolutionVisualizer extends JPanel {
    private double[][] points;
    private final BenchmarkFunction function;
    private String title;
    private final Color color;
    private final int gridRes = 20;

    public EvolutionVisualizer(String title, Color color, BenchmarkFunction f) {
        this.title = title;
        this.color = color;
        this.function = f;
        this.setPreferredSize(new Dimension(500, 500));
        this.setBackground(Color.WHITE);
    }

    public void updateData(double[][] p, String t) {
        this.points = p;
        this.title = t;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2 + 50; // Przesunięcie w dół, by widzieć "głębię"

        // 1. RYSOWANIE SIATKI (TERENU)
        g2.setColor(new Color(220, 220, 220));
        double step = (function.max - function.min) / gridRes;

        for (int i = 0; i <= gridRes; i++) {
            for (int j = 0; j <= gridRes; j++) {
                double x1 = function.min + i * step;
                double y1 = function.min + j * step;

                // Rysujemy linie łączące sąsiednie punkty siatki
                if (i < gridRes) drawLine3D(g2, x1, y1, x1 + step, y1, cx, cy);
                if (j < gridRes) drawLine3D(g2, x1, y1, x1, y1 + step, cx, cy);
            }
        }

        // 2. RYSOWANIE OSOBNIKÓW
        g2.setColor(color);
        if (points != null) {
            for (double[] p : points) {
                Point pt = project(p[0], p[1], p[2], cx, cy);
                g2.fillOval(pt.x - 3, pt.y - 3, 6, 6);
            }
        }

        g2.setColor(Color.BLACK);
        g2.drawString(title, 10, 20);
    }

    // Pomocnicza metoda rzutowania 3D -> 2D
    private Point project(double x, double y, double z, int cx, int cy) {
        // Parametry rzutu izometrycznego
        double angle = Math.toRadians(30);
        int px = cx + (int) ((x - y) * Math.cos(angle) * 20);
        int py = cy + (int) ((x + y) * Math.sin(angle) * 10 - z * 2);
        return new Point(px, py);
    }

    private void drawLine3D(Graphics2D g2, double x1, double y1, double x2, double y2, int cx, int cy) {
        // Obliczamy Z dla siatki (uproszczenie: n=2 dla tła)
        double z1 = function.func.applyAsDouble(new double[]{x1, y1, 0});
        double z2 = function.func.applyAsDouble(new double[]{x2, y2, 0});

        Point p1 = project(x1, y1, z1, cx, cy);
        Point p2 = project(x2, y2, z2, cx, cy);
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
}