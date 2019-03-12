package cn.com.java.encrypt;

import cn.com.java.encrypt.ecc.ECCUtils;
import org.junit.Test;

import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Map;


public class ECUtilsTest {

    @Test
    public void test() throws Exception {
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();

        try {
            Map<String, ECKey> keyMap = ECCUtils.init();
            String publicKey = ECCUtils.getPublicKey(keyMap);
            String privateKey = ECCUtils.getPrivateKey(keyMap);
            System.err.println("公钥: \n" + publicKey);
            System.err.println("私钥： \n" + privateKey);

            byte[] encodedData = ECCUtils.encrypt(data, (ECPublicKey) keyMap.get(ECCUtils.PUBLIC_KEY));

            byte[] decodedData = ECCUtils.decrypt(encodedData, (ECPrivateKey) keyMap.get(ECCUtils.PRIVATE_KEY));

            String outputStr = new String(decodedData);
            System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
