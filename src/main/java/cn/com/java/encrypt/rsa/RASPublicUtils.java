package cn.com.java.encrypt.rsa;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class RASPublicUtils {


    /**
     * 获取Publickey
     * @param inputStream 公钥输入流
     * @return
     * @throws Exception
     */
    public static PublicKey loadPublicKey(InputStream inputStream) throws Exception {
        byte[] cache = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (;;){
            int offset = inputStream.read(cache);
            if (offset<0){
                break;
            }
            outputStream.write(cache,0,offset);
        }
        byte[] bytes = outputStream.toByteArray();
        outputStream.flush();
        outputStream.close();
        return getPublicKey(bytes);
    }

    /**
     * 获取Publickey
     * @param file 公钥文件
     * @return
     * @throws Exception
     */
    public static PublicKey loadPublicKey(File file) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buffer = new StringBuffer();
        for (;;){
            String cache = reader.readLine();
            if (cache == null){
                break;
            }
            buffer.append(cache);
        }
        return getPublicKey(buffer.toString());
    }

    /**
     * 获取Publickey
     * @param path 公钥路径
     * @return
     * @throws Exception
     */
    public static PublicKey loadPublicKey(String path) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuffer buffer = new StringBuffer();
        for (;;){
            String cache = reader.readLine();
            if (cache == null){
                break;
            }
            buffer.append(cache);
        }
        return getPublicKey(buffer.toString());
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param content 加密数据
     * @return
     * @throws Exception
     */
    public static String encrypt(PublicKey publicKey,String content) throws Exception{
        return new String(encrypt(publicKey,content,RASContants.UTF));
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param content 加密数据
     * @param charset 编码格式
     * @return
     * @throws Exception
     */
    public static String encrypt(PublicKey publicKey,String content,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = RASContants.UTF;
        byte[] bytes = content.getBytes(charset);
        return new String(encrypt(publicKey,bytes));
    }

    /**
     * 公钥加密
     * @param publicKey 公钥
     * @param data 加密数据
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(PublicKey publicKey,byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance(RASContants.KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return Base64.encodeBase64(cipher.doFinal(data));
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param content 解密数据
     * @return
     * @throws Exception
     */
    public static String decrypt(PublicKey publicKey,String content) throws Exception{
        return new String(decrypt(publicKey,content,RASContants.UTF));
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param content 解密数据
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decrypt(PublicKey publicKey,String content,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = RASContants.UTF;
        byte[] bytes = content.getBytes(charset);
        return new String(decrypt(publicKey,bytes));
    }

    /**
     * 公钥解密
     * @param publicKey 公钥
     * @param data 解密数据
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(PublicKey publicKey,byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance(RASContants.KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        return cipher.doFinal(Base64.decodeBase64(data));
    }

    /**
     * 获取公钥
     * @param data 公钥数据
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(byte[] data) throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(data));//公钥解析构造器
        KeyFactory factory = KeyFactory.getInstance(RASContants.KEY_ALGORITHM);//根据算法获取对应的算法工厂
        return factory.generatePublic(keySpec);//获取对应算法的公钥
    }

    /**
     * 获取公钥
     * @param data 公钥数据
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String data) throws Exception{
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(data));
        KeyFactory factory = KeyFactory.getInstance(RASContants.KEY_ALGORITHM);
        return factory.generatePublic(keySpec);
    }

}
