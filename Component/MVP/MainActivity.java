package com.dgc.testmvp;

import com.dgc.testmvp.common.BaseActivity;
import com.dgc.testmvp.common.IPresenter;

public class MainActivity extends BaseActivity implements LoginContract.ILoginView {

    private LoginPresenter mLoginPresenter = new LoginPresenter();

    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    protected IPresenter[] getPresenters() {
        return new IPresenter[]{mLoginPresenter};
    }

    //初始化presenters，
    protected void onInitPresenters() {
        mLoginPresenter.initView(this);
    }

    public void initView() {

//        mLoginPresenter.login(name, password);
    }

    @Override
    public void onShowSuccessLoginView(UserEntity userEntity) {

    }

    @Override
    public void onShowFailedLoginView(String failed) {

    }

    @Override
    public void showLoginingView() {

    }

    @Override
    public void dissLoginingView() {

    }

}