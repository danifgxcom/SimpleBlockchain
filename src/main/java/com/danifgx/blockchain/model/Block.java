package com.danifgx.blockchain.model;

import com.danifgx.blockchain.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Block implements Serializable {
    private String previousHash;
    private List<Transaction> transactions;
    private int nonce;
    private String hash;

    public Block(String previousHash, List<Transaction> transactions) {
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String transactionsString = transactions.stream().map(Transaction::toString).reduce("", String::concat);
        return StringUtil.applySha256(previousHash + transactionsString + Integer.toString(nonce));
    }

    public void mine(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Crea una cadena con 'difficulty' ceros
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Bloque minado: " + hash);
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

}
