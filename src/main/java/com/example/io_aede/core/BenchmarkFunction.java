package com.example.io_aede.core;

import java.util.function.ToDoubleFunction;

public enum BenchmarkFunction {
    SPHERE("Sphere", -5.12, 5.12, x -> {
        double sum = 0;
        for (double v : x) sum += v * v;
        return sum;
    }),
    RASTRIGIN("Rastrigin", -5.12, 5.12, x -> {
        double sum = 10 * x.length;
        for (double v : x) sum += (v * v - 10 * Math.cos(2 * Math.PI * v));
        return sum;
    }),
    ACKLEY("Ackley", -32.768, 32.768, x -> {
        double s1 = 0, s2 = 0;
        for (double v : x) { s1 += v*v; s2 += Math.cos(2*Math.PI*v); }
        return -20 * Math.exp(-0.2 * Math.sqrt(s1/x.length)) - Math.exp(s2/x.length) + 20 + Math.E;
    }),
    ROSENBROCK("Rosenbrock", -5.0, 10.0, x -> {
        double sum = 0;
        for (int i = 0; i < x.length - 1; i++)
            sum += 100 * Math.pow(x[i+1] - x[i]*x[i], 2) + Math.pow(1 - x[i], 2);
        return sum;
    }),
    GRIEWANK("Griewank", -600.0, 600.0, x -> {
        double sum = 0, prod = 1;
        for (int i = 0; i < x.length; i++) {
            sum += (x[i]*x[i])/4000.0;
            prod *= Math.cos(x[i]/Math.sqrt(i+1));
        }
        return sum - prod + 1;
    });

    public final String name;
    public final double min, max;
    public final ToDoubleFunction<double[]> func;

    BenchmarkFunction(String n, double min, double max, ToDoubleFunction<double[]> f)
    { this.name = n; this.min = min; this.max = max; this.func = f; }
}