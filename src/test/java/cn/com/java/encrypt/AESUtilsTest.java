package cn.com.java.encrypt;

import cn.com.java.encrypt.aes.AESUtils;
import org.junit.Test;

public class AESUtilsTest {

    private static final String DEFAULT_KEY = "cn.com.java.encrypt.Aes";

    @Test
    public void testEncrypt(){
        String content = "hello 你好！@,大傻逼";
        String encrypt = AESUtils.encrypt(content, DEFAULT_KEY);
        System.out.println(encrypt);
    }

    @Test
    public void testDecrypt(){
        String content = "+VtWM/X3//ZWU1TmPbID6zajmPjSZuscI/E0uli2QTA=";
        String decrypt = AESUtils.decrypt(content, DEFAULT_KEY);
        System.out.println(decrypt);
    }
}
