package com.kltn.minioservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;


public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);
    private static final int SALT_LENGTH = 8;
    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = new byte[]{'T', 'h', 'e', 'B', 'e', 's', 't', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};


    /**
     * Check valid file
     * @param fileName
     * @sparam validExt - extentions that are allow to upload
     * @return
     */

    public static boolean checkFileTypeValid(String fileName, String []validExt) {
        return checkFileExtensionValid(fileName, validExt);
    }

    public static boolean checkFileSizeValid(byte[] file, Integer maxFileSizeMb) {
        if (Objects.isNull(maxFileSizeMb)) {
            maxFileSizeMb = 15;
        }
        Objects.requireNonNull(file);
        return file.length <= maxFileSizeMb;
    }

    /**
     * Check valid extension file
     * @param fileName
     * @return
     */
    public static boolean checkFileExtensionValid(String fileName, String []includeExt) {
        Objects.requireNonNull(fileName);
        for (String fileExtension : includeExt) {
            if (fileName.toLowerCase().endsWith(fileExtension.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public static String getRandomSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(salt);
    }

    /**
     *
     * @param valueToEnc
     * @return
     * @throws Exception
     */
    public static String encrypt(String valueToEnc) throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        byte[] encryptedByteValue = Base64.getUrlEncoder().withoutPadding().encode(encValue);
        return new String(encryptedByteValue);
    }

    /**
     *
     * @param encryptedValue
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptedValue) throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getUrlDecoder().decode(encryptedValue);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }
}