package com.danifgx.blockchain.model;

import com.danifgx.blockchain.util.StringUtil;
import lombok.Getter;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
public class Transaction implements Serializable {
    private final PublicKey sender;
    private final PublicKey recipient;
    private final double value;
    private byte[] signature;

    public Transaction(PublicKey sender, PublicKey recipient, double value) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
    }

    public void signTransaction(PrivateKey privateKey) {
        signature = sign(privateKey, getSignatureData());
    }

    public boolean verifySignature() {
        return verify(sender, getSignatureData(), signature);
    }

    public String calculateHash() {
        return StringUtil.applySha256(getSignatureData());
    }

    private String getSignatureData() {
        return StringUtil.getStringFromKey(sender) + recipient + Double.toString(value);
    }

    private byte[] sign(PrivateKey privateKey, String data) {
        return StringUtil.applyECDSASig(privateKey, data);
    }

    private boolean verify(PublicKey publicKey, String data, byte[] signature) {
        return StringUtil.verifyECDSASig(publicKey, data, signature);
    }
}
