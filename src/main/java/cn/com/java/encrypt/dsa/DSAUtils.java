package cn.com.java.encrypt.dsa;

import org.apache.commons.codec.binary.Base64;

import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class DSAUtils {
    /**
     * 签文：
     * RSA算法
     */
    private static final String KEY_ALGORITHM = "DSA";

    /**
     * 默认密钥字节数
     *
     * <pre>
     * DSA
     * Default Keysize 1024
     * Keysize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
     * </pre>
     */
    public static final int KEY_SIZE = 1024;

    /**
     * 默认的种子
     */
    public static final String DEFAULT_SEED = "0f22507a10bbddd07d8a3082122966e3";
    private static final String PUBLIC_KEY = "PUBLIC_KEY";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";

    public static Map<String,Object> init() throws Exception{
        return init(DEFAULT_SEED);
    }

    public static Map<String,Object> init(String seed) throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);

        SecureRandom random = new SecureRandom();
        random.setSeed(seed.getBytes());

        generator.initialize(KEY_SIZE,random);

        KeyPair keyPair = generator.generateKeyPair();

        DSAPublicKey aPublic = (DSAPublicKey)keyPair.getPublic();
        DSAPrivateKey aPrivate = (DSAPrivateKey)keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PRIVATE_KEY,aPrivate);
        keyMap.put(PUBLIC_KEY,aPublic);
        return keyMap;
    }

    /**
     * RSA签名
     * @param content 待签文数据
     * @param privateKey RSA私钥
     * @return
     * @throws Exception
     */
    public static String sign(String content,String privateKey) throws Exception{
        return new String(sign(content.getBytes(),privateKey.getBytes()));
    }

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey RSA私钥
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] content,byte[] privateKey) throws Exception{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey aPrivate = factory.generatePrivate(keySpec);

        return sign(content,aPrivate);
    }

    /**
     * RSA签文
     * @param content 待签名数据
     * @param privateKey RSA私钥
     * @return
     * @throws Exception
     */
    public static byte[] sign(byte[] content,PrivateKey privateKey) throws Exception{
        Signature signature = Signature.getInstance(privateKey.getAlgorithm());
        signature.initSign(privateKey);
        signature.update(content);
        return Base64.encodeBase64(signature.sign());
    }

    /**
     * RSA解签
     * @param content 参与签名的内容
     * @param publicKey 公钥
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String content,String publicKey,String sign) throws Exception{
        return verify(content.getBytes(),publicKey.getBytes(),sign.getBytes());
    }

    /**
     * RSA解签
     * @param content 参与签名的内容
     * @param publicKey 公钥
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] content,byte[] publicKey,byte[] sign) throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        PublicKey aPublic = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(keySpec);
        return verify(content,aPublic,sign);
    }

    /**
     * RSA解签
     * @param content 参与签名的内容
     * @param publicKey 公钥
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] content,PublicKey publicKey,byte[] sign) throws Exception{
        Signature signature = Signature.getInstance(publicKey.getAlgorithm());
        signature.initVerify(publicKey);
        signature.update(content);
        return signature.verify(Base64.decodeBase64(sign));
    }

    public static String getPublicKey(Map<String,Object> keyMap) throws Exception{
        PublicKey publicKey = (PublicKey) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(publicKey.getEncoded());
    }

    public static String getPrivateKey(Map<String,Object> keyMap) throws Exception{
        PrivateKey privateKey = (PrivateKey) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(privateKey.getEncoded());
    }
}
