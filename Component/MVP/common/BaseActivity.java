package com.dgc.testmvp.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with Android Studio
 * User : Deng
 * Date: 17/7/13
 */
public abstract class BaseActivity extends FragmentActivity {

    private Set<IPresenter> mAllPresenters = new HashSet<>();

    /**
     * 获取layout的id，具体由子类实现
     *
     * @return
     */
    protected abstract int getLayoutResId();

    /**
     * 需要子类来实现，获取子类的IPresenter，一个activity有可能有多个IPresenter
     */
    protected abstract IPresenter[] getPresenters();

    //初始化presenters，
    protected abstract void onInitPresenters();

    /**
     * 从intent中解析数据，具体子类来实现
     *
     * @param argIntent
     */
    protected void parseArgumentsFromIntent(Intent argIntent) {
    }

    private void addPresenters() {

        IPresenter[] presenters = getPresenters();
        if (presenters != null) {
            for (int i = 0; i < presenters.length; i++) {
                mAllPresenters.add(presenters[i]);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        if (getIntent() != null) {
            parseArgumentsFromIntent(getIntent());
        }
        addPresenters();

        onInitPresenters();
    }
}