package com.example.wengturtle.arphone;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ChatActivity extends Activity implements NetHandler {

    private GestureImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager wm = this.getWindowManager();
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();

        imageView = new GestureImageView(this, windowWidth, windowHeight, R.drawable.chat, 0, 1, ApplicationID.Chat);
        setContentView(imageView);
    }

    public void onRecvData(String data) {
        Intent intent;

        switch(data) {
            case "book":
                intent = new Intent(ChatActivity.this,BookActivity.class);
                startActivity(intent);
                break;
            case "browser":
                intent = new Intent(ChatActivity.this,BrowserActivity.class);
                startActivity(intent);
                break;
            case "picture":
                intent = new Intent(ChatActivity.this,PictureActivity.class);
                startActivity(intent);
                break;
            case "video":
                intent = new Intent(ChatActivity.this,VideoActivity.class);
                startActivity(intent);
                break;
            case "calendar":
                intent = new Intent(ChatActivity.this, CalendarActivity.class);
                startActivity(intent);
                break;
            case "map":
                intent = new Intent(ChatActivity.this, MapActivity.class);
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
