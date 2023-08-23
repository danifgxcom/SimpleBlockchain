package com.danifgx.blockchain.model;

import com.danifgx.blockchain.interfaces.HashCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainTest {

    private Blockchain blockchain;

    @BeforeEach
    void setUp() {
        blockchain = new Blockchain(new ArrayList<>());
    }

    @Test
    void testGenesisBlockCreation() {
        Blockchain blockchain = new Blockchain(null);
        List<Block> chain = blockchain.getChain();

        assertNotNull(chain);
        assertEquals(1, chain.size());
        assertEquals("0", chain.get(0).getPreviousHash());
    }


    @Test
    void testAddValidBlock() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair keyPairAlice = keyGen.genKeyPair();
        PublicKey alice = keyPairAlice.getPublic();

        KeyPair keyPairBob = keyGen.genKeyPair();
        PublicKey bob = keyPairBob.getPublic();

        Transaction transaction = new Transaction(alice, bob, 50);
        List<Transaction> transactions = Arrays.asList(transaction);

        blockchain.addBlock(transactions);

        assertEquals(2, blockchain.getChain().size());
        assertEquals(transactions, blockchain.getLastBlock().getTransactions());
    }

    @Test
    void testAddTransaction() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair keyPairAlice = keyGen.genKeyPair();
        PublicKey alice = keyPairAlice.getPublic();

        KeyPair keyPairBob = keyGen.genKeyPair();
        PublicKey bob = keyPairBob.getPublic();

        Transaction transaction = new Transaction(alice, bob, 50);

        boolean result = blockchain.addTransaction(transaction);

        assertTrue(result);
        assertEquals(transaction, blockchain.getCurrentBlock().getTransactions().get(0));
    }

    @Test
    void testAddTransaction_NullTransaction() {
        assertFalse(blockchain.addTransaction(null));
    }

    @Test
    void testIsValid_EmptyBlockchain() {
        assertTrue(blockchain.isValid());
    }

    @Test
    void testIsValid_InvalidBlockchain() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair aliceKeyPair = keyGen.genKeyPair();
        KeyPair bobKeyPair = keyGen.genKeyPair();

        PublicKey alice = aliceKeyPair.getPublic();
        PublicKey bob = bobKeyPair.getPublic();

        Transaction transaction = new Transaction(alice, bob, 50);
        List<Transaction> transactions = Collections.singletonList(transaction);

        HashCalculator corruptingHashCalculator = (prevHash, trans, n) -> "corrupted hash";
        Block block = Block.builder()
                .previousHash("prev")
                .transactions(transactions)
                .hashCalculator(corruptingHashCalculator)
                .build();

        blockchain.addBlock(transactions);

        // Directly manipulate the blockchain to add the corrupt block
        blockchain.getChain().add(block); // Assuming you have a method to access the chain directly

        assertFalse(blockchain.isValid());
    }


    @Test
    void testLoadBlockchain_NonExistingFile() {
        List<Block> loadedBlockchain = blockchain.loadBlockchain("nonexistentfile.blockchain");
        assertTrue(loadedBlockchain.isEmpty());
    }

    @Test
    void testSaveAndLoad_EmptyBlockchain() {
        String filename = "test_empty.blockchain";
        blockchain.saveBlockchain(blockchain.getChain(), filename);

        List<Block> loadedBlockchain = blockchain.loadBlockchain(filename);
        assertEquals(1, loadedBlockchain.size()); // Genesis block
        assertEquals("0", loadedBlockchain.get(0).getPreviousHash());
        assertTrue(new File(filename).delete());
    }

    @Test
    void testSaveAndLoad_NonEmptyBlockchain() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair keyPairAlice = keyGen.genKeyPair();
        PublicKey alice = keyPairAlice.getPublic();
        KeyPair keyPairBob = keyGen.genKeyPair();
        PublicKey bob = keyPairBob.getPublic();

        Transaction transaction = new Transaction(alice, bob, 50);
        List<Transaction> transactions = Collections.singletonList(transaction);

        blockchain.addBlock(transactions);

        String filename = "test_non_empty.blockchain";
        blockchain.saveBlockchain(blockchain.getChain(), filename);

        List<Block> loadedBlockchain = blockchain.loadBlockchain(filename);
        assertEquals(2, loadedBlockchain.size());
        assertEquals(transactions, loadedBlockchain.get(1).getTransactions());
        assertTrue(new File(filename).delete());
    }

    @Test
    void testSaveBlockchain_NullBlockchain() {
        assertDoesNotThrow(() -> blockchain.saveBlockchain(null, "nulltest.blockchain"));
    }



}
