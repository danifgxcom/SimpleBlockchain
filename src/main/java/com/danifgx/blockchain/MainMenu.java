package com.danifgx.blockchain;

import com.danifgx.blockchain.model.Block;
import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.KeyGeneratorUtil;
import com.danifgx.blockchain.util.StringUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
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
            System.out.println("5. Guardar cadena de bloques en disco");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.println(blockchain.toString());
                    break;
                case 2:
                    System.out.print("Ingrese la clave pública del remitente (Enter para clave aleatoria): ");
                    String senderKeyString = scanner.nextLine().trim();
                    PrivateKey privateKey = null;
                    if (senderKeyString.isEmpty()) {
                        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
                        senderKeyString = StringUtil.getStringFromKey(keyPair.getPublic());
                        privateKey = keyPair.getPrivate();
                    }
                    PublicKey sender = StringUtil.getKeyFromString(senderKeyString);
                    System.out.println("Sender Public Key: " + StringUtil.toEcPublicKeyString(sender));
                    System.out.print("Ingrese la clave pública del destinatario (Enter para clave aleatoria): ");
                    String receiverKeyString = scanner.nextLine().trim();
                    if (receiverKeyString.isEmpty()) {
                        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
                        receiverKeyString = StringUtil.getStringFromKey(keyPair.getPublic());
                    }
                    PublicKey receiver = StringUtil.getKeyFromString(receiverKeyString);
                    System.out.println("Receiver Public Key: " + StringUtil.toEcPublicKeyString(receiver));
                    System.out.print("Ingrese el valor de la transacción (Enter para valor por defecto de 1.0): ");
                    String valueString = scanner.nextLine().trim();
                    double value = valueString.isEmpty() ? 1.0 : Double.parseDouble(valueString);
                    System.out.print("Ingrese la clave privada para firmar la transacción (Enter para usar la clave privada del remitente): ");
                    String privateKeyString = scanner.nextLine();
                    if (!privateKeyString.isEmpty()) {
                        privateKey = StringUtil.getPrivateKeyFromString(privateKeyString);
                    }
                    Transaction transaction = new Transaction(sender, receiver, value);
                    transaction.signTransaction(privateKey);
                    Block lastBlock = blockchain.getChain().get(blockchain.getChain().size() - 1);
                    lastBlock.addTransaction(transaction);
                    System.out.println("Transacción agregada.");
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
                    System.out.print("Ingrese el nombre del archivo para guardar (presione Enter para el nombre por defecto): ");
                    String filename = scanner.nextLine().trim();
                    if (filename.isEmpty()) {
                        filename = "defaultBlockchainFile";
                    }
                    blockchain.saveBlockchain(blockchain.getChain(), filename);
                    System.out.println("Cadena de bloques guardada en el archivo: " + filename);
                    break;

                case 0:
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
