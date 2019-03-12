package cn.com.java.email.api;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SendEmailUtils {

    /**
     * mail address
     */
    private static final String SEND_ADDRESS = "517099914@qq.com";
    /**
     * mail username
     */
    private static final String SEND_ACCOUNT = "517099914";
    /**
     * mail password
     */
    private static final String SEND_PASSWORD = "xin447306010";
    /**
     * mail smtp auth default value : true
     */
    private static final String EMAIL_CERTIFICATION = "true";
    /**
     * email smtp host: default value : mail.smtp.host
     */
    private static final String EMAIL_SMTP_HOST = "smtp.qq.com";
    /**
     * email transport protocol default value : smtp
     */
    private static final String EMAMIL_TRANSPORT_PROTOCOL = "smtp";
    /**
     * is not need log default value：true
     */
    private static boolean IAMBUS = true;

    private static Session SESSION = null;

    /**
     * init connect to mail server
     */
    public static void init(){
        Properties properties = new Properties();//1、设置连接邮件服务器的参数

        properties.setProperty("mail.smtp.auth",EMAIL_CERTIFICATION);//设置用户认证协议

        properties.setProperty("mail.smtp.host",EMAIL_SMTP_HOST);//设置发件人的SMTP服务器地址

        properties.setProperty("mail.transport.protocol",EMAMIL_TRANSPORT_PROTOCOL);//设置传输协议

        SESSION = Session.getInstance(properties);//根据连接参数获取环境的会话信息

        SESSION.setDebug(IAMBUS);
    }

    /**
     * send message to address
     * @param message
     */
    public static void sendMail(Message message){
        try {
            Transport transport = SESSION.getTransport();//获取邮件发送对象

            transport.connect(SEND_ACCOUNT,SEND_PASSWORD);//设置用户名和密码

            transport.sendMessage(message,message.getAllRecipients());//发送邮件

            transport.close();//关闭发送接口
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * create text mail
     *
     * @param addressMap
     * @param text
     * @return
     */
    public static Message createTextMessage(Map<MailType, String[]> addressMap,String text){
        MimeMessage message = new MimeMessage(SESSION);

        try {
            message.setFrom(SEND_ADDRESS);

            /**
             * Message.RecipientType.TO,发送人，可以多人
             * Message.RecipientType.CC：抄送人，可以多人
             * Message.RecipientType.BCC：密送,可以多人
             */
            Set<MailType> mailTypes = addressMap.keySet();
            for (MailType mailType : mailTypes){
                switch (mailType){
                    case TO:
                        message.addRecipients(Message.RecipientType.TO,getAddresses(addressMap.get(mailType)));
                        break;
                    case CC:
                        message.addRecipients(Message.RecipientType.CC,getAddresses(addressMap.get(mailType)));
                        break;
                    case BCC:
                        message.addRecipients(Message.RecipientType.BCC,getAddresses(addressMap.get(mailType)));
                        break;
                }
            }
            message.setSubject("测试纯文本邮件","UTF-8");//设置邮件标题

            message.setContent("简单的纯文本邮件！","text/html;charset=UTF-8");

            message.setSentDate(new Date());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static MimeMessage createMimeMessage(Map<MailType,String[]> addressMap) throws Exception {
        MimeMessage message = new MimeMessage(SESSION);

        message.setFrom(SEND_ADDRESS);//设置邮件发送人的邮箱地址

        /**
         * 设置邮件发送人
         */
        Set<MailType> mailTypes = addressMap.keySet();
        for (MailType mailType : mailTypes){
            switch (mailType){
                case TO:
                    message.addRecipients(Message.RecipientType.TO,getAddresses(addressMap.get(mailType)));
                    break;
                case CC:
                    message.addRecipients(Message.RecipientType.CC,getAddresses(addressMap.get(mailType)));
                    break;
                case BCC:
                    message.addRecipients(Message.RecipientType.BCC,getAddresses(addressMap.get(mailType)));
                    break;
            }
        }

        message.setSubject("","UTF-8");//设置邮件标题

        Multipart multipart = crateMultipart();//设置邮件要发送的内容

        message.setContent(multipart);//设置邮件体

        message.setSentDate(new Date());//设置邮件发送时间
        return message;
    }

    private static Multipart crateMultipart() throws MessagingException, UnsupportedEncodingException {
        MimeMultipart multipart = new MimeMultipart();


        MimeBodyPart filePart = new MimeBodyPart();
        String filePath = "";
        filePart.setDataHandler(new DataHandler(new FileDataSource(filePath)));
        filePart.setContentID("MAIL_FILE_ID");

        //创建文本"节点"，携带超链接
        MimeBodyPart textPart = new MimeBodyPart();
        //这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
        textPart.setContent("这是一张图片<br/><a href='http://www.cnblogs.com/ysocean/p/7666061.html'><img src='cid:mailTestPic'/></a>","text/html;charset=UTF-8");


        //将 文本+图片 的混合"节点"封装成一个普通"节点"
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        //上面的 mailTestPic 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
        MimeMultipart text_file_port = new MimeMultipart();
        text_file_port.addBodyPart(filePart);
        text_file_port.addBodyPart(textPart);
        text_file_port.setSubType("related");//设置关联关系


        // 将 文本+图片 的混合"节点"封装成一个普通"节点"
        // 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
        // 上面的 filePart 并非 BodyPart, 所有要把 text_file_port 封装成一个 BodyPart
        MimeBodyPart text_file = new MimeBodyPart();
        text_file.setContent(text_file_port);

        // 创建附件"节点"
        MimeBodyPart attachment = new MimeBodyPart();
        // 读取本地文件
        DataHandler dataHandler = new DataHandler(new FileDataSource("src\\mailDocx.docx"));
        // 将附件数据添加到"节点"
        attachment.setDataHandler(dataHandler);
        // 设置附件的文件名（需要编码）
        attachment.setFileName(MimeUtility.encodeText(dataHandler.getName()));

        //设置（文本+图片）和 附件 的关系（合成一个大的混合"节点" / Multipart ）
        multipart.addBodyPart(text_file);
        multipart.addBodyPart(attachment);     // 如果有多个附件，可以创建多个多次添加
        multipart.setSubType("mixed");         // 混合关系

        return multipart;
    }

    private static Address[] getAddresses(String[] addressArr){
        Address[] addresses = new Address[addressArr.length];
        for (int i=0;i<addressArr.length;i++){
            try {
                addresses[i] = new InternetAddress(addressArr[i]);
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }
        return addresses;
    }

}
