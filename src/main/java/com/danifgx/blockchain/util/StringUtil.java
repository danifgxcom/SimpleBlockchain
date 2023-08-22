package com.danifgx.blockchain.util;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class StringUtil {

    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PublicKey getKeyFromString(String keyString) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(keyString);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("ECDSA");

            return kf.generatePublic(X509publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey getPrivateKeyFromString(String keyString) {
        try {
            byte[] byteKey = Base64.getDecoder().decode(keyString);
            PKCS8EncodedKeySpec PKCS8privateKey = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("ECDSA");

            return kf.generatePrivate(PKCS8privateKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature signature;
        byte[] output = new byte[0];
        try {
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privateKey);
            byte[] strByte = input.getBytes();
            signature.update(strByte);
            byte[] realSig = signature.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
