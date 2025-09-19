package cat.ietiexercici0;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Crear pool de 2 fils
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Definir tasques Runnable
        Runnable tasca1 = () -> {
            System.out.println("Iniciant registre d’esdeveniments de sistema...");
            try {
                Thread.sleep(2000); // simulació de feina
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Esdeveniments de sistema registrats correctament.");
        };

        Runnable tasca2 = () -> {
            System.out.println("Comprovant l'estat de la xarxa...");
            try {
                Thread.sleep(3000); // simulació de feina
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Estat de la xarxa comprovat.");
        };

        // Enviar tasques a l'executor
        executor.execute(tasca1);
        executor.execute(tasca2);

        // Tancar executor
        executor.shutdown();
    }
}
