package com.exercici0;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int NUM_MICROSEVEIS = 3;
    private static String[] resultatsParcials = new String[NUM_MICROSEVEIS];

    public static void main(String[] args) {
        // Crear executor per gestionar fils
        ExecutorService executor = Executors.newFixedThreadPool(NUM_MICROSEVEIS);
        
        // Crear barrera cíclica per sincronitzar els microserveis
        CyclicBarrier barrera = new CyclicBarrier(NUM_MICROSEVEIS, () -> {
            // Acció executada quan tots arriben a la barrera: combinar resultats
            StringBuilder resultatFinal = new StringBuilder("Resultat final: ");
            for (String resultat : resultatsParcials) {
                resultatFinal.append(resultat).append(" ");
            }
            System.out.println(resultatFinal.toString().trim());
        });

        // Crear i executar tasques per cada microservei
        for (int i = 0; i < NUM_MICROSEVEIS; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // Simular processament de dades amb un retard
                    Thread.sleep(1000 + (index * 500)); // Temps diferent per simular càrrega variable
                    resultatsParcials[index] = "Dades processades per Microservei " + (index + 1);
                    System.out.println("Microservei " + (index + 1) + " ha acabat el seu processament.");
                    barrera.await(); // Esperar que tots els microserveis arribin
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Error en microservei " + (index + 1) + ": " + e.getMessage());
                }
            });
        }

        // Tancar l'executor
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Esperar fins que totes les tasques acabin
        }
        System.out.println("Tots els microserveis han completat el seu treball.");
    }
}
