package com.danifgx.blockchain.model;

import com.danifgx.blockchain.util.StringUtil;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Block implements Serializable {
    private final String previousHash;
    private final List<Transaction> transactions;
    private int nonce;
    private String hash;

    @Builder
    public Block(String previousHash, List<Transaction> transactions) {
        if (previousHash == null || transactions == null) {
            throw new IllegalArgumentException("Fields cannot be null");
        }
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.nonce = 0; // Puedes establecer el valor por defecto aquí
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
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }
}
