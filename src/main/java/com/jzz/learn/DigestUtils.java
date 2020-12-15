package com.jzz.learn;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class DigestUtils {
    public final static String ENCODING = "UTF-8";

    public final static String DIGEST_KEY = "AVuu7SWwv99YCbb11IHlk5ONlq77YXba3HLlp57RRvu99Bbe13HHkk55RQuu99i3";


    public static String decodeBase64(String data) throws UnsupportedEncodingException {
        byte[] _date = data.getBytes();
        /*try {
            return new String(Base64.decodeBase64(_date), ENCODING);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        return new String(Base64.decodeBase64(_date), ENCODING);
    }

    public static String decodeBase64(String data, String encoding) throws UnsupportedEncodingException {
        byte[] _date = data.getBytes();
        /*try {
            return new String(Base64.decodeBase64(_date), encoding);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        return new String(Base64.decodeBase64(_date), encoding);
    }

    public static String encodeBase64(String data) throws UnsupportedEncodingException {
        byte[] _date = data.getBytes();
        /*try {
            return new String(Base64.encodeBase64(_date), ENCODING);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        return new String(Base64.encodeBase64(_date), ENCODING);
    }

    public static byte[] encodeBase64ToByte(byte[] data) {
        /*try {
            return Base64.encodeBase64(data);
        } catch (Exception e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        return Base64.encodeBase64(data);
    }

    public static String encodeBase64(byte[] date) throws UnsupportedEncodingException {
        /*try {
            return new String(Base64.encodeBase64(date), ENCODING);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
            throw e;
        }*/
        return new String(Base64.encodeBase64(date), ENCODING);
    }

    public static byte[] decodeBase64(byte[] date) {
        return Base64.decodeBase64(date);
    }


    public static String encodeBase64(String data, String encoding) throws UnsupportedEncodingException {
        byte[] _date = data.getBytes();
        return new String(Base64.encodeBase64(_date), encoding);
        /*try {
            return new String(Base64.encodeBase64(_date), encoding);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
    }

    public static String encodeBase64(String data, String byteEncoding, String encoding) throws UnsupportedEncodingException {
        /*try {
            byte[] _date = data.getBytes(byteEncoding);
            return new String(Base64.encodeBase64(_date), encoding);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        byte[] _date = data.getBytes(byteEncoding);
        return new String(Base64.encodeBase64(_date), encoding);
    }

    public static String md5(String data) {
        String _date = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        return _date;
    }

    public static String md5(byte[] data) {
        String _date = org.apache.commons.codec.digest.DigestUtils.md5Hex(data);
        return _date;
    }

    public static String md5(InputStream data) {
        String _date = null;
        _date = org.apache.commons.codec.digest.DigestUtils.md5Hex(String.valueOf(data));
        return _date;
    }


    public static String sha1(String data) {
        return org.apache.commons.codec.digest.DigestUtils.shaHex(data);
    }


    public static String urlEncode(String data, String encoding) throws UnsupportedEncodingException {
        /*try {
            return URLEncoder.encode(data, encoding);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        return URLEncoder.encode(data, encoding);
    }

    public static String urlDecode(String data, String encoding) throws UnsupportedEncodingException {
        /*try {
            return URLDecoder.decode(data, encoding);
        } catch (UnsupportedEncodingException e) {
            //throw new CryptException("encoding unknow", e);
        }*/
        return URLDecoder.decode(data, encoding);
    }
}

