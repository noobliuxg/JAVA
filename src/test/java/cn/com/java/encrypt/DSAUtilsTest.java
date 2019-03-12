package cn.com.java.encrypt;

import cn.com.java.encrypt.dsa.DSAUtils;
import org.junit.Test;

import java.util.Map;

public class DSAUtilsTest {

    @Test
    public void signAndVerify(){
        try {
            Map<String, Object> init = DSAUtils.init();
            System.out.println("DSA公钥: "+DSAUtils.getPublicKey(init));
            System.out.println("DSA私钥: "+DSAUtils.getPrivateKey(init));
            String content = "14785255523we";
            String sign = DSAUtils.sign(content, DSAUtils.getPrivateKey(init));
            boolean verify = DSAUtils.verify(content, DSAUtils.getPublicKey(init), sign);
            System.out.println("RSA原文: "+content);
            System.out.println("RSA签名: "+sign);
            System.out.println("RSA验签: "+verify);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
