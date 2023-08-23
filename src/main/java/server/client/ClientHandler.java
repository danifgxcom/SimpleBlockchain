package server.client;

import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.StringUtil;
import lombok.AllArgsConstructor;
import server.authentication.UserAuthentication;

import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;

@AllArgsConstructor
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Blockchain blockchain;
    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, Blockchain blockchain) {
        this.socket = socket;
        this.blockchain = blockchain;
        try {
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            output.writeUTF("Please enter your username: ");
            String username = input.readUTF();
            output.writeUTF("Please enter your password: ");
            String password = input.readUTF();
            boolean authenticated = UserAuthentication.authenticate(username, password);
            output.writeBoolean(authenticated);

            if (authenticated) {
                System.out.println("Authenticated!");
                String clientRequest = input.readUTF();
                handleRequest(clientRequest);
            } else {
                System.out.println("Authentication failed!");
            }
        } catch (IOException e) {
            System.out.println("Client exception: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleRequest(String clientRequest) throws IOException {
        if (clientRequest.startsWith("ADD_TRANSACTION")) {
            handleAddTransaction(clientRequest);
        } else if (clientRequest.equals("GET_BLOCKCHAIN")) {
            handleGetBlockchain();
        } else if (clientRequest.startsWith("SYNC_BLOCKCHAIN")) {
            handleSyncBlockchain(clientRequest);
        } else if (clientRequest.equals("VALIDATE_BLOCKCHAIN")) {
            handleValidateBlockchain();
        } else {
            output.writeUTF("ERROR:Unknown Request");
        }
    }

    private void handleAddTransaction(String request) throws IOException {
        String[] parts = request.split(":");
        if (parts.length == 5) {
            String sender = parts[1];
            String receiver = parts[2];
            double amount = Double.parseDouble(parts[3]);
            String privateKeyString = parts[4];
            PrivateKey privateKey = StringUtil.getPrivateKeyFromString(privateKeyString);
            PublicKey senderKey = StringUtil.getKeyFromString(sender);
            Transaction transaction = new Transaction(senderKey, receiver, amount);
            transaction.signTransaction(privateKey);


            boolean success = blockchain.addTransaction(transaction);
            output.writeUTF(success ? "SUCCESS" : "ERROR");
        } else {
            output.writeUTF("ERROR:Malformed Request");
        }
    }


    private void handleGetBlockchain() throws IOException {
        // TODO: Serialize and send the blockchain data
        String serializedBlockchain = ""; // Serialize the blockchain object
        output.writeUTF(serializedBlockchain);
    }

    private void handleSyncBlockchain(String request) throws IOException {
        String serializedData = request.substring(15); // Extract serialized data
        // TODO: Deserialize and sync the blockchain data
        // Respond with SUCCESS or ERROR as needed
        output.writeUTF("SUCCESS");
    }

    private void handleValidateBlockchain() throws IOException {
        // TODO: Validate the blockchain
        boolean isValid = blockchain.isValid();
        output.writeUTF(isValid ? "VALID" : "INVALID");
    }
}
