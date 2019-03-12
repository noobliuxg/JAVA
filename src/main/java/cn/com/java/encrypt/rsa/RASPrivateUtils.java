package cn.com.java.encrypt.rsa;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class RASPrivateUtils {

    /**
     * 获取PrivateKey
     * @param inputStream 私钥输入流
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(InputStream inputStream) throws Exception{
        byte[] cache = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (;;){
            int offset = inputStream.read(cache);
            if (offset<0){
                break;
            }
            out.write(cache,0,offset);
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return getPrivateKey(bytes);
    }

    /**
     * 获取PrivateKey
     * @param file 私钥文件
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(File file)throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuffer buffer = new StringBuffer();
        for (;;){
            String readLine = reader.readLine();
            if (readLine==null){
                break;
            }
            buffer.append(readLine);
        }
        return getPrivateKey(buffer.toString());
    }

    /**
     * 获取PrivateKey
     * @param path 私钥路径
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String path) throws Exception{
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuffer buffer = new StringBuffer();
        for (;;){
            String readLine = reader.readLine();
            if (readLine==null){
                break;
            }
            buffer.append(readLine);
        }
        return getPrivateKey(buffer.toString());
    }

    /**
     * 私钥加密
     * @param key 私钥
     * @param content 加密数据
     * @return
     * @throws Exception
     */
    public static String encrypt(PrivateKey key,String content) throws Exception{
        byte[] data = content.getBytes(RASContants.UTF);
        return new String(encrypt(key,data));
    }

    /**
     * 私钥加密
     * @param key 私钥
     * @param content 加密数据
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String encrypt(PrivateKey key,String content,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = RASContants.UTF;
        byte[] data = content.getBytes(charset);
        return new String(encrypt(key,data));
    }

    /**
     * 私钥加密
     * @param key 私钥
     * @param data 加密数据
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(PrivateKey key,byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance(RASContants.KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        return Base64.encodeBase64(cipher.doFinal(data));
    }

    /**
     * 私钥解密
     * @param key 私钥
     * @param content 解密数据
     * @return
     * @throws Exception
     */
    public static String decrypt(PrivateKey key,String content) throws Exception{
        return new String(decrypt(key,content,RASContants.UTF));
    }

    /**
     * 私钥解密
     * @param key 私钥
     * @param content 解密数据
     * @param charset 编码
     * @return
     * @throws Exception
     */
    public static String decrypt(PrivateKey key,String content,String charset) throws Exception{
        if (StringUtils.isBlank(charset)) charset = RASContants.UTF;
        return new String(decrypt(key,content.getBytes(charset)));
    }

    /**
     * 私钥解密
     * @param key 私钥
     * @param data 解密数据
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(PrivateKey key,byte[] data) throws Exception{
        Cipher cipher = Cipher.getInstance(RASContants.KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,key);
        return cipher.doFinal(Base64.decodeBase64(data));
    }

    /**
     * 获取私钥
     * @param data 私钥数据
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(byte[] data) throws Exception{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(data));
        KeyFactory factory = KeyFactory.getInstance(RASContants.KEY_ALGORITHM);
        return factory.generatePrivate(keySpec);
    }

    /**
     * 获取私钥
     * @param data 私钥数据
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String data) throws Exception{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(data));
        KeyFactory factory = KeyFactory.getInstance(RASContants.KEY_ALGORITHM);
        return factory.generatePrivate(keySpec);
    }

}
