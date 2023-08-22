package server;

import com.danifgx.blockchain.model.Blockchain;
import lombok.SneakyThrows;
import server.client.ClientHandler;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CentralServer {
    private static final int PORT = 8080;
    private static final String FILENAME = "blockchain.dat";

    @SneakyThrows
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        File file = new File(FILENAME);
        if (!file.exists()) {
            file.createNewFile();
        }
        blockchain.setChain(blockchain.loadBlockchain(FILENAME));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler clientHandler = new ClientHandler(socket, blockchain);
                // Aquí usamos un nuevo hilo para manejar múltiples clientes simultáneamente
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }

        blockchain.saveBlockchain(blockchain.getChain(), FILENAME);
    }
}
