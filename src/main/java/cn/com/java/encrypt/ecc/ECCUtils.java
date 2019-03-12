package cn.com.java.encrypt.ecc;

import org.apache.commons.codec.binary.Base64;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import java.math.BigInteger;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;

public class ECCUtils {

    public static final String PUBLIC_KEY = "PUBLIC_KEY";
    public static final String PRIVATE_KEY = "PRIVATE_KEY";

    public static byte[] encrypt(byte[] data, ECPublicKey publicKey) throws Exception{
        ECPublicKeySpec keySpec = new ECPublicKeySpec(publicKey.getW(),publicKey.getParams());

        //TODO Cipher 不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        cipher.init(Cipher.ENCRYPT_MODE,publicKey,keySpec.getParams());
        return cipher.doFinal(data);
    }


    public static byte[] decrypt(byte[] data, ECPrivateKey privateKey) throws Exception{
        ECPrivateKeySpec keySpec = new ECPrivateKeySpec(privateKey.getS(),privateKey.getParams());

        // TODO Cipher不支持EC算法 未能实现
        Cipher cipher = new NullCipher();
        cipher.init(Cipher.DECRYPT_MODE,privateKey,keySpec.getParams());
        return cipher.doFinal(data);
    }

    public static Map<String, ECKey> init() throws Exception{
        BigInteger x1 = new BigInteger(
                "2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
        BigInteger x2 = new BigInteger(
                "289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);

        ECPoint g = new ECPoint(x1, x2);

        // the order of generator
        BigInteger n = new BigInteger(
                "5846006549323611672814741753598448348329118574063", 10);
        // the cofactor
        int h = 2;
        int m = 163;
        int[] ks = { 7, 6, 3 };
        ECFieldF2m ecField = new ECFieldF2m(m, ks);
        // y^2+xy=x^3+x^2+1
        BigInteger a = new BigInteger("1", 2);
        BigInteger b = new BigInteger("1", 2);

        EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);

        ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
                n, h);
        // 公钥
        ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);

        BigInteger s = new BigInteger(
                "1234006549323611672814741753598448348329118574063", 10);
        // 私钥
        ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);

        Map<String, ECKey> keyMap = new HashMap<>(2);

        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    public static String getPublicKey(Map<String,ECKey> keyMap){
        ECPublicKey ecKey = (ECPublicKey)keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(ecKey.getEncoded());
    }

    public static String getPrivateKey(Map<String,ECKey> keyMap){
        ECPrivateKey ecKey = (ECPrivateKey)keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(ecKey.getEncoded());
    }
}
