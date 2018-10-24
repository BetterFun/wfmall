package com.cug.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Administrator on 2018/10/8 0008.
 */
public class DateTimeUtil {
    public static Date strToDate(String dateTimeStr,String formateStr){
        if(StringUtils.isBlank(dateTimeStr) || StringUtils.isBlank(formateStr)){
            return null;
        }
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(formateStr);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date,String formateStr){
        if(date==null){
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formateStr);
    }
}
