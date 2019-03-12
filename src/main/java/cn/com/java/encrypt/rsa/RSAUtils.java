package cn.com.java.encrypt.rsa;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtils.class);

    /**
     * 公钥key
     */
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /**
     * 私钥key
     */
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 一次公钥与私钥存放的Map
     */
    private static Map<String,Key> KEY_MAP = null;

    /**
     * 初始化公钥/私钥
     * @throws Exception
     */
    public static void init() throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RASContants.KEY_ALGORITHM);//根据算法指定私钥/公钥生成器
        generator.initialize(1024);//初始化生成器的大小
        KeyPair keyPair = generator.generateKeyPair();//获取私钥/公钥器
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();
        KEY_MAP = new HashMap<>();
        KEY_MAP.put(PUBLIC_KEY,aPublic);
        KEY_MAP.put(PRIVATE_KEY,aPrivate);
    }

    /**
     * 初始化公钥/私钥并存储在指定的文件中
     * @param publicPath 公钥路径
     * @param privatePath 私钥路径
     * @throws Exception
     */
    public static void initStorage(String publicPath,String privatePath) throws Exception{
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("公钥存储路径：{} \n 私钥存储路径：{}",publicPath,privatePath);
        }
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RASContants.KEY_ALGORITHM);
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        storage(keyPair.getPublic(),publicPath);
        storage(keyPair.getPrivate(),privatePath);
    }

    /**
     * 存储私钥
     * @param privateKey
     * @param path
     * @throws IOException
     */
    public static void storage(PrivateKey privateKey,String path) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(Base64.encodeBase64String(privateKey.getEncoded()));
        writer.flush();
        writer.close();
    }

    /**
     * 存储公钥
     * @param publicKey
     * @param path
     * @throws Exception
     */
    public static void storage(PublicKey publicKey,String path) throws Exception{
        FileWriter fileWriter = new FileWriter(path);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(Base64.encodeBase64String(publicKey.getEncoded()));
        writer.flush();
        writer.close();
    }

    /**
     * 签名
     * @param key 私钥
     * @param content 待签名数据
     * @return
     * @throws Exception
     */
    public static String sign(PrivateKey key,String content) throws Exception{
        return sign(key,content,null);
    }

    /**
     * 签名
     * @param key 私钥
     * @param content 待签名数据
     * @param charset 编码格式
     * @return
     * @throws Exception
     */
    public static String sign(PrivateKey key,String content,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = RASContants.UTF;
        return new String(sign(key,content.getBytes(charset)),charset);
    }

    /**
     * 签名
     * @param key 私钥
     * @param data 待签名数据
     * @return
     * @throws Exception
     */
    public static byte[] sign(PrivateKey key,byte[] data) throws Exception{
        Signature signature = Signature.getInstance(RASContants.SIGNATURE_ALGORITHM);//获取指定算法的签约器
        signature.initSign(key);//初始化签民私钥
        signature.update(data);//更新待签名数据
        return Base64.encodeBase64(signature.sign());//对待签名数据签名完成后，使用Base64加密
    }

    /**
     * 验签
     * @param key 公钥
     * @param data 参与签名数据
     * @param sign 签名(Base64加密)
     * @return
     * @throws Exception
     */
    public static boolean verify(PublicKey key,String data,String sign) throws Exception{
        return verify(key,data,sign,RASContants.UTF);
    }

    /**
     * 验签
     * @param key 公钥
     * @param data 参与签名数据
     * @param sign 签名(Base64加密)
     * @param charset 编码格式
     * @return
     * @throws Exception
     */
    public static boolean verify(PublicKey key,String data,String sign,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = RASContants.UTF;
        return verify(key,data.getBytes(charset),sign.getBytes(charset));
    }

    /**
     * 验签
     * @param key 公钥
     * @param data 参与签名数据
     * @param sign 签名(Base64加密)
     * @return
     * @throws Exception
     */
    public static boolean verify(PublicKey key,byte[] data,byte[] sign) throws Exception{
        Signature signature = Signature.getInstance(RASContants.SIGNATURE_ALGORITHM);//获取指定算的签约器
        signature.initVerify(key);//初始化验证签名的公钥
        signature.update(data);//更新要参与验证的原始数据
        return signature.verify(Base64.decodeBase64(sign));//对签名数据进行校验
    }

    /**
     * 获取私钥
     * @return
     */
    public static PrivateKey getPrivateKey(){return (PrivateKey) KEY_MAP.get(PRIVATE_KEY);}

    /**
     * 获取公钥
     * @return
     */
    public static PublicKey getPublicKey(){ return (PublicKey)KEY_MAP.get(PUBLIC_KEY);}
}
