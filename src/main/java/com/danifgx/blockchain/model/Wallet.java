package com.danifgx.blockchain.model;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private double balance;

    public Wallet(PrivateKey privateKey, PublicKey publicKey, double initialBalance) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.balance = initialBalance;
    }

    public Transaction createTransaction(PublicKey recipient, double value) {
        if (!hasSufficientFunds(value)) {
            System.out.println("No hay fondos suficientes para enviar la transacción. Transacción Descartada.");
            return null;
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value);
        newTransaction.signTransaction(privateKey);
        return newTransaction;
    }

    public void debit(double amount) {
        if (hasSufficientFunds(amount)) {
            balance -= amount;
        }
        // handle error if necessary
    }

    public void credit(double amount) {
        balance += amount;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public double getBalance() {
        return balance;
    }

    private boolean hasSufficientFunds(double amount) {
        return amount <= balance;
    }
}
