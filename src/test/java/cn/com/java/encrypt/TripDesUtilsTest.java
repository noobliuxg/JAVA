package cn.com.java.encrypt;

import cn.com.java.encrypt.desc.TripDESCUtils;
import org.junit.Test;

public class TripDesUtilsTest {

    private static final String DEFAULT_KEY = "2583369~@";

    @Test
    public void testEncrypt(){
        String content = "hello,你好，桂昌余是大傻逼";
        String encrypt = TripDESCUtils.encrypt(content, DEFAULT_KEY);
        System.out.println(encrypt);
    }

    @Test
    public void testDecryppt(){
        String content = "aeAtBCAUi6LkC/AqdQuot753vr6fo7RWh/y4olkx6OzucGpqQDMBjQ==";
        String decrypt = TripDESCUtils.decrypt(content, DEFAULT_KEY);
        System.out.println(decrypt);
    }
}
