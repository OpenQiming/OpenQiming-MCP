package cn.com.ctsi.mcp.entity.dto;

import cn.com.ctsi.mcp.enums.ResultCodeEnum;
import lombok.Data;

@Data
public class ResultVo {
    // 状态码
    private int code;

    // 状态信息
    private String msg;

    // 返回对象
    private Object data;

    // 手动设置返回vo
    public ResultVo(int code, String msg) {
        this(code, msg, null);
    }

    // 手动设置返回vo
    public ResultVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 默认返回成功状态码，数据对象
    public ResultVo(Object data) {
        this(ResultCodeEnum.SUCCESS, data);
    }

    // 返回指定状态码，数据对象
    public ResultVo(ResultCodeEnum statusCode, Object data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }

    // 只返回状态码
    public ResultVo(ResultCodeEnum statusCode) {
        this(statusCode, null);
    }

}
