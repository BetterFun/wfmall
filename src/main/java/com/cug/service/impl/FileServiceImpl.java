package com.cug.service.impl;

import com.cug.service.IFileService;
import com.cug.util.FtpUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Administrator on 2018/10/8 0008.
 */
@Service("iFileServiceImpl")
public class FileServiceImpl implements IFileService{

    private static Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){

        String fileName= file.getOriginalFilename();
        //文件的扩展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        //新文件
        String targetFileName= UUID.randomUUID().toString()+"."+fileExtensionName;

        logger.info("文件上传开始，上传文件的文件名：{}，上传路径：{}，新文件名：{}",fileName,path,targetFileName);

        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,targetFileName);
        try {
            file.transferTo(targetFile);
            //将文件上传到ftp服务器
            FtpUtil.uploadFile(Lists.newArrayList(targetFile));

            //删除upload目录下的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.info("文件上传异常",e);
        }

        return targetFileName;
    }
}
