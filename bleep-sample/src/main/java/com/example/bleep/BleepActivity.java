package com.example.bleep;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class BleepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleep);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new BleepFragment())
                .commit();
        }
    }
}
