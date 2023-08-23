package com.danifgx.blockchain.model;

import java.security.PublicKey;

public class Miner {
    private final PublicKey minerId;
    private final Wallet wallet;
    private static final double MINING_REWARD = 50;

    public Miner(PublicKey minerId, Wallet wallet) {
        if (minerId == null || wallet == null) {
            throw new IllegalArgumentException("MinerId y Wallet no pueden ser nulos");
        }
        this.minerId = minerId;
        this.wallet = wallet;
    }

    public void mine(Block block, int difficulty) {
        if (block == null) {
            throw new IllegalArgumentException("El bloque no puede ser nulo");
        }

        performMining(block, difficulty);
        rewardMiner(block);
    }

    private void performMining(Block block, int difficulty) {
        block.mine(difficulty);
    }

    private void rewardMiner(Block block) {
        Transaction reward = new Transaction(null, minerId, MINING_REWARD);
        block.addTransaction(reward);
    }

    public PublicKey getMinerId() {
        return minerId;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
