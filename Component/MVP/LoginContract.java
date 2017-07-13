package com.dgc.testmvp;

import com.dgc.testmvp.common.IPresenter;
import com.dgc.testmvp.common.IView;

/**
 * Created with Android Studio
 * User : Deng
 * Date: 17/7/13
 */
public interface LoginContract {
    //登录的条约接口，分别定义了登录view的一些方法，和登录presenter的一些方法

    //需要view层来实现的登录view接口，IView是所有view的基类
    interface ILoginView extends IView {

        void onShowSuccessLoginView(UserEntity userEntity);

        void onShowFailedLoginView(String failed);

        void showLoginingView();

        void dissLoginingView();
    }

    //定义了登录presenter的一些方法，IPresenter是所有Presenter的基类
    interface ILoginPresenter extends IPresenter<ILoginView> {
        void login(String name, String password);
    }
}