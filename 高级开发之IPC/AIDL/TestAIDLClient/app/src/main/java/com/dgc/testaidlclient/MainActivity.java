package com.dgc.testaidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dgc.aidl.ICalculateInterface;

import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "CalculateClient";

    private ICalculateInterface mService;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            logE("disconnect service");
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            logE("connect service");
            mService = ICalculateInterface.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button btn = new Button(this);

        setContentView(btn);

        Intent intent = new Intent("com.dgc.aidl.CalculateService");
        final Intent eintent = new Intent(createExplicitFromImplicitIntent(this,intent));
        bindService(eintent, mServiceConnection, Context.BIND_AUTO_CREATE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logE("开始远程运算");
                try {
                    double num1 = 2.0d;
                    double num2 = 5.0d;
                    String answer = "计算结果：" + mService.doCalculate(num1, num2);

                    System.out.println(answer);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void logE(String str) {
        Log.e(TAG, "--------" + str + "--------");
    }

    // 5.0以上的机子报错-----IllegalArgumentException: Service Intent must be explicit
    // 经过查找相关资料，发现是因为Android5.0中service的intent一定要显性声明。 隐式调用时, Intent 需要用以下函数转为显式
    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }

        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);

        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);

        // Set the component to be explicit
        explicitIntent.setComponent(component);

        return explicitIntent;
    }
}
