package com.example.io_aede.service;

import com.example.io_aede.core.StepResult;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

    private final List<ComparisonEntry> history = new ArrayList<>();

    private record ComparisonEntry(int gen, StepResult ae, StepResult de) {}

    public void addEntry(int generation, StepResult aeResult, StepResult deResult) {
        history.add(new ComparisonEntry(generation, aeResult, deResult));
    }

    /**
     * Drukuje w konsoli sformatowaną tabelę porównawczą.
     */
    public void printConsoleReport() {
        System.out.println("\n" + "=".repeat(90));
        System.out.printf("%-5s | %-20s | %-20s | %-20s | %-20s\n",
                "Gen", "AE Best", "DE Best", "AE Avg", "DE Avg");
        System.out.println("-".repeat(90));

        for (var entry : history) {
            System.out.printf("%-5d | %-20.6f | %-20.6f | %-20.6f | %-20.6f\n",
                    entry.gen(),
                    entry.ae().best(), entry.de().best(),
                    entry.ae().avg(), entry.de().avg());
        }
        System.out.println("=".repeat(90));
    }

    /**
     * Zapisuje raport do pliku CSV, który można łatwo otworzyć w Excelu.
     */
    public void saveToCsv(String fileName) {
        try (PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8)) {
            writer.println("Generation;AE_Best;DE_Best;AE_Avg;DE_Avg;AE_Worst;DE_Worst");
            for (var e : history) {
                writer.printf("%d;%.10f;%.10f;%.10f;%.10f;%.10f;%.10f\n",
                        e.gen(),
                        e.ae().best(), e.de().best(),
                        e.ae().avg(), e.de().avg(),
                        e.ae().worst(), e.de().worst());
            }
            System.out.println("Raport zapisany do pliku: " + fileName);
        } catch (Exception e) {
            System.err.println("Błąd podczas zapisu raportu: " + e.getMessage());
        }
    }
}
