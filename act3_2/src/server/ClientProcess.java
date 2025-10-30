package server;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import operation.operation;

public class ClientProcess extends Thread {

    private Socket s;
    private int count;
    private static int totalOperations = 0;

    public ClientProcess(Socket s, int count) {
        this.s = s;
        this.count = count;
    }
    public void run() {
        System.out.println("Client n°" + count + " depuis " + s.getRemoteSocketAddress());

        try {
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            out.println("Client n°" + count + " depuis " + s.getRemoteSocketAddress());
            out.println("Numéro d'ordre de connexion : " + count);
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            operation f = (operation) ois.readObject();

            int x = f.getPremier();
            int z = f.getDeuxieme();
            String op = f.getOp();

            System.out.println("Formule reçue : " + x + " " + op + " " + z);
            CompletableFuture<Double> futureResult = CompletableFuture.supplyAsync(() -> {
                try {
                 
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                double resultat = 0;
                switch (op) {
                    case "+": resultat = x + z; break;
                    case "-": resultat = x - z; break;
                    case "*": resultat = x * z; break;
                    case "/":
                        if (z != 0) resultat = (double) x / z;
                        else throw new ArithmeticException("Division par zéro !");
                        break;
                    default: throw new IllegalArgumentException("Opérateur invalide !");
                }
                return resultat;
            });

 
            futureResult.thenAccept(resultat -> {
                out.println("Résultat : " + resultat);
                synchronized (ClientProcess.class) {
                    totalOperations++;
                    System.out.println(" Total opérations : " + totalOperations);
                }
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).exceptionally(ex -> {
                out.println("Erreur : " + ex.getMessage());
                try { s.close(); } catch (IOException e) { e.printStackTrace(); }
                return null;
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}