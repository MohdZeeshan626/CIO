package com.singleevent.sdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import io.paperdb.Paper;

public class DataBaseStorage {

    private static final String TAG = DataBaseStorage.class.getSimpleName();

    public static String json_pass = "QEWPOFAJLVKNORHGOIAGAPEJAWEPNAPAS;Mjfpj3e9r309urw09jwfjszpt9r"; //.word
    public static String F_I_L_ENCP1 = "text.dat";

    public static String token_pass = json_pass;//.word
    public static String F_I_L_ENCP2 = "delta.dat";

    public static String encrypt(String input) {
        // This is base64 encoding, which is not an encryption
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }


    //encrypt files
    public static HashMap<String, byte[]> encryptBytes(byte[] plainTextBytes, String pawrdStr) {

        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();


            //Random salt for next step
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //Create initialization vector for AES
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(plainTextBytes);

            Paper.book().write("webmobi", secretKey);


            map.put("salt", salt);
            map.put("iv", iv);
            map.put("encrypted", encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }


    // decrypt files
    public static byte[] decryptData(HashMap<String, byte[]> map, String pawrdStr) {
        byte[] decrypted = null;
        try {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            SecretKey secretKey = Paper.book().read("webmobi");
            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            //keyspec
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            decrypted = cipher.doFinal(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;
    }


    public static HashMap<String, byte[]> encryptJsonData(byte[] plainTextBytes, String pawrdStr, Context context) {

        Paper.init(context);
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();


            //Random salt for next step
            SecureRandom random = new SecureRandom();
            byte salt[] = new byte[256];
            random.nextBytes(salt);

            //Create initialization vector for AES
            SecureRandom ivRandom = new SecureRandom(); //not caching previous seeded instance of SecureRandom
            byte[] iv = new byte[16];
            ivRandom.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            //Encrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            //keySpec
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            Paper.book().write("wwwwebmobicom", secretKey);

            byte[] encrypted = cipher.doFinal(plainTextBytes);

            map.put("salt", salt);
            map.put("iv", iv);
            // map.put("encrypted", encryptedString.getBytes());
            map.put("encrypted", encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;


    }

    public static byte[] decryptJsonData(HashMap<String, byte[]> map, String pawrdStr, Context context) {
        Paper.init(context);
        byte[] decrypted = null;
        try {
            byte salt[] = map.get("salt");
            byte iv[] = map.get("iv");
            byte encrypted[] = map.get("encrypted");

            //Decrypt
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKey secretKey = Paper.book().read("wwwwebmobicom");
            //keyspec
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            decrypted = cipher.doFinal(encrypted);
            // decrypted = outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return decrypted;


    }

    public static boolean isInternetConnectivity(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    private static boolean canExecuteSuCommand() {
        int i;
        try {
            Runtime.getRuntime().exec("su");
            i = 1;
        } catch (IOException localIOException) {
            i = 0;

        }
        if (i == 0) {
            return false;
        } else return true;
    }

    private static boolean hasSuperuserApk() {
        return new File("/system/app/Superuser.apk").exists();
    }

    private static boolean isTestKeyBuild() {
        String str = Build.TAGS;
        if ((str != null) && (str.contains("test-keys"))) {
            return true;
        } else return false;


    }

    public static boolean isRootedPhone() {

        if ((!isTestKeyBuild()) && (!hasSuperuserApk()) && (!canExecuteSuCommand())) {
            return false;
        } else return true;
    }

    public static String getAppVersionName(Context context) {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

   /* public static String getToken(Context context){


        Runnable r = new Runnable() {
            @Override
            public void run() {
                // your code here

                try {
                    FileInputStream fis = context.openFileInput(DataBaseStorage.F_I_L_ENCP2);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    byte[] decrypted = DataBaseStorage.decryptData((HashMap<String, byte[]>) ois.readObject(),
                            DataBaseStorage.token_pass);
                    if (decrypted != null) {
                        //decryptedString[0] = new String(decrypted);
                        token = new String(decrypted);
                    }
                    ois.close();

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        };

        Thread t = new Thread(r);
        t.start();

        return token;
    }
*/
}
