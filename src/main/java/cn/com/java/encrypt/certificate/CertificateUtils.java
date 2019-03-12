package cn.com.java.encrypt.certificate;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.net.ssl.*;
import java.security.Signature;
import java.security.cert.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class CertificateUtils {

    /**
     * JAVA 秘钥库(JAVA KEY STORE)JKS
     */
    public static final String KEY_STORE = "JKS";

    public static final String X509 = "X.509";

    public static final String SunX509 = "SunX509";
    public static final String SSL = "SSL";

    /**
     * 由keystore获取私钥
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String keyStorePath,String password,String alias) throws Exception{
        KeyStore keyStore = getKeyStore(keyStorePath,password);
        PrivateKey key = (PrivateKey)keyStore.getKey(alias, password.toCharArray());
        return key;
    }

    /**
     * 由证书获取公钥
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String certificatePath) throws Exception{
        Certificate certificate = getCertificate(certificatePath);
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
    }

    /**
     * 私钥加密
     * @param content
     * @param keyStorePath
     * @param password
     * @param alias
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPri(byte[] content,String keyStorePath,String password,String alias) throws Exception{
        PrivateKey privateKey = getPrivateKey(keyStorePath,password,alias);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 私钥解密
     * @param content
     * @param keyStorePath
     * @param password
     * @param alias
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPri(byte[] content,String keyStorePath,String password,String alias) throws Exception{
        PrivateKey privateKey = getPrivateKey(keyStorePath,password,alias);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥加密
     * @param content
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPub(byte[] content,String certificatePath) throws Exception{
        PublicKey publicKey = getPublicKey(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥解密
     * @param content
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPub(byte[] content,String certificatePath) throws Exception{
        PublicKey publicKey = getPublicKey(certificatePath);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 验证Certificate
     * @param certificatePath
     * @return
     * @throws Exception
     */
    public static boolean verifyCertificate(String certificatePath){
        return verifyCertificate(new Date(),certificatePath);
    }

    /**
     * 验证Certificate是否过期
     * @param date
     * @param certificatePath
     * @return
     */
    public static boolean verifyCertificate(Date date, String certificatePath){
        boolean status = true;
        Certificate certificate = null;
        try {
            certificate = getCertificate(certificatePath);
            status = verifyCertificate(date,certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 验证Certificate是否过期
     * @param date
     * @param certificate
     * @return
     */
    public static boolean verifyCertificate(Date date,Certificate certificate) {
        boolean status = true;
        X509Certificate x509Certificate = (X509Certificate)certificate;
        try {
            x509Certificate.checkValidity(date);
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    /**
     * 签名
     * @param content
     * @param keyStorePath
     * @param password
     * @param alias
     * @return
     * @throws Exception
     */
    public static String sign(byte[] content,String keyStorePath,String password,String alias) throws Exception{
        //获取证书
        X509Certificate x509Certificate = (X509Certificate)getCertificate(keyStorePath,password,alias);

        //获取私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath,password,alias);

        //构建签名器
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(content);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 验签
     * @param content
     * @param certificate
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] content,String certificate,String sign) throws Exception{
        X509Certificate x509Certificate = (X509Certificate)getCertificate(certificate);

        //获取公钥
        PublicKey publicKey = x509Certificate.getPublicKey();

        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(content);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 验证Certificate
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static boolean verifyCertificate(Date date, String keyStorePath,String alias, String password) {
        boolean status = true;
        try {
            Certificate certificate = getCertificate(keyStorePath, alias,password);
            status = verifyCertificate(date, certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 验证Certificate
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static boolean verifyCertificate(String keyStorePath, String alias,String password) {
        return verifyCertificate(new Date(), keyStorePath, alias, password);
    }

    /**
     * 获得Certificate
     * @param certificatePath
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String certificatePath) throws Exception{
        FileInputStream fis = new FileInputStream(certificatePath);
        CertificateFactory factory = CertificateFactory.getInstance(X509);
        Certificate certificate = factory.generateCertificate(fis);
        fis.close();
        return certificate;
    }

    /**
     * 获取Certificate
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String keyStorePath,String password,String alias) throws Exception{
        KeyStore keyStore = getKeyStore(keyStorePath,password);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate;
    }

    /**
     * 获取KeyStore
     * @param keyStorePath
     * @param password
     * @return
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String password) throws Exception{
        KeyStore keyStore = KeyStore.getInstance(KEY_STORE);
        FileInputStream fis = new FileInputStream(keyStorePath);
        keyStore.load(fis,password.toCharArray());
        fis.close();
        return keyStore;
    }

    /**
     * 为HttpsURLConnection配置SSLSocketFactory
     * @param connection
     * @param password 密码
     * @param keyStorePath 秘钥库的位置
     * @param trustKeyStorePath 信任库路径
     * @throws Exception
     */
    public static void configSSLSocketFactory(HttpsURLConnection connection,String password,String keyStorePath,String trustKeyStorePath) throws Exception{
        connection.setSSLSocketFactory(getSSLSocketFactory(password,keyStorePath,trustKeyStorePath));
    }

    /**
     * 获取SSLSockeFactory
     * @param password
     * @param keyStorePath
     * @param trustKeyStorePath 信任库路径
     * @return
     */
    private static SSLSocketFactory getSSLSocketFactory(String password, String keyStorePath, String trustKeyStorePath) throws Exception{

        //初始化秘钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
        KeyStore keyStore = getKeyStore(keyStorePath,password);
        keyManagerFactory.init(keyStore,password.toCharArray());

        //初始化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
        KeyStore trustKeyStore = getKeyStore(trustKeyStorePath,password);
        trustManagerFactory.init(trustKeyStore);

        //初始化SSL上下文
        SSLContext context = SSLContext.getInstance(SSL);
        context.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);
        SSLSocketFactory socketFactory = context.getSocketFactory();

        return socketFactory;
    }
}
