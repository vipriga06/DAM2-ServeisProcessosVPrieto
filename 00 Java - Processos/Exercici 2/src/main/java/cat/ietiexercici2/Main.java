package cat.ietiexercici2;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciant proces asincron...");

        CompletableFuture<Void> cadena = CompletableFuture.supplyAsync(() -> {
            // Primera tasca: validar dades
            System.out.println("[Etapa 1] Validant sol-licitud de l'usuari...");
            try {
                Thread.sleep(1000); // simulació de validació
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Dades validades correctament";
        }).thenApply(dades -> {
            // Segona tasca: processar dades
            System.out.println("[Etapa 2] Processant dades...");
            try {
                Thread.sleep(1500); // simulació de càlcul
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return dades + " -> Resultat calculat: 42";
        }).thenAccept(resultat -> {
            // Tercera tasca: mostrar resposta final
            System.out.println("[Etapa 3] Enviant resposta a l'usuari...");
            System.out.println("[Resultat final] " + resultat);
        });

        // Esperar que tota la cadena acabi
        cadena.join();

        System.out.println("Procés complet.");
    }
}
