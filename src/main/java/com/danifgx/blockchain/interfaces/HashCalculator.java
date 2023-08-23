package com.danifgx.blockchain.interfaces;

import com.danifgx.blockchain.model.Transaction;

import java.util.List;

public interface HashCalculator {
    String calculateHash(String previousHash, List<Transaction> transactions, int nonce);
}
