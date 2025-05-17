package com.avis.vehicle_reservation_consumer.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtil {

//    public static String encrypt(String plainText) throws Exception {
//    byte[] keyBytes = Base64.getDecoder().decode(BASE64_KEY); // Decode Base64 key
//    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
//
//    Cipher cipher = Cipher.getInstance("AES"); // Defaults to AES/ECB/PKCS5Padding
//    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//
//    byte[] encrypted = cipher.doFinal(plainText.getBytes());
//    return Base64.getEncoder().encodeToString(encrypted); // Return Base64-encoded ciphertext
//    }

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
