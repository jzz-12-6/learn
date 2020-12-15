package com.jzz.learn;



import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Des3Utils {
    private final static String encoding = "utf-8";

    /**
     * ECB加密,不要IV
     *
     * @param _key  密钥
     * @param _data 明文
     * @return Base64编码的密文
     */
    public static byte[] des3EncodeECB(String _key, String _data) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        try {
            byte[] key = _key.getBytes(encoding);
            byte[] data = _data.getBytes(encoding);
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            return cipher.doFinal(data);
        } catch (Exception ex) {
            //throw new CryptException(ex);
            throw ex;
        }
    }

    /**
     * ECB解密,不要IV
     *
     * @param _key 密钥
     * @param data Base64编码的密文
     * @return 明文
     */
    public static byte[] des3DecodeECB(String _key, byte[] data) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        try {
            byte[] key = _key.getBytes(encoding);
            Key desKey;
            DESedeKeySpec spec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
            desKey = keyFactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            return cipher.doFinal(data);
        } catch (Exception ex) {
            //throw new CryptException(ex);
            throw ex;
        }

    }

}

