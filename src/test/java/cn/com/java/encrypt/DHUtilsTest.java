package cn.com.java.encrypt;

import cn.com.java.encrypt.dh.DHUtils;
import org.junit.Test;

import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import java.util.Map;

public class DHUtilsTest {

    @Test
    public void encryptAndDecrypt(){
        try {
            Map<String, Object> akeyMap = DHUtils.init();
            System.out.println("甲方公钥："+DHUtils.getPublicKey(akeyMap));
            System.out.println("甲方私钥："+DHUtils.getPrivateKey(akeyMap));
            Map<String, Object> bkeyMap = DHUtils.init(DHUtils.getPublicKey(akeyMap));
            System.out.println("乙方公钥："+DHUtils.getPublicKey(bkeyMap));
            System.out.println("乙方私钥："+DHUtils.getPrivateKey(bkeyMap));
            String content = "asf258669";
            String encrypt = new String(DHUtils.encrypt(content.getBytes(),(DHPublicKey)akeyMap.get(DHUtils.PUBLIC_KEY),(DHPrivateKey)bkeyMap.get(DHUtils.PRIVATE_KEY)));
            String decrypt = new String(DHUtils.decrypt(encrypt.getBytes(), (DHPublicKey)bkeyMap.get(DHUtils.PUBLIC_KEY),(DHPrivateKey)akeyMap.get(DHUtils.PRIVATE_KEY)));
            System.out.println("原文："+content);
            System.out.println("甲方公钥 乙方私钥 加密，密文："+encrypt);
            System.out.println("甲方私钥 乙方公钥 解密，密文："+decrypt);

            System.out.println("===========================================反转过来===========================================");
            encrypt = new String(DHUtils.encrypt(content.getBytes(),(DHPublicKey)bkeyMap.get(DHUtils.PUBLIC_KEY),(DHPrivateKey)akeyMap.get(DHUtils.PRIVATE_KEY)));
            decrypt = new String(DHUtils.decrypt(encrypt.getBytes(), (DHPublicKey)akeyMap.get(DHUtils.PUBLIC_KEY),(DHPrivateKey)bkeyMap.get(DHUtils.PRIVATE_KEY)));
            System.out.println("原文："+content);
            System.out.println("乙方公钥 甲方私钥 加密，密文："+encrypt);
            System.out.println("乙方私钥 甲方公钥 解密，密文："+decrypt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void encryptAndDecryptWithStr(){
        try {
            Map<String, Object> akeyMap = DHUtils.init();
            System.out.println("甲方公钥："+DHUtils.getPublicKey(akeyMap));
            System.out.println("甲方私钥："+DHUtils.getPrivateKey(akeyMap));
            Map<String, Object> bkeyMap = DHUtils.init(DHUtils.getPublicKey(akeyMap));
            System.out.println("乙方公钥："+DHUtils.getPublicKey(bkeyMap));
            System.out.println("乙方私钥："+DHUtils.getPrivateKey(bkeyMap));
            String content = "asf258669";
            String encrypt = new String(DHUtils.encrypt(content.getBytes(),DHUtils.getPublicKey(akeyMap),DHUtils.getPrivateKey(bkeyMap)));
            String decrypt = new String(DHUtils.decrypt(encrypt.getBytes(), DHUtils.getPublicKey(bkeyMap),DHUtils.getPrivateKey(akeyMap)));
            System.out.println("原文："+content);
            System.out.println("甲方公钥 乙方私钥 加密，密文："+encrypt);
            System.out.println("甲方私钥 乙方公钥 解密，密文："+decrypt);

            System.out.println("===========================================反转过来===========================================");
            encrypt = new String(DHUtils.encrypt(content.getBytes(),(DHPublicKey)bkeyMap.get(DHUtils.PUBLIC_KEY),(DHPrivateKey)akeyMap.get(DHUtils.PRIVATE_KEY)));
            decrypt = new String(DHUtils.decrypt(encrypt.getBytes(), (DHPublicKey)akeyMap.get(DHUtils.PUBLIC_KEY),(DHPrivateKey)bkeyMap.get(DHUtils.PRIVATE_KEY)));
            System.out.println("原文："+content);
            System.out.println("乙方公钥 甲方私钥 加密，密文："+encrypt);
            System.out.println("乙方私钥 甲方公钥 解密，密文："+decrypt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
