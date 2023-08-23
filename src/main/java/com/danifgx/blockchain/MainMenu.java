package com.danifgx.blockchain;

import com.danifgx.blockchain.model.Block;
import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.KeyGeneratorUtil;
import com.danifgx.blockchain.util.StringUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
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
                    String senderKeyString = scanner.nextLine().trim();
                    if (senderKeyString.isEmpty()) {
                        senderKeyString = publicKeyAsString;
                    }
                    PublicKey sender = StringUtil.getKeyFromString(senderKeyString);
                    String receiverKeyString = scanner.nextLine().trim();
                    PublicKey receiver = StringUtil.getKeyFromString(receiverKeyString);
                    double value = Double.parseDouble(scanner.nextLine());
                    String privateKeyString = scanner.nextLine();
                    PrivateKey privateKey = StringUtil.getPrivateKeyFromString(privateKeyString);
                    Transaction transaction = new Transaction(sender, receiver, value);
                    transaction.signTransaction(privateKey);
                    Block lastBlock = blockchain.getChain().get(blockchain.getChain().size() - 1);
                    lastBlock.addTransaction(transaction);
                    break;
                case 3:
                    Block currentBlock = blockchain.getCurrentBlock();
                    if (!currentBlock.getTransactions().isEmpty()) {
                        blockchain.addBlock(currentBlock.getTransactions());
                        System.out.println("Bloque minado y añadido a la cadena.");
                    } else {
                        System.out.println("No hay transacciones para minar.");
                    }
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
