package com.jzz.learn;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author lazycece
 * 2018.03.28
 */

public class SignUtils {

    public enum SignType {
        MD5, HMAC_SHA_256
    }

    public static String generateSignature(final Map<String, String> data, String key, SignType signType, String fieldSign) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(fieldSign)) {
                continue;
            }
            String value = data.get(k);
            if (value != null && value.trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return md5(sb.toString()).toUpperCase();
        } else if (SignType.HMAC_SHA_256.equals(signType)) {
            return hmacSha256(sb.toString(), key).toUpperCase();
        } else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }


    private static String md5(String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    private static String hmacSha256(String data, String key) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] array = sha256Hmac.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
}

