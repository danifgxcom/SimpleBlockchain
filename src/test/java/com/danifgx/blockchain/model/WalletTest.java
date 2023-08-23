package com.danifgx.blockchain.model;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;

import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private Wallet wallet;
    private PublicKey recipient;
    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        keyGen.initialize(256, random);
        keyPair = keyGen.generateKeyPair();
        wallet = new Wallet(keyPair.getPrivate(), keyPair.getPublic(), 100);
        recipient = keyGen.generateKeyPair().getPublic();
    }

    @Test
    void testCreateTransactionWithSufficientFunds() {
        double value = 50;
        Transaction transaction = wallet.createTransaction(recipient, value);
        assertNotNull(transaction);
        assertEquals(keyPair.getPublic(), transaction.getSender());
        assertEquals(recipient, transaction.getRecipient());
        assertEquals(value, transaction.getValue());
    }

    @Test
    void testCreateTransactionWithInsufficientFunds() {
        double value = 200; // Greater than wallet balance
        Transaction transaction = wallet.createTransaction(recipient, value);
        assertNull(transaction);
    }

    @Test
    void testDebitWithSufficientFunds() {
        double initialBalance = wallet.getBalance();
        double debitAmount = 50;
        wallet.debit(debitAmount);
        assertEquals(initialBalance - debitAmount, wallet.getBalance());
    }

    @Test
    void testDebitWithInsufficientFunds() {
        double initialBalance = wallet.getBalance();
        double debitAmount = 200; // Greater than wallet balance
        wallet.debit(debitAmount);
        assertEquals(initialBalance, wallet.getBalance()); // Balance should remain unchanged
    }

    @Test
    void testCredit() {
        double initialBalance = wallet.getBalance();
        double creditAmount = 50;
        wallet.credit(creditAmount);
        assertEquals(initialBalance + creditAmount, wallet.getBalance());
    }

    @Test
    void testCreateTransactionSuccess() {
        Transaction transaction = wallet.createTransaction(recipient, 50);
        assertNotNull(transaction);
        assertEquals(wallet.getPublicKey(), transaction.getSender());
        assertEquals(recipient, transaction.getRecipient());
        assertEquals(50, transaction.getValue());
    }

    @Test
    void testCreateTransactionInsufficientFunds() {
        Transaction transaction = wallet.createTransaction(recipient, 150);
        assertNull(transaction);
    }

    @Test
    void testDebitSuccess() {
        wallet.debit(50);
        assertEquals(50, wallet.getBalance());
    }

    @Test
    void testDebitInsufficientFunds() {
        wallet.debit(150);
        assertEquals(100, wallet.getBalance());
    }

    @Test
    void testPublicKey() {
        assertEquals(keyPair.getPublic(), wallet.getPublicKey());
    }

    @Test
    void testPrivateKey() {
        assertEquals(keyPair.getPrivate(), wallet.getPrivateKey());
    }
}
