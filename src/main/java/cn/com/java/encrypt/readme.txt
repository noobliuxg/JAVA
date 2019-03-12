一、单向加密
1、单向加密的种类：MD5、SHA、HMAC
1.1、MD5：广泛用于加密和解密技术，常用于文件校验。校验不管文件多大，经过MD5后都能生成唯一的MD5值。通过碰撞法可以破解
1.2、SHA：数字签名等密码学应用中重要的工具，被广泛地应用于电子商务等信息安全领域。通过碰撞法可以破解
1.3、HMAC：散列消息鉴别码，基于密钥的Hash算法的认证协议。消息鉴别码实现鉴别的原理是，用公开函数和密钥产生一个固定长度的值作为认证标识，用这个标识鉴别消息的完整性。
使用场景：使用一个密钥生成一个固定大小的小数据块，即MAC，并将其加入到消息中，然后传输。接收方利用与发送方共享的密钥进行鉴别认证等。
二、对称加密
1、所谓对称加密算法即：加密和解密使用相同密钥的算法。常见的有DES、3DES、AES、PBE等加密算法，这几种算法安全性依次是逐渐增强的。
1.1、DES加密：DES是一种对称加密算法，是一种非常简便的加密算法，但是密钥长度比较短。
    缺点：DES加密算法出自IBM的研究，后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，24小时内即可被破解。
1.2、3DES加密：是一种对称加密算法，在 DES 的基础上，使用三重数据加密算法，对数据进行加密，它相当于是对每个数据块应用三次 DES 加密算法。
    由来：由于计算机运算能力的增强，原版 DES 密码的密钥长度变得容易被暴力破解；3DES 即是设计用来提供一种相对简单的方法，即通过增加 DES 的密钥长度来避免类似的攻击，而不是设计一种全新的块密码算法这样来说，破解的概率就小了很多。
    缺点：由于使用了三重数据加密算法，可能会比较耗性能。
1.3、AES加密：是一种对称加密算法，在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，已经被多方分析且广为全世界所使用
    128位秘钥：AES和Rijndael加密法并不完全一样（虽然在实际应用中二者可以互换），因为Rijndael加密法可以支持更大范围的区块和密钥长度：AES的区块长度固定为128 比特，密钥长度则可以是128，192或256比特；而Rijndael使用的密钥和区块长度可以是32位的整数倍，以128位为下限，256比特为上限。
1.4、PEB加密：Password-based encryption（基于密码加密）。其特点在于口令由用户自己掌管，不借助任何物理媒体；采用随机数（这里我们叫做盐）杂凑多重加密等方法保证数据的安全性。
三、非对称加密：
1、非对称加密算法需要两个密钥：公开密钥（publickey）和私有密钥（privatekey）。公开密钥与私有密钥是一对，如果用公开密钥对数据进行加密，只有用对应的私有密钥才能解密；如果用私有密钥对数据进行加密，那么只有用对应的公开密钥才能解密。一般公钥是公开的，私钥是自己保存。因为加密和解密使用的是两个不同的密钥，所以这种算法叫作非对称加密算法。安全性相对对称加密来说更高，是一种高级加密方式。
2、非对称加密：RSA、DH
3、DH：密钥一致协议。简单的说就是允许两名用户在公开媒体上交换信息以生成"一致"的、可以共享的密钥。换句话说，就是由甲方产出一对密钥（公钥、私钥），乙方依照甲方公钥产生乙方密钥对（公钥、私钥）。以此为基线，作为数据传输保密基础，同时双方使用同一种对称加密算法构建本地密钥（SecretKey）对数据加密
四、数字签名
1、DSA：是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(DigitalSignature Standard)。简单的说，这是一种更高级的验证方式，用作数字签名。不单单只有公钥、私钥，还有数字签名。私钥加密生成数字签名，公钥验证数据及签名。如果数据和签名不匹配则认为验证失败！数字签名的作用就是校验数据在传输过程中不被修改。
2、ECC：椭圆曲线密码编码学，是目前已知的公钥体制中，对每比特所提供加密强度最高的一种体制。在软件注册保护方面起到很大的作用，一般的序列号通常由该算法产生。
五、证书
1、生成keystore：keytool -genkey -validity 36000 -alias www.liuxg.org -keyalg RSA -keystore d:\keystore\liuxg.keystore 密码：123456
keytool：是jdk只带的生成证书的工具；-genKey：表示生成秘钥；-validity：指定证书的有效期，这里为36000；-alias：指定别名；-keyalg：指定算法，这里为RSA算法；-keystore：证书生成的路径
2、生成自签名证书：keytool -export -keystore d:\keystore\liuxg.keystore -alias www.liuxg.org -file d:\keystore\liuxg.cer -rfc 密码：123456
-export：表示导出操作；-keystore：表示keystore存储的位置；-alias：表示keystore文件的别名；-file：表示指定证书导出的位置;-rfc：以文本格式输出，使用BASE64编码
光有证书是不够的，还需要证书文件，证书才是直接提供给外界使用的公钥凭证。
3、代码做签名—代码签名：通过工具JarSigner可以完成代码签名
   签名：jarsigner -storetype jks -keystore liuxg.keystore -verbose tools.jar www.liuxg.org
   验签：jarsigner -verify -verbose -certs tools.jar
六、SSL
1、将证书导入秘钥库，keytool -import -alias www.liuxg.org -file d:/keystore/liuxg.cer -keystore d:/keystore/liuxg.keystore 密码：654321
    -import：表示导入秘钥操作；-alias：表示别名；-file：表示导入证书的位置；-keystore：表示导入keystore文件
2、在tomcat中放入conf文件夹中放入liuxg.keystore文件，并在server.xml中加入以下内容：
<Connector
    SSLEnabled="true"
    URIEncoding="UTF-8"
    clientAuth="false"  为测试，正式使用true
    keystoreFile="conf/liuxg.keystore"
    keystorePass="123456"
    maxThreads="150"
    port="443"
    protocol="HTTP/1.1"
    scheme="https"
    secure="true"
    sslProtocol="TLS" />
3、绑定域名，在hosts文件中添加：127.0.0.1 www.liuxg.org
4、启动tomcat，输入：www.liuxg.org





