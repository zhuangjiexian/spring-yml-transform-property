package com.elndu.transform.exception;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author elndu
 * @version 1.0, 2020/6/18 19:25
 * @since JDK 1.8
 */
public interface AbstractServiceException
{
    /**
     * 获取异常的状态码
     */
    Integer getCode();

    /**
     * 获取异常的提示信息
     */
    String getMessage();
}
