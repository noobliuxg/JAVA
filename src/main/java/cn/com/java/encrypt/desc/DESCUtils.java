package cn.com.java.encrypt.desc;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
public class DESCUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DESCUtils.class);
    /**
     * 设定加密过程中指定的编码格式
     */
    public static String DEFAULT_ENCRYPT_CHARSETNAME = "UTF-8";

    /**
     * 默认的加密算法
     */
    private static String DEFAULT_CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    /**
     * 指定私钥加密的算法为：DES
     */
    private static String DEFAULT_KEY_ALGORITHM = "DES";

    /**
     * 加密
     * @param content 加密内容
     * @param key 加密的秘钥
     * @return
     */
    public static String encrypt(String content,String key){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("加密的内容：{}\n参与加密的秘钥：{}",content,key);
        }
        if(StringUtils.isBlank(content) || StringUtils.isBlank(key)) throw new RuntimeException("要加密的内容或者参与加密的秘钥不能为空！");
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);//创建密码器
            byte[] contentBytes = content.getBytes(DEFAULT_ENCRYPT_CHARSETNAME);
            cipher.init(Cipher.ENCRYPT_MODE,getSecretKey(key));//初始化密码器为加密密码器
            byte[] doFinal = cipher.doFinal(contentBytes);//对内容进行加密
            return Base64.encodeBase64String(doFinal);//使用Base64对加密后的内容进行再次加密
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return null;
    }

    /**
     * 解密
     * @param content 解密内容
     * @param key 秘钥
     * @return
     */
    public static String decrypt(String content,String key){
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("要解密的内容：{}\n参与解密的秘钥：{}",content,key);
        }
        if(StringUtils.isBlank(content) || StringUtils.isBlank(key)) throw new RuntimeException("要解密的内容或者参与解密的秘钥不能为空！");
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


    /**
     * DES秘钥生成器
     * @param key:用于加密的秘钥
     * @return 专门用于DES加密的秘钥
     */
    private static SecretKeySpec getSecretKey(String key) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(DEFAULT_KEY_ALGORITHM);//实例化指定的算法秘钥生成器对象

            generator.init(56,new SecureRandom(key.getBytes(DEFAULT_ENCRYPT_CHARSETNAME)));//初始化为一个56位的秘钥生成器对象，DES加密：采用56位进行加密的

            SecretKey secretKey = generator.generateKey();//生成秘钥

            return new SecretKeySpec(secretKey.getEncoded(),DEFAULT_KEY_ALGORITHM);//转换为DES专用秘钥
        } catch (Exception e) {
            LOGGER.error("",e);
        }
        return null;
    }
}
