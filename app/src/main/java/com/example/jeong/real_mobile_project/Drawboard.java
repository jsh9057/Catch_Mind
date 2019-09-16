package com.example.jeong.real_mobile_project;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


/**
 * Drawboard
 *
 *  캔버스에 point를 point_list에 저장하고 point 와 point를 이어서 그린다.
 *  터치시 or 눌린상태로 이동할때 point_list에 point 가 add된다.
 *  터치시 or 눌린상태로 이동할때 오브젝트아웃풋스트림에 Object_mop(message or point) 객체를 넣어서 서버로 보낸다.
 *  draw_permission 이 false 일 경우에는 그림을 그릴 수 없다.
 *  draw_permission 이 false 일 경우에는 오브젝트인풋스트림에서 Object_mop를 받아와 그린다.
 *
 */

public class Drawboard extends View{
    Socket socket;
    ArrayList<Point> point_list;
    static final int RED_STATE = 0;
    static final int BLUE_STATE = 1;
    static final int YELLOW_STATE = 2;
    static final int BLACK_STATE = 3;
    int now_pointlist_size;
    int old_pointlist_size;

    DataOutputStream output;
    ObjectOutputStream point_outputstream;

    DataInputStream input;
    ObjectInputStream point_inputstream;

    String whodraw;
    boolean draw_permission=false;
    boolean isstart=false;

    int colorState = 0;
    Paint[] paintList = new Paint[4];

    public Drawboard(Context context) {
        super(context);
        init();
    }


    public Drawboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        point_list = new ArrayList<Point>();

        Paint blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(3);
        blackPaint.setAntiAlias(true);

        Paint bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        bluePaint.setStrokeWidth(3);
        bluePaint.setAntiAlias(true);

        Paint redPaint = new Paint();
        redPaint.setColor(Color.RED);
        redPaint.setStrokeWidth(3);
        redPaint.setAntiAlias(true);

        Paint yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setStrokeWidth(3);
        yellowPaint.setAntiAlias(true);

        paintList[0] = blackPaint;
        paintList[1] = redPaint;
        paintList[2] = bluePaint;
        paintList[3] = yellowPaint;
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        for (int i = 0; i < point_list.size(); i++) {
            Point p = point_list.get(i);
            if (!(p.isStart)) {
                canvas.drawLine(point_list.get(i - 1).x,
                        point_list.get(i - 1).y,
                        point_list.get(i).x,
                        point_list.get(i).y,
                        paintList[point_list.get(i).colorState]);
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (draw_permission&&isstart) {
            int eventX = (int) event.getX();
            int eventY = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Point p = new Point(eventX, eventY, true, colorState, whodraw);
                    point_list.add(p);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    Point p2 = new Point(eventX, eventY, false, colorState, whodraw);
                    point_list.add(p2);
                    try{ Thread.sleep(5);}
                    catch(Exception e){}
                    invalidate();
                    break;
            }
            return true;
        }
        return false;
    }
}