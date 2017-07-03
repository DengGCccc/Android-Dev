package com.dgc.testretrofitrxjava.web;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NetworkBaseActivity extends AppCompatActivity {
    public ProgressDialog pd;
    private final long RETRY_TIMES = 5;
    private boolean mIsShowLoading = true;


    public <T> ObservableTransformer<T,T> setThread(boolean isShowLoading) {
        mIsShowLoading = isShowLoading;
        pd = new ProgressDialog(this);

        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.retry(RETRY_TIMES)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                if (NetworkUtil.isNetworkAvailable(NetworkBaseActivity.this)) {
                                    if (mIsShowLoading) {
                                        if(pd != null && !pd.isShowing()){
                                            pd.show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(NetworkBaseActivity.this, "网络连接异常，请检查网络", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }
}
