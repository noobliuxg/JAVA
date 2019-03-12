package cn.com.java.encrypt.one_way;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class SHAUtils {

    public static String encrypt(String data) throws Exception{
        return encrypt(data,OneWayContants.UTF);
    }

    public static String encrypt(String data,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = OneWayContants.UTF;
        return new String(encrypt(data.getBytes(charset)),charset);
    }

    public static byte[] encrypt(byte[] data) throws Exception{
        MessageDigest sha = MessageDigest.getInstance(OneWayContants.KEY_SHA_ALGORITHM);
        sha.update(data);
        return Base64.encodeBase64(sha.digest());
    }
}
