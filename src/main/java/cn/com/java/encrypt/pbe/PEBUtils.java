package cn.com.java.encrypt.pbe;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.util.Random;

public class PEBUtils {

    /**
     * 加密算法
     *  PBEWithMD5AndDES
     *  PBEWithMD5AndTripleDES
     *  PBEWithSHA1AndDESede
     *  PBEWithSHA1AndRC2_40
     */
    public static final String KEY_PEB_ALGORITHM = "PBEWITHMD5andDES";

    /**
     * 盐初始化
     * @return
     * @throws Exception
     */
    public static byte[] initSalt() throws Exception{
        byte[] salt = new byte[8];
        Random random = new Random();
        random.nextBytes(salt);
        return salt;
    }

    /**
     * 初始化秘钥
     * @param passward
     * @return
     * @throws Exception
     */
    public static Key initKey(String passward) throws Exception{
        PBEKeySpec keySpec = new PBEKeySpec(passward.toCharArray());//获取秘钥构造器
        SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_PEB_ALGORITHM);//根据指定的算法获取秘钥工厂
        SecretKey secretKey = factory.generateSecret(keySpec);//生成指定的秘钥
        return secretKey;
    }

    /**
     * 加密，Base64加密
     * @param data 待加密数据
     * @param passward 密码
     * @return
     * @throws Exception
     */
    public static String encrypt(String data,String passward,byte[] salt) throws Exception{
        return encrypt(data.getBytes(),passward,salt);
    }

    /**
     * 加密,Base64加密
     * @param data 待加密数据
     * @param passward 密码
     * @return
     * @throws Exception
     */
    public static String encrypt(byte[] data,String passward,byte[] salt) throws Exception{
        return Base64.encodeBase64String(encrypt(data,initKey(passward),salt));
    }

    /**
     * 加密
     * @param data 加密数据
     * @param key 加密秘钥
     * @param salt 盐
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,Key key,byte[] salt) throws Exception{
        Cipher cipher = Cipher.getInstance(KEY_PEB_ALGORITHM);
        PBEParameterSpec spec = new PBEParameterSpec(salt,100);
        cipher.init(Cipher.ENCRYPT_MODE,key,spec);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     * @param data 解密数据
     * @param password 密码
     * @param salt 盐
     * @return
     * @throws Exception
     */
    public static String decrypt(String data,String password,byte[] salt) throws Exception{
        return decrypt(data.getBytes(),password,salt);
    }

    /**
     * 解密
     * @param data 解密数据
     * @param password 密码
     * @param salt 盐
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] data,String password,byte[] salt) throws Exception{
        return new String(decrypt(data,initKey(password),salt));
    }

    /**
     * 解密
     * @param data 解密数据
     * @param key 秘钥
     * @param salt 盐
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,Key key,byte[] salt) throws Exception{
        Cipher cipher = Cipher.getInstance(KEY_PEB_ALGORITHM);
        PBEParameterSpec spec = new PBEParameterSpec(salt,100);
        cipher.init(Cipher.DECRYPT_MODE,key,spec);
        return cipher.doFinal(Base64.decodeBase64(data));
    }

}
