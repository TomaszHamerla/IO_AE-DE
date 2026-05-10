package com.example.io_aede.core;

/**
 * Przechowuje statystyki i stan populacji po pojedynczej generacji.
 * @param best     Wartość funkcji celu najlepszego osobnika (minimum).
 * @param avg      Średnia wartość funkcji celu w całej populacji.
 * @param worst    Wartość funkcji celu najgorszego osobnika.
 * @param population Tablica współrzędnych wszystkich osobników (do wizualizacji).
 */
public record StepResult(
        double best,
        double avg,
        double worst,
        double[][] population
) {}