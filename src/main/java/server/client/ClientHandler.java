package server.client;

import com.danifgx.blockchain.model.Blockchain;
import lombok.AllArgsConstructor;

import java.io.*;
import java.net.Socket;

@AllArgsConstructor
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Blockchain blockchain;


    @Override
    public void run() {
        try (
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
        ) {
            String clientRequest = reader.readLine();
            if (clientRequest.startsWith("ADD_TRANSACTION")) {
                handleAddTransaction(clientRequest, writer);
            } else if (clientRequest.equals("GET_BLOCKCHAIN")) {
                handleGetBlockchain(writer);
            } else if (clientRequest.startsWith("SYNC_BLOCKCHAIN")) {
                handleSyncBlockchain(clientRequest, writer);
            } else if (clientRequest.equals("VALIDATE_BLOCKCHAIN")) {
                handleValidateBlockchain(writer);
            } else {
                writer.println("ERROR:Unknown Request");
            }
        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void handleAddTransaction(String request, PrintWriter writer) {
        String[] parts = request.split(":");
        if (parts.length == 5) {
            String sender = parts[1];
            String receiver = parts[2];
            double amount = Double.parseDouble(parts[3]);
            String privateKey = parts[4];

            // TODO: Create and add transaction to blockchain
            // Respond with SUCCESS or ERROR as needed
            writer.println("SUCCESS");
        } else {
            writer.println("ERROR:Malformed Request");
        }
    }

    private void handleGetBlockchain(PrintWriter writer) {
        // TODO: Serialize and send the blockchain data
        String serializedBlockchain = ""; // Serialize the blockchain object
        writer.println(serializedBlockchain);
    }

    private void handleSyncBlockchain(String request, PrintWriter writer) {
        String serializedData = request.substring(15); // Extract serialized data
        // TODO: Deserialize and sync the blockchain data
        // Respond with SUCCESS or ERROR as needed
        writer.println("SUCCESS");
    }

    private void handleValidateBlockchain(PrintWriter writer) {
        // TODO: Validate the blockchain
        boolean isValid = blockchain.isValid();
        writer.println(isValid ? "VALID" : "INVALID");
    }
}
