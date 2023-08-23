package com.danifgx.blockchain.model;

import com.danifgx.blockchain.interfaces.DefaultHashCalculator;
import com.danifgx.blockchain.util.StringUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    private Block block;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair keyPairSender = keyGen.generateKeyPair();
        KeyPair keyPairRecipient = keyGen.generateKeyPair();

        PublicKey sender = keyPairSender.getPublic();
        PublicKey recipient = keyPairRecipient.getPublic();
        double value = 50.0;

        transaction1 = new Transaction(sender, recipient, value);
        transaction2 = new Transaction(sender, recipient, value);

        block = Block.builder()
                .previousHash("previousHash")
                .transactions(Arrays.asList(transaction1, transaction2))
                .hashCalculator(new DefaultHashCalculator())
                .build();

    }

    @Test
    void testCalculateHash() {
        String transactionsString = block.getTransactions()
                .stream()
                .map(Transaction::toString)
                .reduce("", String::concat);

        String expectedHash = StringUtil.applySha256(block.getPreviousHash() + transactionsString + Integer.toString(block.getNonce()));
        assertEquals(expectedHash, block.calculateHash());
    }

    @Test
    void testMine() {
        int difficulty = 3;
        block.mine(difficulty);
        String hash = block.getHash();
        assertTrue(hash.startsWith(new String(new char[difficulty]).replace('\0', '0')));
    }

    @Test
    void testAddTransaction() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair keyPairSender = keyGen.generateKeyPair();
        KeyPair keyPairRecipient = keyGen.generateKeyPair();

        PublicKey sender = keyPairSender.getPublic();
        PublicKey recipient = keyPairRecipient.getPublic();
        double value = 25.0;

        Transaction newTransaction = new Transaction(sender, recipient, value);
        block.addTransaction(newTransaction);
        assertTrue(block.getTransactions().contains(newTransaction));
    }

    @Test
    void testConstructorWithNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> {
            Block.builder()
                    .previousHash(null)
                    .transactions(Arrays.asList(transaction1, transaction2))
                    .build();
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Block.builder()
                    .previousHash("previousHash")
                    .transactions(null)
                    .build();
        });
    }
}
