package com.danifgx.blockchain.model;

import com.danifgx.blockchain.util.KeyGeneratorUtil;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class TransactionTest {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }
    @Test
    public void testEqualTransactions() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair.getPublic();
        PublicKey recipient = keyPair.getPublic();

        Transaction transaction1 = new Transaction(sender, recipient, 100.0);
        Transaction transaction2 = new Transaction(sender, recipient, 100.0);

        assertEquals(transaction1, transaction2);
    }

    @Test
    public void testDifferentSender() throws NoSuchAlgorithmException {
        KeyPair keyPair1 = KeyGeneratorUtil.generateKeyPair();
        KeyPair keyPair2 = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender1 = keyPair1.getPublic();
        PublicKey sender2 = keyPair2.getPublic();
        PublicKey recipient = KeyGeneratorUtil.generateKeyPair().getPublic();

        Transaction transaction1 = new Transaction(sender1, recipient, 100.0);
        Transaction transaction2 = new Transaction(sender2, recipient, 100.0);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testDifferentRecipient() throws NoSuchAlgorithmException {
        KeyPair keyPair1 = KeyGeneratorUtil.generateKeyPair();
        KeyPair keyPair2 = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair1.getPublic();
        PublicKey recipient1 = keyPair1.getPublic();
        PublicKey recipient2 = keyPair2.getPublic();

        Transaction transaction1 = new Transaction(sender, recipient1, 100.0);
        Transaction transaction2 = new Transaction(sender, recipient2, 100.0);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testDifferentValue() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair.getPublic();
        PublicKey recipient = keyPair.getPublic();

        Transaction transaction1 = new Transaction(sender, recipient, 100.0);
        Transaction transaction2 = new Transaction(sender, recipient, 99.0);

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testEqualWithSignature() {
        KeyPair keyPair1 = KeyGeneratorUtil.generateKeyPair();
        KeyPair keyPair2 = KeyGeneratorUtil.generateKeyPair();

        Transaction transaction1 = new Transaction(keyPair1.getPublic(), keyPair2.getPublic(), 100);
        Transaction transaction2 = new Transaction(keyPair1.getPublic(), keyPair2.getPublic(), 100);

        transaction1.signTransaction(keyPair1.getPrivate());
        transaction2.signTransaction(keyPair1.getPrivate());

        if (!transaction1.equals(transaction2)) {
            System.out.println("Sender: " + transaction1.getSender().equals(transaction2.getSender()));
            System.out.println("Recipient: " + transaction1.getRecipient().equals(transaction2.getRecipient()));
            System.out.println("Value: " + (transaction1.getValue() == transaction2.getValue()));
            System.out.println("Signature: " + Arrays.equals(transaction1.getSignature(), transaction2.getSignature()));
        }

        assertEquals(transaction1, transaction2);
    }

    @Test
    public void testDifferentSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair.getPublic();
        PublicKey recipient = keyPair.getPublic();

        Transaction transaction1 = new Transaction(sender, recipient, 100.0);
        Transaction transaction2 = new Transaction(sender, recipient, 100.0);

        transaction1.signTransaction(keyPair.getPrivate());
        transaction2.signTransaction(KeyGeneratorUtil.generateKeyPair().getPrivate());

        assertNotEquals(transaction1, transaction2);
    }

    @Test
    public void testEqualWithDifferentHash() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair.getPublic();
        PublicKey recipient = keyPair.getPublic();

        Transaction transaction1 = new Transaction(sender, recipient, 100.0);
        Transaction transaction2 = new Transaction(sender, recipient, 100.0);

        String hash1 = transaction1.calculateHash();
        String hash2 = transaction2.calculateHash();

        assertEquals(hash1, hash2);
        assertEquals(transaction1, transaction2);
    }

    @Test
    public void testSelfEquality() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair.getPublic();
        PublicKey recipient = keyPair.getPublic();

        Transaction transaction = new Transaction(sender, recipient, 100.0);

        assertEquals(transaction, transaction);
    }

    @Test
    public void testNotEqualToNull() throws NoSuchAlgorithmException {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();
        PublicKey sender = keyPair.getPublic();
        PublicKey recipient = keyPair.getPublic();

        Transaction transaction = new Transaction(sender, recipient, 100.0);

        assertNotEquals(transaction, null);
    }
}

