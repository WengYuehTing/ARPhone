package com.example.wengturtle.arphone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BrowserActivity extends Activity implements NetHandler{

    private GestureImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager wm = this.getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();

        RelativeLayout layout = new RelativeLayout(this);



        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.setMargins(24, 60, 0, 0);
        params.width = 600;
        params.height = 110;

        EditText input = new EditText(this);
        input.setText("input");
        input.setLayoutParams(params);
        input.setSingleLine(true);
        input.setBackground(null);

        View cover = new View(this);
        RelativeLayout.LayoutParams coverParams = new RelativeLayout.LayoutParams(-2, -2);
        coverParams.setMargins(190, 690, 0, 0);
        coverParams.width = 600;
        coverParams.height = 70;
        cover.setLayoutParams(coverParams);
        cover.setBackgroundColor(Color.WHITE);

        imageView = new GestureImageView(this, windowWidth, windowHeight, R.drawable.web, 0, 1, ApplicationID.Browser);
        layout.addView(imageView);
        layout.addView(cover);
        layout.addView(input);


        setContentView(layout);
    }

    public void onRecvData(String data) {
        Intent intent;
        switch(data) {
            case "book":
                intent = new Intent(BrowserActivity.this,BookActivity.class);
                startActivity(intent);
                break;
            case "chat":
                intent = new Intent(BrowserActivity.this,ChatActivity.class);
                startActivity(intent);
                break;
            case "picture":
                intent = new Intent(BrowserActivity.this,PictureActivity.class);
                startActivity(intent);
                break;
            case "video":
                intent = new Intent(BrowserActivity.this,VideoActivity.class);
                startActivity(intent);
                break;
            case "calendar":
                intent = new Intent(BrowserActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case "map":
                intent = new Intent(BrowserActivity.this, MapActivity.class);
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
