package com.byl.mvp.api;

/**
 * data为一个对象时使用
 *
 * @param <T>
 */
public class HttpResult<T> {
    /**
     * 错误码
     */
    public int errorCode;
    /**
     * 错误信息
     */
    public String errorMessage;
    /**
     * 返回消息主体
     */
    public T result;

}
