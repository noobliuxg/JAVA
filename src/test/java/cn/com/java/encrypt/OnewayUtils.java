package cn.com.java.encrypt;

import cn.com.java.encrypt.one_way.HMACUtils;
import cn.com.java.encrypt.one_way.MD5Utils;
import cn.com.java.encrypt.one_way.SHAUtils;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class OnewayUtils {

    @Test
    public void md5(){
        String content = "25874@4343&";
        try {
            String encrypt = MD5Utils.encrypt(content);
            System.out.println(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sha(){
        String content = "1475225";
        try {
            String encrypt = SHAUtils.encrypt(content);
            System.out.println(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hmac(){
        try {
            String encrypt = HMACUtils.encrypt("258774555");
            System.out.println(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
