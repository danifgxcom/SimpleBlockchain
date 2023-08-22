package com.danifgx.blockchain.model;

import com.danifgx.blockchain.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
@Setter
public class Transaction implements Serializable {
    private PublicKey sender;
    private String recipient;
    private double value;
    private byte[] signature;

    public Transaction(PublicKey sender, String recipient, double value) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
    }

    public void signTransaction(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + recipient + Double.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + recipient + Double.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        recipient +
                        Double.toString(value)
        );
    }
}
