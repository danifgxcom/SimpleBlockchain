package com.danifgx.blockchain.validator;

import com.danifgx.blockchain.model.Block;

import java.util.List;

public class BlockchainValidator {

    public static boolean isValid(List<Block> chain) {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash()) ||
                    !currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }
        }
        return true;
    }
}
