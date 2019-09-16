package com.example.jeong.real_mobile_project;

import java.io.Serializable;

import java.io.Serializable;

/**
 * Created by Jeong on 2017-11-07.
 */

public class Point {

    public int x,y;
    public boolean isStart = false;
    public int colorState;
    public String who_draw=null;

    public Point() { }

    public Point(int x, int y, boolean isStart, String whodraw){
        this.x=x;
        this.y=y;
        this.colorState=3;
        this.isStart=isStart;
        this.who_draw=whodraw;
    }

    public Point(int x,int y,boolean isStart, int colorState, String whodraw){
        this.x=x;
        this.y=y;
        this.isStart=isStart;
        this.colorState= colorState;
        this.who_draw=whodraw;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    public int getColorState() {
        return colorState;
    }

    public void setColorState(int colorState) {
        this.colorState = colorState;
    }

    public String getWho_draw() {
        return who_draw;
    }

    public void setWho_draw(String who_draw) {
        this.who_draw = who_draw;
    }

    @Override
    public String toString() {
        return "Point [x=" + x + ", y=" + y + ", isStart=" + isStart + ", colorState=" + colorState + ", who_draw="
                + who_draw + "]";
    }




}
