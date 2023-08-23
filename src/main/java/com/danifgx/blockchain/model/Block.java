package com.danifgx.blockchain.model;

import com.danifgx.blockchain.interfaces.DefaultHashCalculator;
import com.danifgx.blockchain.interfaces.HashCalculator;
import com.danifgx.blockchain.util.StringUtil;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Block implements Serializable {
    private final String previousHash;
    private final HashCalculator hashCalculator;
    private List<Transaction> transactions;
    private int nonce;
    private String hash;


    @Builder
    public Block(String previousHash, List<Transaction> transactions, HashCalculator hashCalculator) {
        if (previousHash == null || transactions == null || hashCalculator == null) {
            throw new IllegalArgumentException("Fields cannot be null");
        }
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.nonce = 0;
        this.hashCalculator = hashCalculator;
        this.hash = calculateHash();
    }

    public Block(String previousHash, List<Transaction> transactions) {
        this(previousHash, transactions, new DefaultHashCalculator());
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
            this.transactions = new ArrayList<>(transactions);
            this.transactions.add(transaction);
        }
    }

}
