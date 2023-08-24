package com.danifgx.blockchain.server;

import com.danifgx.blockchain.model.Block;
import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.server.client.ClientHandler;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class CentralServer {
    private static final int PORT = 8080;
    private static final String FILENAME = "blockchain.dat";

    public static void main(String[] args) throws IOException {
        File file = new File(FILENAME);
        if (!file.exists()) {
            file.createNewFile();
        }

        Blockchain temporaryBlockchain = new Blockchain(null); // Create temporary blockchain to load the file
        List<Block> loadedChain = temporaryBlockchain.loadBlockchain(FILENAME);
        Blockchain blockchain = new Blockchain(loadedChain); // Create the actual blockchain with loaded data

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket, blockchain);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }

        blockchain.saveBlockchain(blockchain.getChain(), FILENAME);
    }
}
