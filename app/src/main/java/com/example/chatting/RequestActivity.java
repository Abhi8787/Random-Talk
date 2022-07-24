package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

public class RequestActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressBar progressBar;
    HashSet<String> request;
    RequestAdapter adapter;
    ArrayList<Users> usersArrayList;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressBar = findViewById(R.id.progressBar);

        request = new HashSet<>();

        DatabaseReference reference =  database.getReference().child("acceptRequest");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot :snapshot.getChildren())
                {
                    String str = dataSnapshot.getValue(String.class);
                    request.add(str);
                }
                Log.d("HI",request.size()+"");
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


         usersArrayList = new ArrayList<>();

        DatabaseReference reference1 = database.getReference().child("user");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = new Users();
                    users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                Collections.sort(usersArrayList);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        bottomNavigationView = findViewById(R.id.navigationButton);

        bottomNavigationView.setSelectedItemId(R.id.request);

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
                        startActivity(new Intent(getApplicationContext() , HomeActivity3.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.find:
                        startActivity(new Intent(getApplicationContext() , HomeActivity2.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home :
                        startActivity(new Intent(getApplicationContext() , MyChatActivity.class));
                        overridePendingTransition(0,0);
                        return  true;

                    case R.id.request :
                        startActivity(new Intent(getApplicationContext() , RequestActivity.class));
                        overridePendingTransition(0,0);
                        return  true;
                }

                return false;
            }
        });


        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestAdapter(RequestActivity.this, usersArrayList , request);
        recycler.setAdapter(adapter);



    }

    public void onBackPressed()
    {
        Intent intent = new Intent(RequestActivity.this , MyChatActivity.class);
        startActivity(intent);
        finish();

    }


}