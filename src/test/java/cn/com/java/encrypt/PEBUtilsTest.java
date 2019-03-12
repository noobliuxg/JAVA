package cn.com.java.encrypt;

import cn.com.java.encrypt.pbe.PEBUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class PEBUtilsTest {

    @Test
    public void encryptAndDecrypt(){
        String content = "hello world,桂昌余是大傻逼";
        String passward = "25863555";
        try {
            byte[] bytes = PEBUtils.initSalt();
            String encrypt = PEBUtils.encrypt(content, passward, bytes);
            System.out.println("盐："+Base64.encodeBase64String(bytes));
            System.out.println("原文："+content);
            System.out.println("加密："+encrypt);
            String decrypt = PEBUtils.decrypt(encrypt, passward, bytes);
            System.out.println("解密："+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
