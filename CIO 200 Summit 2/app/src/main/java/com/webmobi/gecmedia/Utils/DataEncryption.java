package com.webmobi.gecmedia.Utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Lenovo on 10-07-2018.
 */

public class DataEncryption {
    private static final String TAG = DataEncryption.class.getSimpleName();

    public static String events_logo_pass_enc = "AlleventsforyouDDDJJDJDDJDJSSISH"; //pass
    public static String EVENT_ENCP = "event.dat";

    public static String user_pas_code_enc = "WWWWWWHHHHHUUHNHSONUBD";//pass
    public static String F_I_L_ENCP = "delta.dat";

    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }


    //encrypt files
    public static HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes, String pass ) {
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try
        {
            //Random salt for next step
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //PBKDF2 - derive the key from the password, don't use passwords directly
            char[] passwordChar = pass.toCharArray(); //Turn password into char[] array
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 111324, 256); //1324 iterations
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Create initialization vector for AES
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            // NOTE: workaround. too bit bytes doesn't writted correctly.
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

            byte[] bytes = plainTextBytes;
            int oneBulkSize = 1024;// temp value for proof of concept. might be bigger one.
            int numOfBulk = (bytes.length / oneBulkSize);

            for (int i = 0; i < numOfBulk; i++) {
                cipherOutputStream.write(bytes, oneBulkSize * i, oneBulkSize);
            }

            if ((bytes.length % oneBulkSize) != 0) {
                cipherOutputStream.write(bytes, oneBulkSize * numOfBulk, bytes.length % oneBulkSize);
            }
            cipherOutputStream.close();
            String encryptedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            // encryptedStr = Base64.encodeToString(outputStream.toByteArray(),Base64.DEFAULT);
            map.put("salt", salt);
            map.put("iv", iv);
            map.put("encrypted", encryptedString.getBytes());
        }
        catch(Exception e)
        {
            Log.e("MYAPP", "encryption exception", e);
        }

        return map;
    }



    // decrypt files
    public static byte[] decryptData(HashMap<String, byte[]> map, String passwordString) {
        byte[] decrypted = null;
        try
        {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            //regenerate key from password
            char[] passwordChar = passwordString.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 111324, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(encrypted, Base64.DEFAULT));
            CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int b;
            while ((b = cipherInputStream.read()) != -1) {
                outputStream.write(b);
            }
            outputStream.close();

            // String decryptedString = outputStream.toString("UTF-8");
            // decrypted = cipher.doFinal(encrypted);
            decrypted = outputStream.toByteArray();
        }catch (IOException e){
            Log.e(TAG,"Exception IN input-output");
        }
        catch(Exception e)
        {
            Log.e("MYAPP", "decryption exception", e);
        }

        return decrypted;
    }



    public static HashMap<String, byte[]> encryptUserDetailsBytes( byte[] plainTextBytes, String pass ) {
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try
        {
            //Random salt for next step
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //PBKDF2 - derive the key from the password, don't use passwords directly
            char[] passwordChar = pass.toCharArray(); //Turn password into char[] array
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 111324, 256); //1324 iterations
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Create initialization vector for AES
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            // NOTE: workaround. too bit bytes doesn't writted correctly.

        /*
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

            byte[] bytes = plainTextBytes;
            int oneBulkSize = 1024;// temp value for proof of concept. might be bigger one.
            int numOfBulk = (bytes.length / oneBulkSize);

            for (int i = 0; i < numOfBulk; i++) {
                cipherOutputStream.write(bytes, oneBulkSize * i, oneBulkSize);
            }

            if ((bytes.length % oneBulkSize) != 0) {
                cipherOutputStream.write(bytes, oneBulkSize * numOfBulk, bytes.length % oneBulkSize);
            }
            cipherOutputStream.close();
            String encryptedString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        */

            map.put("salt", salt);
            map.put("iv", iv);
            map.put("encrypted", encrypted);
        }
        catch(Exception e)
        {
            Log.e("MYAPP", "encryption exception", e);
        }

        return map;
    }

    public static byte[] decryptUserDetailsData(HashMap<String, byte[]> map, String passwordString) {
        byte[] decrypted = null;
        try
        {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            //regenerate key from password
            char[] passwordChar = passwordString.toCharArray();
            PBEKeySpec pbKeySpec = new PBEKeySpec(passwordChar, salt, 111324, 256);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = secretKeyFactory.generateSecret(pbKeySpec).getEncoded();
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            decrypted = cipher.doFinal(encrypted);

            //for long string
        /*
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(encrypted, Base64.DEFAULT));
            CipherInputStream cipherInputStream = new CipherInputStream(byteArrayInputStream, cipher);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int b;
            while ((b = cipherInputStream.read()) != -1) {
                outputStream.write(b);
            }
            outputStream.close();
            decrypted = outputStream.toByteArray();
            // String decryptedString = outputStream.toString("UTF-8");
        */

        }
        catch(Exception e)
        {
            Log.e("MYAPP", "decryption exception", e);
        }

        return decrypted;
    }

}
