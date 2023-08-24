package com.danifgx.blockchain.model;

import org.junit.jupiter.api.Test;

import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MinerTest {

    @Test
    void constructor_NullMinerId_ThrowsException() {
        PublicKey minerId = null;
        Wallet wallet = mock(Wallet.class);

        assertThrows(IllegalArgumentException.class, () -> new Miner(minerId, wallet));
    }

    @Test
    void constructor_NullWallet_ThrowsException() {
        PublicKey minerId = mock(PublicKey.class);
        Wallet wallet = null;

        assertThrows(IllegalArgumentException.class, () -> new Miner(minerId, wallet));
    }

    @Test
    void constructor_ValidParameters_CreatesMiner() {
        PublicKey minerId = mock(PublicKey.class);
        Wallet wallet = mock(Wallet.class);

        Miner miner = new Miner(minerId, wallet);

        assertEquals(minerId, miner.getMinerId());
        assertEquals(wallet, miner.getWallet());
    }

    @Test
    void mine_NullBlock_ThrowsException() {
        Miner miner = createMiner();
        int difficulty = 4;

        assertThrows(IllegalArgumentException.class, () -> miner.mine(null, difficulty));
    }

    @Test
    void mine_ValidBlock_PerformsMiningAndRewardsMiner() {
        Miner miner = createMiner();
        Block block = mock(Block.class);
        int difficulty = 4;

        miner.mine(block, difficulty);

        verify(block).mine(difficulty);
        verify(block).addTransaction(any(Transaction.class));
    }

    private Miner createMiner() {
        PublicKey minerId = mock(PublicKey.class);
        Wallet wallet = mock(Wallet.class);
        return new Miner(minerId, wallet);
    }
}
