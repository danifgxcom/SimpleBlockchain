package com.danifgx.blockchain.util;

import org.junit.jupiter.api.Test;

import java.security.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilTest {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @Test
    void testApplySha256() {
        String input = "test";
        String hash = StringUtil.applySha256(input);
        assertEquals(64, hash.length());
    }

    @Test
    void testGetStringFromKeyAndGetKeyFromString() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
        keyGen.initialize(256);
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();


        String keyString = StringUtil.getStringFromKey(publicKey);
        PublicKey retrievedKey = StringUtil.getKeyFromString(keyString);

        assertEquals(publicKey, retrievedKey);
    }

    @Test
    void testGetPrivateKeyFromString() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
        keyGen.initialize(256); // or another valid key size
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        String keyString = StringUtil.getStringFromKey(privateKey);
        PrivateKey retrievedKey = StringUtil.getPrivateKeyFromString(keyString);

        assertEquals(privateKey, retrievedKey);
    }

    @Test
    void testApplyECDSASigAndVerifyECDSASig() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
        keyGen.initialize(256); // or another valid key size
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String input = "test";
        byte[] signature = StringUtil.applyECDSASig(privateKey, input);

        assertTrue(StringUtil.verifyECDSASig(publicKey, input, signature));
    }
}
