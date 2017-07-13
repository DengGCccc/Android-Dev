package com.dgc.testmvp.common;

/**
 * Created with Android Studio
 * User : Deng
 * Date: 17/7/13
 */
public interface IPresenter<V extends IView> {
    //IPresenter提供了一些基础方法，其实这些方法是对应Activity或Fragment的生命周期方法

    void initView(V view);
}