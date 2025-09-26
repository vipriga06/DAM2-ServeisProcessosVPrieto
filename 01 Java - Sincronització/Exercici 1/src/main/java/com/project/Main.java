package com.project;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int NUM_TASQUES = 3;
    private static final double[] DADES = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0}; // Conjunt de dades d'exemple
    private static double[] resultats = new double[NUM_TASQUES]; // 0: suma, 1: mitjana, 2: desviació

    public static void main(String[] args) {
        // Crear executor per executar tasques en paral·lel
        ExecutorService executor = Executors.newFixedThreadPool(NUM_TASQUES);
        
        // Crear barrera per sincronitzar els càlculs
        CyclicBarrier barrera = new CyclicBarrier(NUM_TASQUES, () -> {
            // Acció al arribar: mostrar els resultats finals
            System.out.println("Suma: " + resultats[0]);
            System.out.println("Mitjana: " + resultats[1]);
            System.out.println("Desviacio estandard: " + String.format("%.2f", resultats[2]));
        });

        // Tasca 1: Càlcul de la suma
        executor.submit(() -> {
            try {
                double suma = Arrays.stream(DADES).sum();
                resultats[0] = suma;
                System.out.println("Calcul de la suma completat.");
                barrera.await(); // Esperar sincronització
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error en calcul de suma: " + e.getMessage());
            }
        });

        // Tasca 2: Càlcul de la mitjana
        executor.submit(() -> {
            try {
                double suma = Arrays.stream(DADES).sum();
                double mitjana = suma / DADES.length;
                resultats[1] = mitjana;
                System.out.println("Calcul de la mitjana completat.");
                barrera.await(); // Esperar sincronització
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error en calcul de mitjana: " + e.getMessage());
            }
        });

        // Tasca 3: Càlcul de la desviació estàndard
        executor.submit(() -> {
            try {
                double mitjana = Arrays.stream(DADES).average().orElse(0.0);
                double sumaQuadrats = Arrays.stream(DADES).map(x -> Math.pow(x - mitjana, 2)).sum();
                double desviacio = Math.sqrt(sumaQuadrats / DADES.length);
                resultats[2] = desviacio;
                System.out.println("Calcul de la desviació estàndard completat.");
                barrera.await(); // Esperar sincronització
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
                System.err.println("Error en calcul de desviació: " + e.getMessage());
            }
        });

        // Tancar l'executor
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Esperar terminació de totes les tasques
        }
        System.out.println("Tots els calculs estadistics han completat.");
    }
}
