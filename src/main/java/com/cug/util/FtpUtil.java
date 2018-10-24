package com.cug.util;



import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018/10/8 0008.
 */
public class FtpUtil {
    private static Logger logger= LoggerFactory.getLogger(FtpUtil.class);

    private static String ftpIp=PropertiesUtil.getValue("ftp.server.ip");

    private static String ftpUser=PropertiesUtil.getValue("ftp.user");

    private static String ftpPass=PropertiesUtil.getValue("ftp.pass");

    private int port;
    private FTPClient ftpClient;
    private String ip;
    private String user;
    private String pass;

    public FtpUtil(String ip,int port,String user,String pass){
        this.ip=ip;
        this.port=port;
        this.user=user;
        this.pass=pass;

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil=new FtpUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接FTP服务器");
        boolean result=ftpUtil.uploadFile("img",fileList);
        logger.info("结束上传，上传结果为：{}",result);
        return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean upload=true;
        FileInputStream fis=null;
        if(connectServer(this.ip,this.port,this.user,this.pass)){
            try {
                /*从ftp下载文wf.txt到D:\\test.txt
                File file=new File("D:\\test.txt");
                FileOutputStream fos=new FileOutputStream(file);
                boolean result = ftpClient.retrieveFile("./wf.txt",fos);*/
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File fileItem:fileList){
                    fis=new FileInputStream(fileItem);
                    //String str=fileItem.getCanonicalPath();获取文件的全路径
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
            } catch (IOException e) {
                upload=false;
                logger.info("上传文件异常",e);
            }finally {
                if(fis != null){
                    fis.close();
                }
                ftpClient.disconnect();
            }
        }
        return upload;

    }

    private boolean connectServer(String ip,int port,String user,String pass){
        boolean isSuccess=true;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess=ftpClient.login(user,pass);
        } catch (IOException e) {
            logger.info("链接FTP服务器异常",e);
        }
        return isSuccess;
    }
}
