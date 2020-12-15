package com.jzz.learn;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

/**
 * @author lazycece
 * @date 2019/9/25
 */
public class ApiAuthUtils {
    public static final String AUTH_TOKEN = "AUTH-TOKEN";
    public static final String SECRET_KEY = "75HVYG0VQVDEYPLLODZUX99ZCV333EKY";
    public static String sign(Map<String, String> params) throws Exception {
        return SignUtils.generateSignature(params, SECRET_KEY, SignUtils.SignType.MD5, "sign");
    }

    public static String encode(String salt, String jsonData) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        return DigestUtils.encodeBase64(
                Des3Utils.des3EncodeECB(
                        DigestUtils.md5(salt + SECRET_KEY),
                        jsonData));
    }

    public static String decode(String salt, String encodeData) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        byte[] bytes = Des3Utils.des3DecodeECB(
                DigestUtils.md5(salt + SECRET_KEY),
                DigestUtils.decodeBase64(encodeData.getBytes("utf-8")));
        return new String(bytes);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        System.out.println(decode("872425","nqGdPdrMWxeuWygfXQ4NHpw1NGLIt7AyCMpWybWD6EIHSlAxJbhPfbuOoZmW/kmpMtVU/1+GAH5+NWMRhzbrD5i0KkM/q4udfZbgOf9IQDfJYj12uWoq/6CJj/0a0TRgmMXKKAUc0gGsy8Wi39x8pYYj9hSMAjPG2hlPwkULo7i0KbND+KHi9VcP2kLMy3omOFR4q6lHJcpGgSSuiLowRPuFMbz0EOxka6nMx8g1QRY="));}
}




