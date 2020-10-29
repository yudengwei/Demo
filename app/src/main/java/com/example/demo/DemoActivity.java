package com.example.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;


/**
 * create by dengwei.yu.o
 * At 2020/9/15 2:28 PM
 **/
public class DemoActivity extends AppCompatActivity {

    private MyHandle handle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handle = new MyHandle();
        Button button = findViewById(R.id.mBtnCreate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ABiao","hello");
                    }
                },100);
            }
        });
    }

     class MyHandle extends Handler {

     }

    public void a() {
        Log.d("ABiao","hello");
    }
}
