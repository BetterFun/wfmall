package com.cug.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by Administrator on 2018/10/8 0008.
 */
public class PropertiesUtil {

    private static Logger logger= LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties props;

    static {
        String fileName="wfmall.properties";
        props=new Properties();
        InputStream in=PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            props.load(new InputStreamReader(in));
        } catch (IOException e) {
            logger.info("配置文件加载失败",e);
        }
    }

    public static String getValue(String key){
        String value=props.getProperty(key.trim());
        if(value.trim()==null){
            return null;
        }else {
            return value.trim();
        }
    }
}
