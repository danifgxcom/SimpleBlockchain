package com.danifgx.blockchain.model;

import com.danifgx.blockchain.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
public class Blockchain {

    private static final int DIFFICULTY = 4;
    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        List<Transaction> transactions = new ArrayList<>();
        return new Block("0", transactions);
    }


    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    // Método para agregar un bloque a la cadena
    public void addBlock(Block newBlock) {
        newBlock.setPreviousHash(getLastBlock().getHash());
        newBlock.mine(DIFFICULTY); // Puedes implementar la minería según lo desees
        chain.add(newBlock);
    }

    // Método para validar la integridad de la cadena
    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Verificar que los hashes sean correctos
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) ||
                    !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }

    public void saveBlockchain(List<Block> blockchain, String filename) {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(blockchain);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Block> loadBlockchain(String filename) {
        List<Block> blockchain = null;
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            blockchain = (List<Block>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return blockchain;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Blockchain: \n");
        for (Block block : chain) {
            builder.append("\tBlock: \n");
            builder.append("\t\tPrevious Hash: ").append(block.getPreviousHash()).append("\n");
            builder.append("\t\tNonce: ").append(block.getNonce()).append("\n");
            builder.append("\t\tHash: ").append(block.getHash()).append("\n");
            builder.append("\t\tTransactions: \n");
            for (Transaction transaction : block.getTransactions()) {
                builder.append("\t\t\tSender: ").append(StringUtil.getStringFromKey(transaction.getSender())).append("\n");
                builder.append("\t\t\tRecipient: ").append(transaction.getRecipient()).append("\n");
                builder.append("\t\t\tValue: ").append(transaction.getValue()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
