package com.project;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int CAPACITAT_APARCAMENT = 5;
    private static final int NUM_COTXES = 10;

    public static void main(String[] args) {
        // Crear semàfor amb capacitat limitada (permisos = espais disponibles)
        Semaphore semafor = new Semaphore(CAPACITAT_APARCAMENT, true); // Semàfor just (fair)
        Aparcament aparcament = new Aparcament(semafor);

        // Crear executor per simular cotxes concurrents
        ExecutorService executor = Executors.newFixedThreadPool(NUM_COTXES);

        // Crear tasques per cada cotxe (simular entrada i sortida)
        for (int i = 1; i <= NUM_COTXES; i++) {
            final int numCotxe = i;
            executor.submit(() -> {
                try {
                    // Intentar entrar a l'aparcament
                    if (aparcament.entrar(numCotxe)) {
                        System.out.println("Cotxe " + numCotxe + " ha entrat. Espais lliures: " + aparcament.getEspaisLliures());
                        // Simular estada a l'aparcament amb un retard aleatori
                        Thread.sleep(2000 + (int)(Math.random() * 3000));
                        aparcament.sortir(numCotxe);
                        System.out.println("Cotxe " + numCotxe + " ha sortit. Espais lliures: " + aparcament.getEspaisLliures());
                    } else {
                        System.out.println("Cotxe " + numCotxe + " espera: aparcament ple.");
                        // Esperar un moment i reintentar
                        TimeUnit.SECONDS.sleep(1);
                        if (aparcament.entrar(numCotxe)) {
                            System.out.println("Cotxe " + numCotxe + " ha entrat després d'esperar.");
                        } else {
                            System.out.println("Cotxe " + numCotxe + " no pot entrar: encara ple.");
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Error en cotxe " + numCotxe + ": " + e.getMessage());
                }
            });
        }

        // Tancar l'executor
        executor.shutdown();
        while (!executor.isTerminated()) {
            // Esperar terminació de la simulació
        }
        System.out.println("Simulació d'aparcament acabada.");
    }

    // Classe per gestionar l'aparcament
    static class Aparcament {
        private final Semaphore semafor;
        private int espaisLliures;

        public Aparcament(Semaphore semafor) {
            this.semafor = semafor;
            this.espaisLliures = semafor.availablePermits();
        }

        // Mètode per entrar: adquireix un permís si està disponible
        public boolean entrar(int numCotxe) throws InterruptedException {
            if (semafor.tryAcquire(1, TimeUnit.SECONDS)) { // Intentar amb timeout d'1 segon
                espaisLliures--;
                return true;
            }
            return false; // No s'ha pogut adquirir (aparcament ple)
        }

        // Mètode per sortir: allibera un permís
        public void sortir(int numCotxe) {
            semafor.release();
            espaisLliures++;
        }

        public int getEspaisLliures() {
            return espaisLliures;
        }
    }
}
