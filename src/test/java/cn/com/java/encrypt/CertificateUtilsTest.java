package cn.com.java.encrypt;

import cn.com.java.encrypt.certificate.CertificateUtils;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

public class CertificateUtilsTest {

    private String password = "123456";
    private String alias = "www.liuxg.org";
    private String certificatePath = "d:/keystore/liuxg.cer";
    private String keyStorePath = "d:/keystore/liuxg.keystore";
    private String clientKeyStorePath = "d:/keystore/zlex-client.keystore";
    private String clientPassword = "123456";
    @Test
    public void test() throws Exception {
        System.err.println("公钥加密——私钥解密");
        String inputStr = "Ceritifcate";
        byte[] data = inputStr.getBytes();
        byte[] encrypt = CertificateUtils.encryptByPub(data,certificatePath);
        byte[] decrypt = CertificateUtils.decryptByPri(encrypt,keyStorePath, password, alias);
        String outputStr = new String(decrypt);

        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

    }

    @Test
    public void testSign() throws Exception {
        System.err.println("私钥加密——公钥解密");

        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
        byte[] encodedData = CertificateUtils.encryptByPri(data,keyStorePath, password, alias);
        byte[] decodedData = CertificateUtils.decryptByPub(encodedData,certificatePath);

        String outputStr = new String(decodedData);
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

        System.err.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = CertificateUtils.sign(encodedData, keyStorePath, password,alias);
        System.err.println("签名:\r" + sign);

        // 验证签名
        boolean status = CertificateUtils.verify(encodedData,certificatePath,sign);
        System.err.println("状态:\r" + status);
    }

    @Test
    public void testHttps() throws Exception {
        URL url = new URL("https://www.liuxg.org/examples/");
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);

        CertificateUtils.configSSLSocketFactory(conn, clientPassword,
                clientKeyStorePath, clientKeyStorePath);

        InputStream is = conn.getInputStream();

        int length = conn.getContentLength();

        DataInputStream dis = new DataInputStream(is);
        byte[] data = new byte[length];
        dis.readFully(data);

        dis.close();
        System.err.println(new String(data));
        conn.disconnect();
    }
}
