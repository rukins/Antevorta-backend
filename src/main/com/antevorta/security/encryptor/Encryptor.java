package com.antevorta.security.encryptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Encryptor {
    private final String algorithm;
    private final Key key;
    private final IvParameterSpec iv;

    public Encryptor(@Value("${encryption.secretKey}") String secretKey, @Value("${encryption.initializationVector}") String iv) {
        this.algorithm = "AES/CBC/PKCS5Padding";

        this.key = getSecretKeySpec(secretKey);
        this.iv = getIvParameterSpec(iv);
    }

    public String encrypt(String raw)  {
        if (raw == null) {
            return null;
        }

        Cipher cipher;
        byte[] cipherText;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            cipherText = cipher.doFinal(raw.getBytes());
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                 IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public String decrypt(String encrypted) {
        if (encrypted == null) {
            return null;
        }

        Cipher cipher;
        byte[] plainText;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            plainText = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException |
                 IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        return new String(plainText);
    }

    public boolean matches(String raw, String encrypted) {
        return raw.equals(decrypt(encrypted));
    }

    private SecretKeySpec getSecretKeySpec(String secretKey) {
        return new SecretKeySpec(getBytes(secretKey), "AES");
    }

    private IvParameterSpec getIvParameterSpec(String iv) {
        return new IvParameterSpec(getBytes(iv));
    }

    private byte[] getBytes(String str) {
        if (str.length() == 16) {
            return str.getBytes();
        }

        byte[] bytes = new byte[16];
        byte[] strBytes = str.getBytes();

        if (strBytes.length < 16) {
            System.arraycopy(strBytes, 0, bytes, 0, strBytes.length);
        } else {
            System.arraycopy(strBytes, 0, bytes, 0, bytes.length);
        }

        return bytes;
    }
}
