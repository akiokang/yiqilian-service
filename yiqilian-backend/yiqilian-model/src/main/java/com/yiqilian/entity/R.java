package com.yiqilian.entity;

public class R<T> {
    private String code;
    private String message;
    private T data;

    // 构造函数
    public R(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 默认成功响应
    public static R success() {
        return new R("0000", "操作成功", null);
    }

    // 自定义成功响应（带数据）
    public static R success(Object data) {
        return new R("0000", "操作成功", data);
    }

    // 默认失败响应
    public static R error() {
        return new R("9999", "操作失败", null);
    }

    // 自定义失败响应（带消息）
    public static R error(String message) {
        return new R("9999", message, null);
    }

    // 自定义失败响应（带状态码和消息）
    public static R error(String code, String message) {
        return new R(code, message, null);
    }

    // 自定义响应（全参数）
    public static R custom(String code, String message, Object data) {
        return new R(code, message, data);
    }

    // Getter 和 Setter 方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}