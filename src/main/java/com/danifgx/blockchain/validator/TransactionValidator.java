package com.danifgx.blockchain.validator;

import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.StringUtil;

import java.security.PublicKey;
import java.security.Signature;

public class TransactionValidator {

    public boolean isTransactionValid(Transaction transaction, double balanceSender) {
        if (!isSignatureValid(transaction)) {
            System.out.println("Transacción inválida: firma no válida");
            return false;
        }

        if (transaction.getValue() > balanceSender) {
            System.out.println("Transacción inválida: fondos insuficientes");
            return false;
        }

        return true;
    }

    private boolean isSignatureValid(Transaction transaction) {
        try {
            PublicKey publicKey = transaction.getSender(); // Suponiendo que el remitente es un PublicKey
            Signature verifySignature = Signature.getInstance("ECDSA", "BC");
            verifySignature.initVerify(publicKey);
            String data = StringUtil.getStringFromKey(publicKey) + transaction.getRecipient() + Double.toString(transaction.getValue());
            verifySignature.update(data.getBytes());
            return verifySignature.verify(transaction.getSignature());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
