package cn.com.java.encrypt;

import cn.com.java.encrypt.desc.DESCUtils;
import org.junit.Test;

public class DescUtilsTest {

    private static String DEFAULT_KEY = "Ahj3652245~";

    @Test
    public void testEncrypt(){
        String content = "桂昌余是大傻逼";
        String encrypt = DESCUtils.encrypt(content, DEFAULT_KEY);
        System.out.println(encrypt);
    }

    @Test
    public void testDecrypt(){
        String content = "owYwKY34doknZQ9z6h4myP+2IHun8QVC";
        String decrypt = DESCUtils.decrypt(content, DEFAULT_KEY);
        System.out.println(decrypt);
    }
}
