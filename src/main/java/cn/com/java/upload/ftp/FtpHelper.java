package cn.com.java.upload.ftp;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FtpHelper {

    /**
     * default charset:UTF-8
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * default timeout：1 min
     */
    private static final int DEFAULT_TIMEOUT = 60*1000;

    /**
     * default file path :
     */
    private static final String DAILY_FILE_PATH = "dailyFilePath";


    private String host = null;

    private int port = 21;

    private String username = null;

    private String password = null;

    private FTPClient ftpClient;

    private volatile String ftpBasePath;

    private FtpHelper(String host,String username,String password){
        this(host,-1,username,password,DEFAULT_CHARSET);
    }

    private FtpHelper(String host,int port,String username,String password,String charset){
        this.ftpClient = new FTPClient();//创建客户端
        ftpClient.setControlEncoding(charset);//设置连接编码
        this.host = (StringUtils.isBlank(host) ? "127.0.0.1" : host);
        this.port = (port<=0 ? 21 : port);
        this.username = (StringUtils.isBlank(username) ? "anonymous" : username);
        this.password = password;
    }

    /**
     *  <p>Description: create FtpHelper </p>
     *
     * @param host
     * @param username
     * @param password
     * @return
     */
    public static FtpHelper createFtpHelper(String host,String username,String password){
        return new FtpHelper(host,username,password);
    }

    /**
     * <p>Description: create FtpHelper </p>
     *
     * @param host
     * @param username
     * @param password
     * @param charset
     * @return
     */
    public static FtpHelper createFtpHelper(String host,String username,String password,String charset){
        return new FtpHelper(host,-1,username,password,charset);
    }


    /**
     * <p>Description: set time out </p>
     *
     * @param defaultTimeout
     * @param connectTimeout
     * @param dataTimeout
     */
    public void setTimeout(int defaultTimeout,int connectTimeout,int dataTimeout){
        ftpClient.setDefaultTimeout(defaultTimeout);
        ftpClient.setConnectTimeout(connectTimeout);
        ftpClient.setDataTimeout(dataTimeout);
    }

    /**
     * <p>Description：connect FTP SERVER</p>
     * @throws Exception
     */
    public void connect() throws Exception{
        try {
            this.ftpClient.connect(host,port);//连接到指定的ftp服务器上
        } catch (UnknownHostException e) {
            throw new IOException("Can't find FTP server："+host);
        }
        int replyCode = this.ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)){
            disconnect();
            throw new IOException("Can't connect FTP server："+host);
        }

        boolean login = this.ftpClient.login(this.username, this.password);//登陆到ftp服务器上
        if (!login){
            disconnect();
            throw new IOException("can't login FTP server："+host);
        }

        //set data transfer mode : binary
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        //User passive mode to pass firewalls
        ftpClient.enterLocalPassiveMode();

        initFtpBasePath();
    }

    /**
     * <p>Description：init ftp base path</p>
     */
    private void initFtpBasePath() throws IOException {
        if (StringUtils.isBlank(ftpBasePath)){
            synchronized (this){
                ftpBasePath = ftpClient.printWorkingDirectory();
            }
        }
    }

    /**
     * <p>Description：disconnect FTP Server</p>
     */
    private void disconnect(){
        try {
            if (ftpClient!=null && isConnect()){
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {

        }
    }

    /**
     * <p>Description：connect FTP Server status</p>
     *
     * @return     {@code true} if client connect ftp , {@code false} otherwise
     */
    private boolean isConnect() {
        return ftpClient.isConnected();
    }

    /**
     * <p>Description：upload ftpBasePath/dailyFilePath/yyyy/MM/dd/</p>
     *
     * @param fileName
     * @param inputStream
     * @return
     * @throws IOException
     */
    public String uploadFileToDailyDir(String fileName, InputStream inputStream) throws IOException {
        changeWorkingDir(ftpBasePath);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = dateFormat.format(new Date());
        String uploadDir = DAILY_FILE_PATH + formatDate;
        makeDirs(uploadDir);
        storeFile(fileName,inputStream);
        return formatDate+ File.separator+fileName ;
    }

    /**
     * <p>Description：download dailyFilePathDir file</p>
     * 
     * @param dailyDirFilePath
     * @param outputStream
     * @throws IOException
     */
    public void downloadFileFromDailyDir(String dailyDirFilePath, OutputStream outputStream) throws IOException{
        changeWorkingDir(ftpBasePath);
        String ftpRealFilePath = DAILY_FILE_PATH+dailyDirFilePath;
        ftpClient.retrieveFile(ftpRealFilePath,outputStream);//获取ftp服务其中的文件，并填充到OutputStream中
    }

    /**
     *<p>Description：get file from FTP by fileFtpName to OutputStream</p>
     *
     * @param ftpFileName
     * @param outputStream
     * @throws IOException
     */
    public void retrieveFile(String ftpFileName, OutputStream outputStream) throws IOException {
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(ftpFileName);
            if (ftpFiles == null || ftpFiles.length==0){
                throw new IOException("File '"+ftpFileName+"' was not found on FTP server");
            }
            FTPFile ftpFile = ftpFiles[0];
            if (ftpFile.getSize()>Integer.MAX_VALUE){
                throw new IOException("File '"+ftpFileName+"' is too large.");
            }
            if (!ftpClient.retrieveFile(ftpFileName,outputStream)){
                throw new IOException("Error load File '"+ftpFileName+"' from FTP server.Check FTP permissions and path.");
            }
        } finally {
            closeStream(outputStream);
        }
    }

    /**
     * <p>Description：store fileName of file to FTP Server</p>
     *
     * @param fileName
     * @param inputStream
     * @throws IOException
     */
    public void storeFile(String fileName, InputStream inputStream) throws IOException {
        try {
            if (!ftpClient.storeFile(fileName,inputStream)){
                throw new IOException("Can't upload File '"+fileName+"' to FTP server.Check FTP permissions and path");
            }
        } finally {
            closeStream(inputStream);
        }
    }

    /**
     * <p>Description：delete file from FTP server.</p>
     * @param ftpFileName
     * @throws IOException
     */
    public void deletFile(String ftpFileName) throws IOException{
        if (!ftpClient.deleteFile(ftpFileName)){
            throw new IOException("Can't remove File["+ftpFileName+"] from FTP server.");
        }
    }

    /**
     * <p>Description：upload localFile to Ftp Server</p>
     *
     * @param ftpFileName
     * @param localFile
     * @throws IOException
     */
    public void upload(String ftpFileName,File localFile) throws IOException{
        if (localFile==null || !localFile.exists()){
            throw new IOException("Can't upload '"+localFile.getAbsolutePath()+"'.This is not exists");
        }

        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(localFile));
            if (!ftpClient.storeFile(ftpFileName,in)){
                throw new IOException("Can't upload File '"+ftpFileName+"' to FTP server.Check FTP permissions and path");
            }
        } finally {
            closeStream(in);
        }
    }

    /**
     * <p>Description：upload FileDir to Ftp Server.</p>
     *
     * @param remotePath
     * @param localPath
     * @throws IOException
     */
    public void uploadDir(String remotePath,String localPath) throws IOException{
        localPath.replaceAll("\\\\","/");
        File file = new File(localPath);
        if (file.exists()){
            if (!ftpClient.changeWorkingDirectory(remotePath)){//如果不能成功地切换到指定的ftp文件路径下
                ftpClient.makeDirectory(remotePath);//先在ftp上新建指定路径
                ftpClient.changeWorkingDirectory(remotePath);//在切换路径
            }
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")){
                    uploadDir(remotePath+File.separator+f.getName(),f.getPath());
                }else if(f.isFile()){
                    upload(remotePath+File.separator+f.getName(),file);
                }
            }
        }
    }

    /**
     * <p>Description：download ftpFileName from Ftp Server to localFile</p>
     * @param ftpFileName
     * @param localFile
     * @throws IOException
     */
    public void download(String ftpFileName,File localFile) throws IOException{
        FTPFile[] ftpFiles = ftpClient.listFiles(ftpFileName);
        if (ftpFiles==null || ftpFiles.length<=0){
            throw new IOException("File '"+ftpFileName+"' was not found by Ftp Server");
        }
        FTPFile ftpFile = ftpFiles[0];
        if (ftpFile.getSize()>Integer.MAX_VALUE){
            throw new IOException("File '"+ftpFileName+"' is too large.");
        }
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
            if (!ftpClient.retrieveFile(ftpFileName,outputStream)){
                throw new IOException("Error load File '"+ftpFileName+"' from FTP Server.Check FTP permission and path");
            }
        } finally {
            closeStream(outputStream);
        }
    }

    /**
     * <p>Description：download remotepath from Ftp Server To localPath</p>
     *
     * @param remotePath
     * @param localPath
     * @throws IOException
     */
    public void downloadDir(String remotePath,String localPath) throws IOException {
        localPath.replaceAll("\\\\","/");
        File localFile = new File(localPath);
        if (!localFile.exists()){
            localFile.mkdirs();
        }
        FTPFile[] ftpFiles = ftpClient.listFiles(remotePath);
        for (FTPFile ftpFile : ftpFiles) {
            if (ftpFile.isDirectory() && !ftpFile.getName().equals(".") && !ftpFile.getName().equals("..")){
                downloadDir(remotePath+File.separator+ftpFile.getName(),localFile+File.separator+ftpFile.getName());
            }else if(ftpFile.isFile()){
                download(remotePath+File.separator+ftpFile.getName(),new File(localFile+File.separator+ftpFile.getName()));
            }
        }
    }

    /**
     * <p>Description：list remotePath file with fileName</p>
     *
     * @param remotePath
     * @return
     * @throws IOException
     */
    public List<String> listFileNames(String remotePath) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles(remotePath);
        if (ftpFiles!=null && ftpFiles.length>=0){
            List<String> nameList = new ArrayList<>();
            for (FTPFile ftpFile : ftpFiles) {
                nameList.add(ftpFile.getName());
            }
            return nameList;
        }
        return null;
    }

    /**
     * <p>Description：send SiteCommand to Ftp Server.</p>
     * @param args
     * @throws IOException
     */
    public void sendSiteCommand(String args) throws IOException {
        if (isConnect()){
            ftpClient.sendSiteCommand(args);
        }
    }

    /**
     * <p>create fileDir on Ftp Server</p>
     *
     * @param filePath
     * @throws IOException
     */
    private void makeDirs(String filePath) throws IOException {
        filePath.replaceAll("\\\\","/");
        String[] split = filePath.split("/");
        for (String each : split){
            ftpClient.makeDirectory(each);
            ftpClient.changeWorkingDirectory(each);
        }

    }


    /**
     * <p>Description:[获取当前所处的工作目录]</p>
     * Created on 2018/6/6
     *
     * @return java.lang.String 当前所处的工作目录
     * @author 叶向阳
     */
    public String printWorkingDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }

        try {
            return ftpClient.printWorkingDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return "";
    }

    /**
     *  <p>Description: go to current Parent path</p>
     *
     * @return
     */
    public boolean changeToParentDirectory() {

        if (!ftpClient.isConnected()) {
            return false;
        }

        try {
            return ftpClient.changeToParentDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return false;
    }

    /**
     * <p>Description: print current Parent path</p>
     *
     * @return
     */
    public String printParentDirectory() {
        if (!ftpClient.isConnected()) {
            return "";
        }

        String w = printWorkingDirectory();
        changeToParentDirectory();
        String p = printWorkingDirectory();
        changeWorkingDir(w);

        return p;
    }

    /**
     * <p>Description: create file Dir on Ftp Server.</p>
     *
     * @param pathname
     * @return
     * @throws IOException
     */
    public boolean makeDirectory(String pathname) throws IOException {
        return ftpClient.makeDirectory(pathname);
    }


    /**
     * <p> change current Ftp Server working dir</p>
     * @param ftpDir
     * @return
     */
    private boolean changeWorkingDir(String ftpDir) {
        if (!ftpClient.isConnected()){
            return false;
        }
        try {
            return ftpClient.changeWorkingDirectory(ftpDir);
        } catch (IOException e) {
        }
        return false;
    }

    private void closeStream(InputStream inputStream) {
        if (inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    private void closeStream(OutputStream outputStream) {
        if (outputStream!=null){
            try {
                outputStream.close();
            } catch (IOException e) {
            }
        }
    }

}
