package com.example.io_aede.core;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.Random;

public class EvolutionaryEngine {

    private final Random rnd = new Random();

    // --- ALGORYTM EWOLUCYJNY (AE) ---
    public StepResult nextAE(double[][] pop, BenchmarkFunction f) {
        int n = pop[0].length;
        double[][] nextPop = new double[pop.length][n];
        double[] fitness = Arrays.stream(pop).mapToDouble(f.func).toArray();

        for (int i = 0; i < pop.length; i++) {
            // Seleksja turniejowa
            double[] p1 = pop[rnd.nextInt(pop.length)];
            double[] p2 = pop[rnd.nextInt(pop.length)];
            double[] parent = fitness[Arrays.asList(pop).indexOf(p1)] < fitness[Arrays.asList(pop).indexOf(p2)] ? p1 : p2;

            // Mutacja Gaussowska
            for (int j = 0; j < n; j++) {
                nextPop[i][j] = Math.clamp(parent[j] + rnd.nextGaussian() * 0.1, f.min, f.max);
            }
        }
        return getStats(nextPop, f);
    }

    // --- EWOLUCJA RÓŻNICOWA (DE) ---
    public StepResult nextDE(double[][] pop, BenchmarkFunction f, double F, double CR) {
        int n = pop[0].length;
        for (int i = 0; i < pop.length; i++) {
            int r1 = rnd.nextInt(pop.length), r2 = rnd.nextInt(pop.length), r3 = rnd.nextInt(pop.length);
            double[] trial = new double[n];
            int jRand = rnd.nextInt(n);

            for (int j = 0; j < n; j++) {
                if (rnd.nextDouble() < CR || j == jRand) {
                    trial[j] = Math.clamp(pop[r1][j] + F * (pop[r2][j] - pop[r3][j]), f.min, f.max);
                } else trial[j] = pop[i][j];
            }
            if (f.func.applyAsDouble(trial) < f.func.applyAsDouble(pop[i])) pop[i] = trial;
        }
        return getStats(pop, f);
    }

    private StepResult getStats(double[][] pop, BenchmarkFunction f) {
        double[] fit = Arrays.stream(pop).mapToDouble(f.func).toArray();
        DoubleSummaryStatistics s = Arrays.stream(fit).summaryStatistics();
        return new StepResult(s.getMin(), s.getAverage(), s.getMax(), pop);
    }
}