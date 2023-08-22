package com.danifgx.blockchain.model;

import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Setter
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private double balance;

    public Transaction createTransaction(String recipient, double value) {
        if (value > balance) {
            System.out.println("No hay fondos suficientes para enviar la transacción. Transacción Descartada.");
            return null;
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value);
        newTransaction.signTransaction(privateKey);
        return newTransaction;
    }
}
