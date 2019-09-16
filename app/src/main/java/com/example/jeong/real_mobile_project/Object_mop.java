package com.example.jeong.real_mobile_project;

import java.io.Serializable;

/**
 * Object  message or point
 * <p>
 * 오브젝트인풋아웃풋스트림에 넣을 객체
 * 오브젝트를 전송하는 클라이언트의 닉네임정보와 오브젝트의 종류
 * 오브젝트가 message 일 경우
 * 오브젝트가 point 일 경우
 * 오브젝트가 sysmsg 일 경우
 */
public class Object_mop {
    public String nick;
    public String kind;
    public String msg ;
    public Point p;

    public Object_mop(String nick, String kind, String msg) {
        this.nick = nick;
        this.kind = kind;
        this.msg = msg;
    }
    public Object_mop(String nick,String kind, Point point){
        this.nick = nick;
        this.kind = kind;
        this.p = point;
    }
    public Object_mop(String msg){
        String[] toobjectmop = msg.split("/");
        kind = toobjectmop[0];
        nick = toobjectmop[1];
        if (kind.equals("msg")) {
            this.msg = toobjectmop[2];
        }
        if (kind.equals("point")) {
            p = new Point();

            p.setStart(Boolean.parseBoolean(toobjectmop[2]));
            p.setWho_draw(toobjectmop[3]);
            p.setX(Integer.parseInt(toobjectmop[4]));
            p.setY(Integer.parseInt(toobjectmop[5]));
            p.setColorState(Integer.parseInt(toobjectmop[6]));
        }
        if(kind.equals("sys")){
            this.msg =  msg = toobjectmop[2];
        }
    }

    public String toString() {
        if(kind.equals("msg"))
            return kind + "/" + nick + "/" + msg;
        if(kind.equals("point"))
            return kind + "/" + nick + "/" + p.isStart +"/" + p.who_draw +"/"+p.x +"/"+ p.y+"/"+p.colorState;
        if(kind.equals("quiz"))
            return kind;
        return kind;
    }

    public void setObject_mop(String str) {
        String[] toobjectmop = str.split("/");
        kind = toobjectmop[0];
        nick = toobjectmop[1];
        if (kind.equals("msg")) {

            msg = toobjectmop[2];
            System.out.println(kind);
        }
        if (kind.equals("point")) {
            p = new Point();
            p.setColorState(0);
            p.setStart(Boolean.parseBoolean(toobjectmop[2]));
            p.setWho_draw(toobjectmop[3]);
            p.setX(Integer.parseInt(toobjectmop[4]));
            p.setY(Integer.parseInt(toobjectmop[5]));
            p.setColorState(Integer.parseInt(toobjectmop[6]));
        }
    }
    public Point getp (){
        return this.p;
    }
}