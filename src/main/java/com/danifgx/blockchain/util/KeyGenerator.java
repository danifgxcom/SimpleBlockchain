package com.danifgx.blockchain.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            keyGen.initialize(256, random);

            KeyPair pair = keyGen.generateKeyPair();
            PublicKey publicKey = pair.getPublic();
            PrivateKey privateKey = pair.getPrivate();

            String publicKeyAsString = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            String privateKeyAsString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            System.out.println("Clave pública como cadena: " + publicKeyAsString);
            System.out.println("Clave privada como cadena: " + privateKeyAsString);

        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }
}
