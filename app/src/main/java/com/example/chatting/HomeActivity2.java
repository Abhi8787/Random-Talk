package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class HomeActivity2 extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    RecyclerView recycler;
    ProgressBar progressBar;
    FriendAdapter adapter;

    HashSet<String> request;
    ProgressBar progressBar1;
    ProgressBar progressBar2;

    HashSet<String> friends;
    FirebaseDatabase database1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);


        friends = new HashSet<>();
        database1 = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database1.getReference().child("friends").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String str = dataSnapshot.getValue(String.class);
                    friends.add(str);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        database = FirebaseDatabase.getInstance();
        usersArrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);

        DatabaseReference reference = database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = new Users();
                    users = dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                Log.d("fireee",usersArrayList.size()+"UserArrayList Size ");
                progressBar.setVisibility(View.GONE);

                Collections.sort(usersArrayList);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        request = new HashSet<>();
        progressBar1 = findViewById(R.id.progressBar);

        DatabaseReference reference1 =  database.getReference().child("requests").child(auth.getUid());

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot :snapshot.getChildren())
                {
                    String str = dataSnapshot.getValue(String.class);
                    request.add(str);
                }
                Log.d("HI",request.size()+"");
                progressBar1.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        bottomNavigationView = findViewById(R.id.navigationButton);

        bottomNavigationView.setSelectedItemId(R.id.find);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.interest :
                        startActivity(new Intent(getApplicationContext() , HomeActivity.class));
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


                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(),HomeActivity3.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.find:
                        return true;

                }

                return false;
            }
        });



        progressBar2 = findViewById(R.id.progressBar);

        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FriendAdapter(HomeActivity2.this, usersArrayList , request , progressBar2 , friends);
        recycler.setAdapter(adapter);


    }


    public void onBackPressed()
    {
        Intent intent = new Intent(HomeActivity2.this , MyChatActivity.class);
        startActivity(intent);
        finish();

    }




}