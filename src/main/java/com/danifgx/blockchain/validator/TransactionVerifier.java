package com.danifgx.blockchain.validator;

import com.danifgx.blockchain.model.Block;
import com.danifgx.blockchain.model.Blockchain;
import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.StringUtil;

import java.security.PublicKey;

public class TransactionVerifier {

    private Blockchain blockchain;

    public TransactionVerifier(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public boolean verifyTransaction(Transaction transaction) {
        double senderBalance = getBalance(transaction.getSender());
        if (senderBalance < transaction.getValue()) {
            return false;
        }
        return true;
    }

    private double getBalance(PublicKey publicKey) {
        double balance = 0;

        for (Block block : blockchain.getChain()) {
            for (Transaction transaction : block.getTransactions()) {
                if (transaction.getSender().equals(publicKey)) {
                    balance -= transaction.getValue(); // Restar si es el emisor
                }
                if (transaction.getRecipient().equals(StringUtil.getStringFromKey(publicKey))) {
                    balance += transaction.getValue(); // Sumar si es el receptor
                }
            }
        }

        return balance;
    }
}
