package com.example.wengturtle.arphone;

import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class LongPressHandler implements View.OnTouchListener {

    // 长按时间界限（超过此时间则为长按）
    private static final long LONGPRESS_TIME_THRESHOLD = 300;

    // 双击事件界限（两次点击间隔在此时间以内则为双击）
    private static final long DOUBLECLICK_TIME_THRESHOLD = 400;

    // 长按手抖容忍界限（距离超过此距离则不为长按）
    private static final float LONGPRESS_SHAKE_LIMIT = 40.0f;

    // 上下滑距离界限（超过此距离则为合法滑动）
    private static final float GESTURE_VERTICAL_THREOLD = 200.0f;
    private static final float GESTURE_LONG_VERTICAL_THREOLD = 600.0f;

    // 左右滑距离界限（超过此距离则为合法滑动）
    private static final float GESTURE_HORIZONTAL_THREOLD = 200.0f;

    // 判断上滑下滑时对左右移动的容忍界限
    private static final float GESTURE_VERTICAL_LIMIT = 200.0f;

    // 判断左滑右滑时对上下移动的容忍界限
    private static final float GESTURE_HORIZONTAL_LIMIT = 200.0f;

    private static final float GESTURE_BORDER_WIDTH = 150.0f;


    private Handler handler = new Handler();

    private class Point {
        public float x;
        public float y;

        float distance(Point p) {
            return (float)Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
        }

        float verticalDistance(Point p) {
            return Math.abs(p.y - y);
        }

        float horizontalDistance(Point p) {
            return Math.abs(p.x - x);
        }

        boolean leftOf(Point p) {
            return x < p.x;
        }

        boolean rightOf(Point p) {
            return x > p.x;
        }

        boolean above(Point p) {
            return y < p.y;
        }

        boolean under(Point p) {
            return y > p.y;
        }

        void print() {
            System.out.println(x + " " + y);
        }
    }

    private Point startPoint = new Point();
    private Point endPoint = new Point();


    private class LongPressRunnable implements Runnable {
        public boolean pressing = false;
        public boolean added = false;

        @Override
        public void run() {

            pressing = true;

            int border = onBorder(startPoint);
            if(border == 2) {
                Log.d("YueTing","left-edge-long-press-start");
                Net.getInstance().sendString("lelps");
            } else if(border == 1) {
                Log.d("YueTing","right-edge-long-press-start");
                Net.getInstance().sendString("relps");
            } else {
                Log.d("YueTing","long-press-start");
                Net.getInstance().sendString("lps");
            }


        }
    }

    private LongPressRunnable longPressRunnable = new LongPressRunnable();

    private class SingleClickRunnable implements Runnable {
        public GestureImageView view;
        public boolean finished;

        @Override
        public void run() {

            // 单击事件
            view.modifyImage();
            finished = true;
        }
    }

    private SingleClickRunnable singleClickRunnable = new SingleClickRunnable();

    private GestureImageView view;

    private int activityId;

    private int windowWidth;

    private int windowHeight;

    public LongPressHandler(GestureImageView view, int activityId, int windowWidth, int windowHeight) {
        this.view = view;
        this.activityId = activityId;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        singleClickRunnable.finished = true;
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                startPoint.x = event.getRawX();
                startPoint.y = event.getRawY();
                addLongPressCallback();


                break;
            case MotionEvent.ACTION_MOVE:
                endPoint.x = event.getRawX();
                endPoint.y = event.getRawY();
                if (!longPressRunnable.pressing) {
                    if (startPoint.distance(endPoint) > LONGPRESS_SHAKE_LIMIT) {
                        removeLongPressCallBack();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                endPoint.x = event.getRawX();
                endPoint.y = event.getRawY();
                if (longPressRunnable.pressing) {
                    handleGesture(startPoint, endPoint);
                    removeLongPressCallBack();
                }
                else if (singleClickRunnable.finished){
                    removeLongPressCallBack();
                    addSingleClickCallback();
                }
                else {
                    // Double Click
                    removeSingleClickCallback();
                    System.out.println("double click");
                }
                break;

            default:
                break;
        }
        return false;
    }

    private void addSingleClickCallback() {

        singleClickRunnable.view = view;
        singleClickRunnable.finished = false;
        //handler.postDelayed(singleClickRunnable, DOUBLECLICK_TIME_THRESHOLD);
        handler.post(singleClickRunnable);
    }

    private void removeSingleClickCallback() {
        handler.removeCallbacks(singleClickRunnable);
        singleClickRunnable.finished = true;
    }

    private void addLongPressCallback() {
        longPressRunnable.pressing = false;
        handler.postDelayed(longPressRunnable, LONGPRESS_TIME_THRESHOLD);
        longPressRunnable.added = true;

    }

    private void removeLongPressCallBack() {
        handler.removeCallbacks(longPressRunnable);
        longPressRunnable.added = false;
        longPressRunnable.pressing = false;
    }

    private int onBorder(Point p) {

        if(p.x < GESTURE_BORDER_WIDTH) {
            return 2;
        } else if(p.x > windowWidth - GESTURE_BORDER_WIDTH) {
            return 1;
        } else {
            return 0;
        }
    }

    private void handleGesture(Point startPoint, Point endPoint) {
        startPoint.print();
        endPoint.print();

        // 标记是否在边缘
        int border = onBorder(startPoint);

        // 判断上下滑
        if (startPoint.verticalDistance(endPoint) > GESTURE_LONG_VERTICAL_THREOLD &&
            startPoint.horizontalDistance(endPoint) < GESTURE_VERTICAL_LIMIT) {
            if (endPoint.above(startPoint)) {

                if(border == 2) {
                    Log.d("YueTing","left-edge-long-up");
                    Net.getInstance().sendString("lelu");

                    Log.d("YueTing","left-edge-long-press-end");
                    Net.getInstance().sendString("lelpe");
                } else if(border == 1) {
                    Log.d("YueTing","right-edge-long-up");
                    Net.getInstance().sendString("relu");

                    Log.d("YueTing","right-edge-long-press-end");
                    Net.getInstance().sendString("relpe");
                } else {
                    Log.d("YueTing","long-up");
                    Net.getInstance().sendString("lu," + view.getID().toString());

                    Log.d("YueTing","long-press-end");
                    Net.getInstance().sendString("lpe");
                }
            }
            else if (endPoint.under(startPoint)) {

                if(border == 2) {
                    Log.d("YueTing","left-edge-long-down");
                    Net.getInstance().sendString("leld");

                    Log.d("YueTing","left-edge-long-press-end");
                    Net.getInstance().sendString("lelpe");
                } else if(border == 1) {
                    Log.d("YueTing","right-edge-long-down");
                    Net.getInstance().sendString("reld");

                    Log.d("YueTing","right-edge-long-press-end");
                    Net.getInstance().sendString("relpe");
                } else {
                    Log.d("YueTing","long-down");
                    Net.getInstance().sendString("ld");

                    Log.d("YueTing","long-press-end");
                    Net.getInstance().sendString("lpe");
                }
            }
        }
        else if (startPoint.verticalDistance(endPoint) > GESTURE_VERTICAL_THREOLD &&
            startPoint.horizontalDistance(endPoint) < GESTURE_VERTICAL_LIMIT) {
            if (endPoint.above(startPoint)) {
                // 长按并向上短滑
                if(border == 2) {
                    Log.d("YueTing","left-edge-long-up");
                    Net.getInstance().sendString("lelu");

                    Log.d("YueTing","left-edge-long-press-end");
                    Net.getInstance().sendString("lelpe");
                } else if(border == 1) {
                    Log.d("YueTing","right-edge-long-up");
                    Net.getInstance().sendString("relu");

                    Log.d("YueTing","right-edge-long-press-end");
                    Net.getInstance().sendString("relpe");
                } else {
                    Log.d("YueTing","long-up");
                    Net.getInstance().sendString("lu," + view.getID().toString());

                    Log.d("YueTing","long-press-end");
                    Net.getInstance().sendString("lpe");
                }
            }
            else if (endPoint.under(startPoint)) {

                // 长按并向下短滑
                if(border == 2) {
                    Log.d("YueTing","left-edge-long-down");
                    Net.getInstance().sendString("leld");

                    Log.d("YueTing","left-edge-long-press-end");
                    Net.getInstance().sendString("lelpe");
                } else if(border == 1) {
                    Log.d("YueTing","right-edge-long-down");
                    Net.getInstance().sendString("reld");

                    Log.d("YueTing","right-edge-long-press-end");
                    Net.getInstance().sendString("relpe");
                } else {
                    Log.d("YueTing","long-down");
                    Net.getInstance().sendString("ld");

                    Log.d("YueTing","long-press-end");
                    Net.getInstance().sendString("lpe");
                }
            }

        }
        else if (startPoint.horizontalDistance(endPoint) > GESTURE_HORIZONTAL_THREOLD &&
            startPoint.verticalDistance(endPoint) < GESTURE_HORIZONTAL_LIMIT) {
            if (endPoint.leftOf(startPoint)) {

                if(border == 2) {
                    Log.d("YueTing","left-edge-long-press-end");
                    Net.getInstance().sendString("lelpe");
                } else if(border == 1) {
                    Log.d("YueTing","right-edge-long-left");
                    Net.getInstance().sendString("rell");

                    Log.d("YueTing","right-edge-long-press-end");
                    Net.getInstance().sendString("relpe");
                } else {
                    Log.d("YueTing","long-left");
                    Net.getInstance().sendString("ll," + view.getID().toString());

                    Log.d("YueTing","long-press-end");
                    Net.getInstance().sendString("lpe");
                }

            }
            else if (endPoint.rightOf(startPoint)) {

                if(border == 2) {
                    Log.d("YueTing","left-edge-long-right");
                    Net.getInstance().sendString("lelr");

                    Log.d("YueTing","left-edge-long-press-end");
                    Net.getInstance().sendString("lelpe");

                } else if(border == 1) {

                    Log.d("YueTing","right-edge-long-press-end");
                    Net.getInstance().sendString("relpe");
                } else {
                    Log.d("YueTing","long-right");
                    Net.getInstance().sendString("lr," + view.getID().toString());

                    Log.d("YueTing","long-press-end");
                    Net.getInstance().sendString("lpe");
                }

            }
        }
        else {
            //长按結束
            if(border == 2) {
                Log.d("YueTing","left-edge-long-press-end");
                Net.getInstance().sendString("lelpe");
            } else if(border == 1) {

                Log.d("YueTing","right-edge-long-press-end");
                Net.getInstance().sendString("relpe");
            } else {

                Log.d("YueTing","long-press-end");
                Net.getInstance().sendString("lpe");
            }

        }
    }

}
