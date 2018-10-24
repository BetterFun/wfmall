package com.cug.util;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/10/23 0023.
 */
public class BigDecimalUtil {

    public BigDecimalUtil() {
    }

    public static BigDecimal add(double a1,double a2){
        BigDecimal b1=new BigDecimal(Double.toString(a1));
        BigDecimal b2=new BigDecimal(Double.toString(a2));
        return b1.add(b2);
    }

    public static BigDecimal sub(double a1,double a2){
        BigDecimal b1=new BigDecimal(Double.toString(a1));
        BigDecimal b2=new BigDecimal(Double.toString(a2));
        return b1.subtract(b2);
    }

    public static BigDecimal mul(double a1,double a2){
        BigDecimal b1=new BigDecimal(Double.toString(a1));
        BigDecimal b2=new BigDecimal(Double.toString(a2));
        return b1.multiply(b2);
    }

    public static BigDecimal div(double a1,double a2){
        BigDecimal b1=new BigDecimal(Double.toString(a1));
        BigDecimal b2=new BigDecimal(Double.toString(a2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);  //四舍五入，保留两位小数
    }
}
