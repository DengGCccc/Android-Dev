package com.dgc.testmvp;

/**
 * Created with Android Studio
 * User : Deng
 * Date: 17/7/13
 */
public class LoginManager {

    private static LoginManager mLoginManager;

    public static LoginManager getInstance() {
        if (null == mLoginManager)
            mLoginManager = new LoginManager();

        return mLoginManager;
    }

    public interface LoginListener {
        void onSuccessLogin(UserEntity user);

        void onFailedLogin(String errMsg);
    }

    //登录方法
    public void login(String userName, String password, final LoginListener loginListener) {

//        //假设HttpClient是一个请求网络服务的类
//        HttpClient.getInstance().login(userName, password,
//                new ResponseListener() {
//                    public void failed(String msg) {
//
//                        loginListener.onFailedLogin(msg);
//                    }
//
//                    public void success(Response response) {
//                        //假设UserParse类已经存在，主要用来从response中解析UserEntity
//                        UserEntity userEntity = UserParse(response.getContent());
//                        //假设userDatabase是数据库存储类
//                        userDatabase.store(userEntity);
//                        //还可以把userEntity存入内存中，这得根据业务需求进行处理
//                        loginListener.onSuccessLogin(userEntity);
//                    }
//                });
    }
}