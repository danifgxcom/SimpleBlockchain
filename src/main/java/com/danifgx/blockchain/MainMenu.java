package com.danifgx.blockchain;

import com.danifgx.blockchain.model.Block;
import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.KeyGeneratorUtil;
import com.danifgx.blockchain.util.StringUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private final Blockchain blockchain;

    public MainMenu(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Ver cadena de bloques");
            System.out.println("2. Agregar transacción");
            System.out.println("3. Minar bloque");
            System.out.println("4. Verificar cadena");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.println(blockchain.toString());
                    break;
                case 2:
                    KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
                    String publicKeyAsString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

                    System.out.println("Clave pública generada: " + publicKeyAsString);
                    System.out.print("Introduce la clave pública del remitente como una cadena (pulsa Enter para usar la clave generada): ");

                    String inputPublicKey = scanner.nextLine().trim();
                    if (inputPublicKey.isEmpty()) {
                        inputPublicKey = publicKeyAsString;
                    }
                    System.out.print("Introduce la clave pública del remitente como una cadena: ");
                    String senderKeyString = scanner.nextLine();
                    if (senderKeyString.isEmpty()) {
                        senderKeyString = publicKeyAsString;
                    }

                    PublicKey sender = StringUtil.getKeyFromString(senderKeyString);

                    String defaultReceiver = "default_receiver_ID";
                    System.out.print("Introduce el receptor (ID) (pulsa Intro para usar el valor predeterminado: " + defaultReceiver + "): ");
                    String receiver = scanner.nextLine();

                    if (receiver.isEmpty()) {
                        receiver = defaultReceiver;
                    }

                    double defaultValue = 0.0;
                    System.out.print("Introduce la cantidad (pulsa Intro para usar el valor predeterminado: " + defaultValue + "): ");
                    String valueInput = scanner.nextLine();

                    double value;
                    if (valueInput.isEmpty()) {
                        value = defaultValue;
                    } else {
                        value = Double.parseDouble(valueInput);
                    }


                    String defaultPrivateKeyString = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgeaR5iesIVa8FC0BeHKzmCwCoxK7hlje6WvpZJHHWSm2gCgYIKoZIzj0DAQehRANCAARbmFT/qWZ90uZFlFouB4LtrGl9dldaOwErpKcN4zsiLJmjz8dJ0ROlBNaxvc1WB1N/klq/cc9l8uxWeZYQIArc";
                    System.out.print("Introduce la clave privada del remitente como una cadena (pulsa Intro para usar la clave predeterminada): ");
                    String privateKeyString = scanner.nextLine();

                    PrivateKey privateKey;
                    if (privateKeyString.isEmpty()) {
                        privateKey = StringUtil.getPrivateKeyFromString(defaultPrivateKeyString);
                    } else {
                        privateKey = StringUtil.getPrivateKeyFromString(privateKeyString);
                    }


                    Transaction transaction = new Transaction(sender, receiver, value);
                    transaction.signTransaction(privateKey);

                    Block lastBlock = blockchain.getChain().get(blockchain.getChain().size() - 1);
                    lastBlock.addTransaction(transaction);
                    break;
                case 3:
                    List<Transaction> transactions = new ArrayList<>(); // Esto debería estar lleno de las transacciones que quieres incluir
                    blockchain.addBlock(transactions);
                    System.out.println("Bloque minado y añadido a la cadena.");
                    break;
                case 4:
                    if (blockchain.isValid()) {
                        System.out.println("La cadena de bloques es válida.");
                    } else {
                        System.out.println("La cadena de bloques no es válida.");
                    }
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, inténtelo de nuevo.");
                    break;
            }
        }
        scanner.close();
        System.out.println("¡Gracias por utilizar la aplicación!");
    }
}
