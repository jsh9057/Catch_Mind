package com.example.jeong.real_mobile_project;

import android.content.Intent;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.net.Socket;
import java.util.LinkedList;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    String streammsg = "";
    TextView showText;
    Button connectBtn;
    Button Button_send;
    EditText ip_EditText;
    EditText port_EditText;
    EditText editText_massage;
    EditText nick_EditText;
    Handler msghandler;
    ScrollView showText_scroll;
    Drawboard drawboard;
    SocketClient client;
    ReceiveThread receive;
    SendThread send;
    Socket socket;
    PipedInputStream sendstream = null;
    PipedInputStream receivestream = null;
    Button Button_black;
    Button Button_blue;
    Button Button_red;
    Button Button_yellow;
    Button Button_clear;

    LinkedList<SocketClient> threadList;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawboard = (Drawboard) findViewById(R.id.Drawboard);
        showText = (TextView) findViewById(R.id.showText_TextView);
        editText_massage = (EditText) findViewById(R.id.editText_massage);
        Button_send = (Button) findViewById(R.id.Button_send);
        showText_scroll = (ScrollView) findViewById(R.id.showText_ScrollView);

        Button_black = (Button) findViewById(R.id.Button_black);
        Button_blue = (Button) findViewById(R.id.Button_blue);
        Button_red = (Button) findViewById(R.id.Button_red);
        Button_yellow = (Button) findViewById(R.id.Button_yellow);
        Button_clear = (Button) findViewById(R.id.Button_clear);

        Button_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               drawboard.colorState=0;
            }
        });
        Button_blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawboard.colorState=2;
            }
        });
        Button_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawboard.colorState=3;
            }
        });
        Button_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawboard.colorState=1;
            }
        });


        threadList = new LinkedList<MainActivity.SocketClient>();
        Intent intent = getIntent();
        nick = intent.getStringExtra("uid");

        client = new SocketClient("117.17.145.67",
                "7777",nick);
        threadList.add(client);
        client.start();
        editText_massage.setText("");
        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 1111) {
                    showText.append(hdmsg.obj.toString() + "\n");
                }
            }
        };
        Button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_massage.getText().toString() != null) {
                    send = new SendThread(socket, nick);
                    editText_massage.setText("");
                    send.start();
                }
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
        String port;
        String nick;
        Object_mop mop;

        OutputStream outputStream = null;
        BufferedReader br = null;

        private DataOutputStream output = null;

        public SocketClient(String ip, String port, String nick) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
            this.nick = nick;
        }

        public void run() {
            try {
                socket = new Socket(ip, Integer.parseInt(port));
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                receive.start();
                output.writeUTF(nick);
                drawboard.whodraw = nick;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ReceiveThread extends Thread {
        private Socket socket = null;
        DataInputStream input;
        Object_mop mop;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                while (input != null) {

                    String msg = input.readUTF();
                    if (msg != null) {
                        mop = new Object_mop(msg);
                        if(mop.kind.equals("msg")) {
                            Log.d(ACTIVITY_SERVICE, "test");
                            Message hdmsg = msghandler.obtainMessage();
                            hdmsg.what = 1111;
                            hdmsg.obj = mop.nick+" : "+mop.msg+"\n";
                            msghandler.sendMessage(hdmsg);
                            //Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());
                        }
                        else if(mop.kind.equals("point")&&!mop.nick.equals(drawboard.whodraw)){
                            drawboard.point_list.add(mop.getp());
                            drawboard.postInvalidate();
                        }
                        else if(mop.kind.equals("sys")){
                            Log.d(ACTIVITY_SERVICE, "test");
                            Message hdmsg = msghandler.obtainMessage();
                            hdmsg.what = 1111;
                            hdmsg.obj = "<< "+mop.msg+" >>\n";
                            msghandler.sendMessage(hdmsg);
                        }
                        else if (mop.kind.equals("permission")){
                            if(mop.nick.equals(nick))
                            drawboard.draw_permission=true;
                        }
                        else if(mop.kind.equals("state")){
                            drawboard.isstart=true;
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread {
        private Socket socket;
        String sendmsg = editText_massage.getText().toString();
        String nick;
        DataOutputStream output;
        Object_mop mop;
        boolean isreset=false;
        int old_pointlist_size=0;
        int now_pointlist_size;

        public SendThread(Socket socket, String nick) {
            this.socket = socket;
            this.nick = nick;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while(output!=null){
                    if(isreset){
                        now_pointlist_size = 0;
                        old_pointlist_size = 0;
                        drawboard.point_list.clear();
                        isreset = false;
                    }
                    now_pointlist_size=drawboard.point_list.size();
                    if(old_pointlist_size!=now_pointlist_size && !drawboard.draw_permission){
                        mop = new Object_mop(nick, "point", drawboard.point_list.get(drawboard.point_list.size()-1));
                        output.writeUTF(mop.toString());
                        output.flush();
                    }
                    old_pointlist_size=now_pointlist_size;
                    if(sendmsg !=null){
                        mop = new Object_mop(nick, "msg", sendmsg);
                        sendmsg=null;
                        output.writeUTF(mop.toString());
                        output.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
