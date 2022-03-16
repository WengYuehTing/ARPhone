package com.example.wengturtle.arphone;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

enum ApplicationID {
    Video(1),
    Book(2),
    Browser(3),
    Picture(4),
    Chat(5),
    Map(6),
    Calendar(7);

    private int value;

    ApplicationID(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}



public class GestureImageView extends ImageView {

    int resId2;
    private ApplicationID id;
    private LongPressHandler handler;
    private SensorHandler sensorHandler;


    public GestureImageView(Context context, int windowWidth, int windowHeight, int resId, int resId2, int activityId, ApplicationID id) {
        super(context);
        this.resId2 = resId2;
        this.id = id;

        setScaleType(ImageView.ScaleType.FIT_XY);
        setImageResource(resId);
        setClickable(true);

        handler = new LongPressHandler(this, activityId, windowWidth, windowHeight);
        setOnTouchListener(handler);

        sensorHandler = new SensorHandler(context);

    }

    public void modifyImage() {
        if (resId2 != 0) {
            setImageResource(resId2);
        }
    }

    public ApplicationID getID() {
        return id;
    }

}
