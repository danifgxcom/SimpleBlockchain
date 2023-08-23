package com.danifgx.blockchain.util;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyGeneratorUtilTest {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @Test
    void generateKeyPairShouldReturnValidKeyPair() {
        KeyPair keyPair = KeyGeneratorUtil.generateKeyPair();

        assertNotNull(keyPair, "KeyPair should not be null");
        assertValidKeyPair(keyPair);
    }

    @Test
    void generateKeyPairShouldReturnUniqueKeyPairs() {
        KeyPair keyPair1 = KeyGeneratorUtil.generateKeyPair();
        KeyPair keyPair2 = KeyGeneratorUtil.generateKeyPair();

        assertValidKeyPair(keyPair1);
        assertValidKeyPair(keyPair2);
        assertTrue(!keyPair1.getPrivate().equals(keyPair2.getPrivate()), "Private keys should be unique");
        assertTrue(!keyPair1.getPublic().equals(keyPair2.getPublic()), "Public keys should be unique");
    }

    private void assertValidKeyPair(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(privateKey, "Private key should not be null");
        assertNotNull(publicKey, "Public key should not be null");
        assertTrue(privateKey.getAlgorithm().equals("ECDSA"), "Private key algorithm should be ECDSA");
        assertTrue(publicKey.getAlgorithm().equals("ECDSA"), "Public key algorithm should be ECDSA");
    }
}
