package com.example.jeong.real_mobile_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PipedInputStream;
import java.net.Socket;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Jeong on 2017-12-11.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private HttpConnection httpConn = HttpConnection.getInstance();
    EditText idinput;
    EditText pswinput;
    Button loginbtn;
    Button signupbtn;
    TextView alram ;
    Socket socket;
    PipedInputStream sendstream = null;
    PipedInputStream receivestream = null;

    String loginreult ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        alram = (TextView) findViewById(R.id.alram);
        idinput = (EditText) findViewById(R.id.idinput);
        pswinput = (EditText) findViewById(R.id.pswInput);
        loginbtn = (Button) findViewById(R.id.loginButton);
        signupbtn = (Button) findViewById(R.id.signupButton);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                alram.setText(loginreult);

                if(loginreult.equals("ok")){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                String uid = idinput.getText().toString();
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

    }
        private void sendData() {
// 네트워크 통신하는 작업은 무조건 작업스레드를 생성해서 호출 해줄 것!!
            new Thread() {
                    public void run() {
// 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                    httpConn.requestWebServer(idinput.getText().toString(), pswinput.getText().toString(), callback);
                }
            }.start();
            ;
        }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG, "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String body = response.body().string();
            Log.d(TAG, "서버에서 응답한 Body:" + body);
            loginreult=body;
        }
    };
}