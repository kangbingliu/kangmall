package cn.kang.mall.common;

/**
 *  定义返回字段
 * @author mi
 *
 */
public enum ResponseCode {

    SUCCESS(200,"SUCCESS"),
    ERROR(400,"ERROR"),
    NEED_LOGIN(40001,"NEED_LOGIN"),
    ILLEGAL_ARGUMENT(40002,"ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;


    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }
    public String getDesc(){
        return desc;
    }

}
