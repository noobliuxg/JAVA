package cn.com.java.encrypt.one_way;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class MD5Utils {

    /**
     * MD5加密,结果使用(Base64加密)
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception{
        return new String(encrypt(data.getBytes(OneWayContants.UTF)),OneWayContants.UTF);
    }

    /**
     * MD5加密,结果使用(Base64加密)
     * @param data 待加密数据
     * @param charset 数据编码格式
     * @return
     * @throws Exception
     */
    public static String encrypt(String data,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = OneWayContants.UTF;
        return new String(encrypt(data.getBytes(charset)),charset);
    }

    /**
     * MD5加密,结果使用(Base64加密)
     * @param data 待加密数据
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance(OneWayContants.KEY_MD5_ALGORITHM);
        md5.update(data);
        byte[] digest = md5.digest();
        return Base64.encodeBase64(digest);
    }
}
