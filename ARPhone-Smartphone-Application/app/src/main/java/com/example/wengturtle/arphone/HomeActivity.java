package com.example.wengturtle.arphone;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class HomeActivity extends Activity {

    private void switchToVideoActivity() {
        Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
        startActivity(intent);
    }

    private void switchToBookActivity() {
        Intent intent = new Intent(HomeActivity.this, BookActivity.class);
        startActivity(intent);
    }

    private void switchToBrowserActivity() {
        Intent intent = new Intent(HomeActivity.this, BrowserActivity.class);
        startActivity(intent);
    }

    private void switchToPictureActivity() {
        Intent intent = new Intent(HomeActivity.this, PictureActivity.class);
        startActivity(intent);
    }

    private void switchToChatActivity() {
        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    private void switchToMapActivity() {
        Intent intent = new Intent(HomeActivity.this, MapActivity.class);
        startActivity(intent);
    }

    private void switchToCalendarActivity() {
        Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        WindowManager wm = this.getWindowManager();
        final int windowWidth = wm.getDefaultDisplay().getWidth();

        View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        float x = motionEvent.getX();
                        float y = motionEvent.getY();
                        int block = (int)x / (windowWidth / 5);
                        if (y < 320) {
                            if (block == 0) {
                                //switchToVideoActivity();
                            }
                            else if (block == 1) {
                                switchToBookActivity();
                            }
                            else if (block == 2) {
                                switchToBrowserActivity();
                            }
                            else if (block == 3) {
                                switchToPictureActivity();
                            }
                            else if (block == 4) {
                                switchToChatActivity();
                            }
                        }
                        else {
                            if (block == 0) {
                                switchToMapActivity();
                            }
                            else if (block == 1) {
                                switchToCalendarActivity();
                            }
                            else if (block == 2) {
                                switchToVideoActivity();
                            }
                        }

                        break;
                    default:
                        break;
                }
                return false;
            }
        };
        ImageView desktop = findViewById(R.id.desktop);
        desktop.setOnTouchListener(listener);
    }
}
