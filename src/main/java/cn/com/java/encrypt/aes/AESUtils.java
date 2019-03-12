package cn.com.java.encrypt.aes;

import cn.com.java.encrypt.desc.DESCUtils;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

public class AESUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(DESCUtils.class);
    /**
     * 设定加密过程中指定的编码格式
     */
    public static String DEFAULT_ENCRYPT_CHARSETNAME = "UTF-8";

    /**
     * 默认的加密算法
     */
    private static String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 指定私钥加密的算法为：DES
     */
    private static String DEFAULT_KEY_ALGORITHM = "AES";

    /**
     * 加密
     * @param content 加密内容
     * @param key 秘钥
     * @return
     */
    public static String encrypt(String content,String key){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("加密的内容：{}\n 参与加密的秘钥：{}",content,key);
        }
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,getSecretKey(key));
            byte[] bytes = cipher.doFinal(content.getBytes(DEFAULT_ENCRYPT_CHARSETNAME));
            return Base64.encodeBase64String(bytes);
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return null;
    }

    /**
     * 解密
     * @param content  解密内容
     * @param key 秘钥
     * @return
     */
    public static String decrypt(String content,String key){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("解密的内容：{}\n 参与解密的秘钥：{}",content,key);
        }
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE,getSecretKey(key));
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(content.getBytes(DEFAULT_ENCRYPT_CHARSETNAME)));
            return new String(bytes,DEFAULT_ENCRYPT_CHARSETNAME);
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return null;
    }

    private static SecretKeySpec getSecretKey(String key) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(DEFAULT_KEY_ALGORITHM);
            generator.init(128,new SecureRandom(key.getBytes(DEFAULT_ENCRYPT_CHARSETNAME)));
            SecretKey secretKey = generator.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(),DEFAULT_KEY_ALGORITHM);
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return null;
    }

}
