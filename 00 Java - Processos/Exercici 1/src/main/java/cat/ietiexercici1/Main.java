package cat.ietiexercici1;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) {
        // Estructura concurrent per compartir dades
        ConcurrentHashMap<String, Double> compte = new ConcurrentHashMap<>();

        // Executor amb 3 fils
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Tasca 1: afegeix dades inicials (simulació d’una operació bancària rebuda)
        Runnable introduirOperacio = () -> {
            System.out.println("[Tasca1] Rebent operació inicial...");
            compte.put("saldo", 1000.0);
            System.out.println("[Tasca1] Saldo inicial registrat: " + compte.get("saldo"));
        };

        // Tasca 2: modifica les dades (simulació de càlcul d’interessos/comissions)
        Runnable calcularInteressos = () -> {
            System.out.println("[Tasca2] Calculant interessos...");
            try {
                Thread.sleep(1000); // simula càlcul
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            compte.computeIfPresent("saldo", (k, v) -> v * 1.02); // +2% interessos
            System.out.println("[Tasca2] Saldo amb interessos: " + compte.get("saldo"));
        };

        // Tasca 3: Callable que llegeix el saldo final i el retorna
        Callable<String> obtenirSaldoFinal = () -> {
            System.out.println("[Tasca3] Llegint saldo final...");
            Thread.sleep(500); // simula accés al sistema
            Double saldo = compte.getOrDefault("saldo", 0.0);
            return "Saldo actualitzat del client: " + saldo;
        };

        try {
            // Llançar les tasques
            executor.execute(introduirOperacio);
            executor.execute(calcularInteressos);
            Future<String> resultat = executor.submit(obtenirSaldoFinal);

            // Mostrar resultat de la tasca Callable
            System.out.println("[Resultat] " + resultat.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
