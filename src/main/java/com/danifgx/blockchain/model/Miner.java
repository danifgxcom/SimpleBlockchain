package com.danifgx.blockchain.model;

public class Miner {
    private String minerId;
    private Wallet wallet;
    private static final double MINING_REWARD = 50;

    public Miner(String minerId, Wallet wallet) {
        this.minerId = minerId;
        this.wallet = wallet;
    }

    public void mine(Block block, int difficulty) {
        block.mine(difficulty);

        Transaction reward = wallet.createTransaction(null, MINING_REWARD);
        block.addTransaction(reward);
    }

    public String getMinerId() {
        return minerId;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
