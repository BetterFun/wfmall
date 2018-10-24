package com.cug.common;

/**
 * Created by Administrator on 2018/9/17 0017.
 */
public enum ResponseEnum {
    SUCCESS(1,"SUCCESS"),
    ERROER(2,"ERROR"),
    NEED_LOGIN(3,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(4,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    private ResponseEnum(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }
}
