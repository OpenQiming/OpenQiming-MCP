package cn.com.ctsi.mcp.enums;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {

    SUCCESS(1000, "请求成功"),
    FAILED(1001, "请求失败");


    private int code;
    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
