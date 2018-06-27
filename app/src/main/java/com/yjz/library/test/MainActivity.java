package com.yjz.library.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yjz.library.test.ui.PhotographDemoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_main_jump).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PhotographDemoActivity.class)));
    }
}
