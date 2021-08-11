package com.glodon.groupsix.seckillprocess.utils;

/**
 * @author:zhuym
 * @date: 2019/8/12
 * @time: 14:05
 */
public class CodeMsg {
    private int code;
    private String msg;
    //通用异常
    public static CodeMsg SUCCESS=new CodeMsg(200,"success");
    public static CodeMsg SERVER_ERROR=new CodeMsg(500100,"服务端异常!");
    public static CodeMsg BIND_ERROR=new CodeMsg(500101,"参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEAGAL=new CodeMsg(500102,"非法请求!");
    public static CodeMsg MIAOSHA_FAIL=new CodeMsg(500103,"秒杀失败!");
    public static CodeMsg ACCESS_LIMIT=new CodeMsg(500104,"达到访问限制次数，访问太频繁!");
    public static CodeMsg EDIT_ERROR=new CodeMsg(500105,"已上下架商品不允许编辑！");
    //登录模块异常
    public static CodeMsg MOBILE_ERROR=new CodeMsg(500213,"手机号格式错误");
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(500213,"请输入手机号");
    public static CodeMsg PASSWORD_ERROR=new CodeMsg(500215,"密码错误!");
    //订单模块异常
    public static CodeMsg ORDER_NOT_EXIST=new CodeMsg(500410,"订单不存在!");
    //秒杀模块异常
    public static CodeMsg MIAOSHA_OVER_ERROR=new CodeMsg(500500,"商品秒杀完毕，库存不足!");
    public static CodeMsg REPEATE_MIAOSHA=new CodeMsg(500501,"不能重复秒杀!");
    public static CodeMsg GOOD_IS_EXIST=new CodeMsg(500502,"该商品已存在，不能再次添加!");
    public static CodeMsg FILE_IS_NULL=new CodeMsg(500503,"文件为空！");
    public static CodeMsg STOCK_IS_NULL=new CodeMsg(500504,"库存为0！");
    public static CodeMsg IS_NOT_INEFFECTIVE=new CodeMsg(500505,"活动未生效！");
    public static CodeMsg IS_EXPIRED=new CodeMsg(500506,"活动已过期！");

    public CodeMsg(int code,String msg) {
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    //返回一个带参数的错误码
    public CodeMsg fillArgs(Object...args) {//变参
        int code=this.code;
        //message是填充了参数的message
        String message=String.format(this.msg, args);
        return new CodeMsg(code,message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
