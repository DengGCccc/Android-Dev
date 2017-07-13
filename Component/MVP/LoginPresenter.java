package com.dgc.testmvp;

import com.dgc.testmvp.LoginContract;
import com.dgc.testmvp.LoginManager;
import com.dgc.testmvp.UserEntity;

/**
 * Created with Android Studio
 * User : Deng
 * Date: 17/7/13
 */
public class LoginPresenter implements LoginContract.ILoginPresenter {

    private LoginContract.ILoginView mLoginView;
    private LoginManager mLoginManager = LoginManager.getInstance();

    @Override
    public void initView(LoginContract.ILoginView view) {
        mLoginView = view;
        mLoginView.initView();
    }

    public void login(String name, String password) {

        if (!name.isEmpty() && !password.isEmpty()) {
            mLoginView.showLoginingView();
            mLoginManager.login(name, password, new LoginManager.LoginListener() {
                @Override
                public void onSuccessLogin(UserEntity user) {
                    mLoginView.onShowSuccessLoginView(user);
                    mLoginView.dissLoginingView();
                }

                @Override
                public void onFailedLogin(String errMsg) {
                    mLoginView.onShowFailedLoginView(errMsg);
                    mLoginView.dissLoginingView();
                }
            });
        } else {
            mLoginView.onShowFailedLoginView("pw error balabala");
        }
    }
}