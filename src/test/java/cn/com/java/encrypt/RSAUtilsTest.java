package cn.com.java.encrypt;

import cn.com.java.encrypt.rsa.RASPrivateUtils;
import cn.com.java.encrypt.rsa.RASPublicUtils;
import cn.com.java.encrypt.rsa.RSAUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public class RSAUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RSAUtilsTest.class);

    @Test
    public void testPublicAndPrivate(){
        try {
            RSAUtils.init();
            PrivateKey privateKey = RSAUtils.getPrivateKey();
            PublicKey publicKey = RSAUtils.getPublicKey();
            System.out.println(privateKey == null);
            System.out.println(publicKey == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void initStorage(){
        try {
            RSAUtils.initStorage("E:\\tmp\\publicKey.keystore","E:\\tmp\\privateKey.keystore");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void signAndVerify(){
        try {
            RSAUtils.init();
            String content = "28845566352552";
            String sign = RSAUtils.sign(RSAUtils.getPrivateKey(), content);
            LOGGER.info("原文：{}",content);
            LOGGER.info("私钥签文：{}",sign);
            boolean verify = RSAUtils.verify(RSAUtils.getPublicKey(), content, sign);
            LOGGER.info("公钥验签结果：{}",verify);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadPublicKey(){
        try {
            String path = "E:\\tmp\\publicKey.keystore";
            //路径
            PublicKey publicKey = RASPublicUtils.loadPublicKey(path);
            System.out.println(publicKey== null);
            System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));

            //File
            File file = new File(path);
            publicKey = RASPublicUtils.loadPublicKey(file);
            System.out.println(publicKey == null);
            System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));

            //InputStream
            InputStream inputStream = new FileInputStream(file);
            publicKey = RASPublicUtils.loadPublicKey(inputStream);
            System.out.println(publicKey == null);
            System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loadPrivateKey(){
        try {
            String path = "E:\\tmp\\privateKey.keystore";
            //路径
            PrivateKey privateKey = RASPrivateUtils.loadPrivateKey(path);
            System.out.println(privateKey== null);
            System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));

            //File
            File file = new File(path);
            privateKey = RASPrivateUtils.loadPrivateKey(file);
            System.out.println(privateKey == null);
            System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));

            //InputStream
            InputStream inputStream = new FileInputStream(file);
            privateKey = RASPrivateUtils.loadPrivateKey(inputStream);
            System.out.println(privateKey == null);
            System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void encryptAndDecrypt(){
        try {
            String privatePath = "E:\\tmp\\privateKey.keystore";
            PrivateKey privateKey = RASPrivateUtils.loadPrivateKey(privatePath);
            String publicPath = "E:\\tmp\\publicKey.keystore";
            PublicKey publicKey = RASPublicUtils.loadPublicKey(publicPath);
            String content = "hello,world！桂昌余是大傻逼";

            String encrypt = RASPrivateUtils.encrypt(privateKey, content);
            String decrypt = RASPublicUtils.decrypt(publicKey, encrypt);

            System.out.println("原文："+content);
            System.out.println("私钥加密密文："+encrypt);
            System.out.println("公钥解密："+decrypt);
            System.out.println("-------------------------------------------------------------------------------------");
            encrypt = RASPublicUtils.encrypt(publicKey, content);
            decrypt = RASPrivateUtils.decrypt(privateKey,encrypt);

            System.out.println("原文："+content);
            System.out.println("公钥加密密文："+encrypt);
            System.out.println("私钥解密："+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
