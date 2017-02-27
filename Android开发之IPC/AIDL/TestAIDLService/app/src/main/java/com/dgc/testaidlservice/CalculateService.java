package com.dgc.testaidlservice;

/**
 * Created by deng on 15/12/15.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.dgc.aidl.ICalculateInterface;

public class CalculateService extends Service {

    private static final String TAG = "CalculateService";

    @Override
    public IBinder onBind(Intent arg0) {
        logE("onBind()");
        return mBinder;
    }

    @Override
    public void onCreate() {
        logE("onCreate()");
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        logE("onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        logE("onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        logE("onDestroy()");
        
        super.onDestroy();
    }

    private static void logE(String str) {
        Log.e(TAG, "--------" + str + "--------");
    }

    private final ICalculateInterface.Stub mBinder = new ICalculateInterface.Stub() {

        @Override
        public double doCalculate(double a, double b) throws RemoteException {
            Log.e("Calculate", "远程计算中");
            return a + b;
        }
    };
}