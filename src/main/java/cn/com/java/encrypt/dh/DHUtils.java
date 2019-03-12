package cn.com.java.encrypt.dh;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class DHUtils {

    /**
     * 指定秘钥生成算法
     */
    private static final String KEY_ALGORITHM = "DH";

    /**
     * Default Keysize 1024
     *  Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
     */
    private static final int KEY_SIZE = 1024;

    /**
     * DH加密下需要一种对称加密算法对数据加密，这里我们使用DES，也可以使用其他对称加密算法。
     */
    public static String SELECT_KEY_ALGORITHM = "AES";

    /**
     * 公钥
     */
    public static final String PUBLIC_KEY = "PUBLIC_KEY";

    /**
     * 私钥
     */
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    /**
     * 初始化甲方公钥/私钥对
     * @return
     */
    public static Map<String, Object> init() throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(KEY_SIZE);
        KeyPair keyPair = generator.generateKeyPair();
        DHPublicKey publicKey = (DHPublicKey)keyPair.getPublic();
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>();
        keyMap.put(PUBLIC_KEY,publicKey);
        keyMap.put(PRIVATE_KEY,privateKey);
        return keyMap;
    }

    /**
     * 初始化乙方秘钥对：公钥/私钥
     * @param key 甲方公钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> init(String key)throws Exception{
        //1、解析甲方的公钥
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        DHPublicKey publicKey = (DHPublicKey)factory.generatePublic(keySpec);
        DHParameterSpec dhParameterSpec = publicKey.getParams();

        //2、根据甲方的公钥生成乙方的秘钥对
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(dhParameterSpec);
        KeyPair keyPair = generator.generateKeyPair();
        DHPublicKey dhPublic = (DHPublicKey)keyPair.getPublic();
        DHPrivateKey dhPrivateKey = (DHPrivateKey)keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY,dhPublic);
        keyMap.put(PRIVATE_KEY,dhPrivateKey);
        return keyMap;
    }

    /**
     * 初始化乙方秘钥对：公钥/私钥
     * @param key 甲方公钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> init(byte[] key) throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key));
        KeyFactory instance = KeyFactory.getInstance(KEY_ALGORITHM);
        DHPublicKey publicKey = (DHPublicKey)instance.generatePublic(keySpec);

        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(publicKey.getParams());
        KeyPair keyPair = generator.generateKeyPair();
        DHPublicKey aPublic = (DHPublicKey) keyPair.getPublic();
        DHPrivateKey aPrivate = (DHPrivateKey) keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY,aPublic);
        keyMap.put(PRIVATE_KEY,aPrivate);
        return keyMap;
    }

    /**
     * 使用公钥和私钥 加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String data,String publicKey,String privateKey) throws Exception{
        return new String(encrypt(data.getBytes(),publicKey,privateKey));
    }

    /**
     * 使用公钥和私钥 加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,String publicKey,String privateKey) throws Exception{
        return encrypt(data,getSecretKey(publicKey,privateKey));
    }

    /**
     * 使用公钥和私钥 加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,byte[] publicKey,byte[] privateKey) throws Exception{
        return encrypt(data,getSecretKey(publicKey,privateKey));
    }

    /**
     * 使用公钥和私钥 加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,DHPublicKey publicKey,DHPrivateKey privateKey) throws Exception{
        SecretKey key = getSecretKey(publicKey,privateKey);
        return encrypt(data,key);
    }

    /**
     * 使用 秘钥 加密
     * @param data 待加密数据
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data,SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,key);
        return Base64.encodeBase64(cipher.doFinal(data));
    }

    /**
     * 使用公钥和私钥 解密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String data,String publicKey,String privateKey) throws Exception{
        return new String(encrypt(data.getBytes(),publicKey,privateKey));
    }

    /**
     * 使用公钥和私钥 加密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,String publicKey,String privateKey) throws Exception{
        return decrypt(data,getSecretKey(publicKey,privateKey));
    }

    /**
     * 使用公钥和私钥 解密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,byte[] publicKey,byte[] privateKey) throws Exception{
        return decrypt(data,getSecretKey(publicKey,privateKey));
    }

    /**
     * 使用公钥和私钥 解密
     * @param data 待加密数据
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,DHPublicKey publicKey,DHPrivateKey privateKey) throws Exception{
        SecretKey key = getSecretKey(publicKey,privateKey);
        return decrypt(data,key);
    }

    /**
     * 使用秘钥解密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data,SecretKey key) throws Exception{
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(Base64.decodeBase64(data));
    }

    /**
     * 获取加密/解密的密钥
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretKey(String publicKey,String privateKey) throws Exception{
        DHPublicKey dhPublicKey = loadDHPublicKey(publicKey);
        DHPrivateKey dhPrivateKey = loadPrivateKey(privateKey);
        return getSecretKey(dhPublicKey,dhPrivateKey);
    }

    /**
     * 获取加密/解密的密钥
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretKey(byte[] publicKey,byte[] privateKey) throws Exception{
        return getSecretKey(publicKey,privateKey,SELECT_KEY_ALGORITHM);
    }

    /**
     * 获取DH加密/解密的秘钥
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @param algorithm 加密的算法
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretKey(byte[] publicKey,byte[] privateKey,String algorithm) throws Exception{
        DHPrivateKey dhPrivateKey = loadPrivateKey(privateKey);
        DHPublicKey dhPublicKey = loadDHPublicKey(publicKey);
        return getSecretKey(dhPublicKey,dhPrivateKey,algorithm);
    }

    /**
     * 获取DH加密/解密的密钥
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretKey(DHPublicKey publicKey, DHPrivateKey privateKey) throws Exception{
        return getSecretKey(publicKey,privateKey,SELECT_KEY_ALGORITHM);
    }

    /**
     * 获取DH加密/解密的秘钥
     * @param publicKey DH公钥
     * @param privateKey DH私钥
     * @param algorithm DH加密秘钥的算法
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretKey(DHPublicKey publicKey,DHPrivateKey privateKey,String algorithm) throws Exception{
        KeyAgreement keyAgreement = KeyAgreement.getInstance(KEY_ALGORITHM);
        keyAgreement.init(privateKey);//初始化私钥
        keyAgreement.doPhase(publicKey,true);//初始化公钥
        return keyAgreement.generateSecret(algorithm);//根据指定的对称加密算法生成秘钥
    }

    public static DHPublicKey loadDHPublicKey(byte[] publicKey) throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        return (DHPublicKey) factory.generatePublic(keySpec);
    }

    public static DHPrivateKey loadPrivateKey(byte[] privateKey) throws Exception{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return (DHPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static DHPublicKey loadDHPublicKey(String publicKey) throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        return (DHPublicKey) factory.generatePublic(keySpec);
    }

    public static DHPrivateKey loadPrivateKey(String privateKey) throws Exception{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return (DHPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static String getPublicKey(Map<String,Object> keyMap){
        DHPublicKey publicKey = (DHPublicKey) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public static String getPrivateKey(Map<String,Object> keyMap){
        DHPrivateKey privateKey = (DHPrivateKey) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(privateKey.getEncoded());
    }
}
