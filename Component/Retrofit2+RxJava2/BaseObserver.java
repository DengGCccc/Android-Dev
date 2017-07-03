package com.dgc.testretrofitrxjava.web;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<BaseEntity<T>> {
    private Context mContext;
    private ProgressDialog mDialog;
    private Disposable mDisposable;
    private final String SUCCESS_CODE = "1";

    public BaseObserver(Context context, ProgressDialog dialog) {
        mContext = context;

        if (null != dialog) {
            mDialog = dialog;

            mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mDisposable.dispose();
                }
            });
        }
    }

    public BaseObserver(Context context) {
        mContext = context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(BaseEntity<T> value) {
        if (value.getRt().equals(SUCCESS_CODE)) {
            T t = value.getData();
            onSuccess(t);
        } else {
            onError(value.getRt(), value.getRd());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e("gesanri", "error:" + e.toString());

        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }

        Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete() {
        Log.i("gesanri", "onComplete");

        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    public abstract void onSuccess(T t);

    public void onError(String code, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}
