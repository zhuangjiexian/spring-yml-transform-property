package com.elndu.transform.enums;

import com.elndu.transform.exception.AbstractServiceException;

public enum CommonExceptionEnum implements AbstractServiceException
{
    MERGE_TEXT_IS_NULL(1702005,"需合并内容为空"),
    PROPERTY_IS_NULL(1702005,"PROPERTY为空"),
    YAML_IS_NULL(1702004,"YML为空"),
    PARAM_IS_NULL(1702003,"必要参数为空"),
    YAML_COVERT_ERROR(1702002, "yaml格式不符合规则"),
    COVERT_TO_YAML_MAP_FAIL(7020001, "转化为yaml失败！"),
    SYSTEM_ERROR(11000, "系统错误，请联系管理员"),
    TOKEN_HAS_EXPIRED(11010, "token已过期"),
    REMOTE_SERVICE_NULL(11020, "找不到服务"),
    REMOTE_SERVICE_TIMEOUT(11021, "请求超时"),
    SERVICE_ERROR(110030, "服务内部错误"),
    NO_FOUNT(11031, "访问路径不存在"),
    IO_ERROR(11050, "读取IO异常"),
    NO_LOGIN(11060, "没有登录或者登录超时"),
    OBJECT_IS_NULL(11070, "对象不存在");

    CommonExceptionEnum(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode()
    {
        return code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
