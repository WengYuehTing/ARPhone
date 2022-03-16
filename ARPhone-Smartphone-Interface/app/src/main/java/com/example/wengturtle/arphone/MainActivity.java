package com.example.wengturtle.arphone;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void OnButtonClicked(View v) {
        EditText ipEditText = findViewById(R.id.editText_ip);
        String ip = ipEditText.getText().toString();

        EditText portEditText = findViewById(R.id.editText_port);
        String port = portEditText.getText().toString();

        if(Net.getInstance() != null) {
            Net.getInstance().close();
        }

        Net.getInstance(ip, Integer.parseInt(port)).connect();

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

}
