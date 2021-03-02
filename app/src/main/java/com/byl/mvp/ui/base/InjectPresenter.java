package com.byl.mvp.ui.base;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title :
 * @Author : BaiYuliang
 * @Date :
 * @Desc : 通过注解方式得到Presenter，
 * 支持一对多（一个Activity对应多个Presenter），与Activity完全解耦
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectPresenter {
}
