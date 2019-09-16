package com.example.jeong.real_mobile_project;

/**
 * Created by JuZero on 2017-12-11.
 */

import android.util.Log;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {

    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();
    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection(){ this.client = new OkHttpClient(); }


    /** 웹 서버로 요청을 한다. */
    public void requestWebServer(String parameter, String parameter2, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("login_id", parameter)
                .add("login_psw", parameter2)
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-52-79-171-92.ap-northeast-2.compute.amazonaws.com:8000/elections/mobile/login/")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public void signuprequestWebServer(String parameter, String parameter2, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("signup_id", parameter)
                .add("signup_psw", parameter2)
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-52-79-171-92.ap-northeast-2.compute.amazonaws.com:8000/elections/mobile/signup/")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }


}