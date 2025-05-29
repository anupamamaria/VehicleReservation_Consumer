package com.avis.vehicle_reservation_consumer.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {
    //to decrypt the encrypted payload

    public static SecretKey getSharedSecretKey() {
        String base64Key = "sBvE9h1C+Jf9eWzL3EzTxg==";
        return new SecretKeySpec(Base64.getDecoder().decode(base64Key), "AES");
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKey secretKey = getSharedSecretKey();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes);
    }
}
