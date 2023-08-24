package com.danifgx.blockchain.model;

import com.danifgx.blockchain.interfaces.DefaultHashCalculator;
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
public class Blockchain implements Serializable{

    private static final int DIFFICULTY = 4;
    private List<Block> chain;

    public Blockchain(List<Block> chain) {
        this.chain = chain != null ? chain : new ArrayList<>();
        if (this.chain.isEmpty()) {
            this.chain.add(createGenesisBlock());
        }
    }


    private Block createGenesisBlock() {
        return new Block("0", new ArrayList<>());
    }

    public Block getLastBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(List<Transaction> transactions) {
        String previousHash = getLastBlock().getHash();
        Block newBlock = Block.builder()
                .previousHash(previousHash)
                .transactions(transactions)
                .hashCalculator(new DefaultHashCalculator())
                .build();

        newBlock.mine(DIFFICULTY);
        chain.add(newBlock);
    }


    public boolean addTransaction(Transaction transaction) {
        if (transaction != null) {
            Block currentBlock = getCurrentBlock();
            currentBlock.addTransaction(transaction);
            return true;
        }
        return false;
    }

    public Block getCurrentBlock() {
        return this.chain.get(this.chain.size() - 1);
    }

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
                builder.append("\t\t\tSender: ").append(StringUtil.toEcPublicKeyString(transaction.getSender())).append("\n");
                builder.append("\t\t\tRecipient: ").append(StringUtil.toEcPublicKeyString(transaction.getRecipient())).append("\n");
                builder.append("\t\t\tValue: ").append(transaction.getValue()).append("\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}