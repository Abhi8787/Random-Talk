package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MyChatActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    HashSet<String> hs;
    MyChatAdapter adapter;
    ArrayList<Users> usersArrayList;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    RecyclerView recycler;
    ImageView logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);


      auth = FirebaseAuth.getInstance();
      database = FirebaseDatabase.getInstance();
      hs = new HashSet<>();


      progressBar1 = findViewById(R.id.progressBar);

      database.getReference().child("mychats").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {

              for(DataSnapshot dataSnapshot : snapshot.getChildren())
              {
                  String str = dataSnapshot.getValue(String.class);

                  String[] parts = str.split("@");
                  String temp1 = parts[0];
                  String temp2 = parts[1];

                  hs.add(temp1);
              }
              progressBar1.setVisibility(View.GONE);
              adapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });



      usersArrayList = new ArrayList<>();
      progressBar2 = findViewById(R.id.progressBar);

        DatabaseReference reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                Log.d("fire",usersArrayList.size()+"UserArrayList Size ");
                Collections.sort(usersArrayList);
                progressBar2.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bottomNavigationView = findViewById(R.id.navigationButton);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.interest :
                        startActivity(new Intent(getApplicationContext() , HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.setting :
                        startActivity(new Intent(getApplicationContext() , HomeActivity3.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.find:
                        startActivity(new Intent(getApplicationContext() , HomeActivity2.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home :
                        return  true;

                    case R.id.request :
                        startActivity(new Intent(getApplicationContext() , RequestActivity.class));
                        overridePendingTransition(0 ,0);
                        return true;
                }

                return false;
            }
        });




        logOut = findViewById(R.id.logout);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(MyChatActivity.this, R.style.Dialogue);

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
                        startActivity(new Intent(MyChatActivity.this, RegistrationActivity.class));
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



        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyChatAdapter(MyChatActivity.this , usersArrayList , hs);
        recycler.setAdapter(adapter);




    }

    public void onBackPressed()
    {
          moveTaskToBack(true);
          android.os.Process.killProcess(android.os.Process.myPid());
          System.exit(1);

    }

}