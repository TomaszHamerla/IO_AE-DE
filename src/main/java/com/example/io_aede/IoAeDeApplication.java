package com.example.io_aede;

import com.example.io_aede.core.BenchmarkFunction;
import com.example.io_aede.core.EvolutionaryEngine;
import com.example.io_aede.service.ReportGenerator;
import com.example.io_aede.ui.EvolutionVisualizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication
public class IoAeDeApplication {

    public static void main(String[] args) throws InterruptedException {
        new SpringApplicationBuilder(IoAeDeApplication.class).headless(false).run(args);

        EvolutionaryEngine engine = new EvolutionaryEngine();
        ReportGenerator report = new ReportGenerator();
        BenchmarkFunction function = BenchmarkFunction.SPHERE;
        int dimensions = 3;
        int popSize = 200; // agenci

        double[][] popAE = init(popSize, dimensions, function);
        double[][] popDE = init(popSize, dimensions, function);

        JFrame frame = new JFrame("Porównanie Algorytmów: AE vs DE");
        frame.setLayout(new GridLayout(1, 2));

        EvolutionVisualizer vizAE = new EvolutionVisualizer(
                "Evolutionary Algorithm", new Color(0, 102, 204), function
        );
        EvolutionVisualizer vizDE = new EvolutionVisualizer(
                "Differential Evolution", new Color(204, 0, 0), function
        );

        frame.add(vizAE);
        frame.add(vizDE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        for (int i = 0; i < 201; i++) {
            var resAE = engine.nextAE(popAE, function);
            var resDE = engine.nextDE(popDE, function, 0.8, 0.9);

            popAE = resAE.population();
            popDE = resDE.population();

            vizAE.updateData(popAE, String.format("AE Gen: %d | Best: %.4f", i, resAE.best()));
            vizDE.updateData(popDE, String.format("DE Gen: %d | Best: %.4f", i, resDE.best()));

            report.addEntry(i, resAE, resDE);

            Thread.sleep(50); // Prędkość symulacji
        }

        report.printConsoleReport();
    }

    private static double[][] init(int size, int dim, BenchmarkFunction f) {
        double[][] p = new double[size][dim];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < dim; j++)
                p[i][j] = f.min + Math.random() * (f.max - f.min);
        return p;
    }
}
