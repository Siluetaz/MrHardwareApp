package com.example.mrhardwareapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, InicioClienteActivity.class);
                startActivity(i);
                finish();
            }
        };

        Timer time = new Timer();
        time.schedule(tarea, 1400);

    }
}