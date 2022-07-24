package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity3 extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    TextView profile;
    TextView interests;
    TextView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);


        bottomNavigationView = findViewById(R.id.navigationButton);

        bottomNavigationView.setSelectedItemId(R.id.setting);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.interest :
                        startActivity(new Intent(getApplicationContext() , HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.setting:
                        return true;


                    case R.id.find:
                        startActivity(new Intent(getApplicationContext() , HomeActivity2.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home :
                        startActivity(new Intent(getApplicationContext() , MyChatActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.request :
                        startActivity(new Intent(getApplicationContext() , RequestActivity.class));
                        overridePendingTransition(0 ,0);
                        return true;
                }

                return false;
            }
        });



        profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity3.this, SettingsActivity.class));
            }
        });


        interests = findViewById(R.id.interests);

        interests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity3.this, InterestActivity.class);
                startActivity(intent);
            }
        });


        logout = findViewById(R.id.logout);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(HomeActivity3.this, R.style.Dialogue);

                dialog.setContentView(R.layout.dialog_layout);
                dialog.show();

                TextView yesbtn;
                TextView nobtn;

                yesbtn = dialog.findViewById(R.id.yes);
                nobtn = dialog.findViewById(R.id.no);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final SharedPreferences sharedPreferences = getSharedPreferences("Data" , Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity3.this, RegistrationActivity.class));
                        finish();
                    }
                });

                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }


    public void onBackPressed()
    {
        Intent intent = new Intent(HomeActivity3.this , MyChatActivity.class);
        startActivity(intent);
        finish();

    }

}