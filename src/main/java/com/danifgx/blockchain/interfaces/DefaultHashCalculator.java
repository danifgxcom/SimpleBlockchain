package com.danifgx.blockchain.interfaces;

import com.danifgx.blockchain.model.Transaction;
import com.danifgx.blockchain.util.StringUtil;

import java.util.List;

public class DefaultHashCalculator implements HashCalculator {
    @Override
    public String calculateHash(String previousHash, List<Transaction> transactions, int nonce) {
        String transactionsString = transactions.stream().map(Transaction::toString).reduce("", String::concat);
        return StringUtil.applySha256(previousHash + transactionsString + Integer.toString(nonce));
    }
}