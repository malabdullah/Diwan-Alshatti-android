package com.malabdullah.alshattidiwan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashThread = new Thread(){
            @Override
            public void run() {
                try {

                    sleep(4000);

                    Intent mainIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(mainIntent);

                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splashThread.start();
    }
}
