package com.example.wengturtle.arphone;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CalendarActivity extends Activity implements NetHandler {

    private GestureImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager wm = this.getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();

        imageView = new GestureImageView(this, windowWidth, windowHeight, R.drawable.calendar, 0, 1, ApplicationID.Calendar);
        setContentView(imageView);
    }

    public void onRecvData(String data) {
        Intent intent;

        switch(data) {
            case "book":
                intent = new Intent(CalendarActivity.this, BookActivity.class);
                startActivity(intent);
                break;
            case "browser":
                intent = new Intent(CalendarActivity.this, BrowserActivity.class);
                startActivity(intent);
                break;
            case "picture":
                intent = new Intent(CalendarActivity.this, PictureActivity.class);
                startActivity(intent);
                break;
            case "chat":
                intent = new Intent(CalendarActivity.this, ChatActivity.class);
                startActivity(intent);
                break;
            case "video":
                intent = new Intent(CalendarActivity.this, VideoActivity.class);
                startActivity(intent);
                break;
            case "map":
                intent = new Intent(CalendarActivity.this, MapActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Net.getInstance().delegate = this;
    }

    @Override
    protected void onPause() {
        super.onPause();

        Net.getInstance().delegate = null;
    }

}
