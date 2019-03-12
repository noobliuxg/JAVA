package cn.com.java.encrypt.one_way;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACUtils {

    /**
     * 初始化HMAC秘钥
     * @return
     * @throws Exception
     */
    public static SecretKeySpec initKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance(OneWayContants.KEY_HMAC_ALGORITHM);
        SecretKey secretKey = generator.generateKey();
        SecretKeySpec spec = new SecretKeySpec(secretKey.getEncoded(),OneWayContants.KEY_HMAC_ALGORITHM);
        return spec;
    }

    /**
     * HMAC加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data) throws Exception{
        Mac mac = Mac.getInstance(OneWayContants.KEY_HMAC_ALGORITHM);
        mac.init(initKey());
        byte[] bytes = mac.doFinal(data);
        return bytes;
    }

    /**
     * HMAC加密，结果(Base64加密过)
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) throws Exception{
        return Base64.encodeBase64String(encrypt(content.getBytes()));
    }
}
