package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over

                final SharedPreferences sharedPreferences = getSharedPreferences("Data" , Context.MODE_PRIVATE);
                final String type = sharedPreferences.getString("Email" , "");


                if(type.isEmpty())
                {
                    Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(i);
                    finish();
                }

                else
                {
                    startActivity(new Intent(MainActivity.this , InterestActivity.class));
                    finish();
                }



            }
        }, 2500);

    }
}