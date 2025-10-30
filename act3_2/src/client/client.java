package client;
import operation.operation;
import java.net.*;
import java.io.*;
import java.util.*;
public class client {
    public static void main(String[] args) {
        try {
            InetAddress serverIP = InetAddress.getLocalHost();
            InetSocketAddress add = new InetSocketAddress(serverIP, 1234);
            Socket s = new Socket();
            s.connect(add);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println(br.readLine());
            System.out.println(br.readLine());
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            Scanner sc = new Scanner(System.in);
            System.out.print("Donner le premier nombre : ");
            int x = sc.nextInt();
            System.out.print("Donner l'opération (+, -, *, /) : ");
            String y = sc.next();
            System.out.print("Donner le deuxième nombre : ");
            int z = sc.nextInt();

            if (!y.equals("+") && !y.equals("-") && !y.equals("*") && !y.equals("/")) {
                System.out.println("Opérateur invalide !");
                s.close();
              
            }
            operation f = new operation(x, y, z);
            oos.writeObject(f);
            oos.flush();
            String resultat = br.readLine();
            System.out.println("Résultat : " + resultat);
            oos.close();
            br.close();
            s.close();
            sc.close();

        } catch (UnknownHostException e) {
            System.err.println("Erreur : hôte introuvable !");
        } catch (IOException e) {
            System.err.println("Erreur d’entrée/sortie : " + e.getMessage());
        }
    }
}