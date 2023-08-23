package com.danifgx.blockchain.persistence;

import com.danifgx.blockchain.model.Block;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BlockchainPersistence {

    public void save(List<Block> blockchain, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(blockchain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Block> load(String filename) {
        List<Block> blockchain = new ArrayList<>();
        File file = new File(filename);
        if (file.exists() && file.length() > 0) {
            try (FileInputStream fileIn = new FileInputStream(filename);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                blockchain = (List<Block>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist or is empty, creating a new blockchain.");
        }
        return blockchain;
    }
}
