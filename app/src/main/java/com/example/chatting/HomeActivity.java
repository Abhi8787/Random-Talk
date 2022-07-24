package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;

    ImageView logOut;
    RecyclerView recycler;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    HashMap<String, ArrayList<String>> hm;

    ArrayList<InterestsFirebase> interestArrayList;
    ArrayList<String> array;

    BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;
    ProgressBar progressBar1;
    ProgressBar progressBar2;

    HashSet<String> friends;
    HashSet<String> request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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



        friends = new HashSet<>();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("friends").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
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























        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersArrayList = new ArrayList<>();

        hm = new HashMap<>();
        array = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar);


           database.getReference().child("interests").child(auth.getCurrentUser().getUid()).
             addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {

                     InterestsFirebase inter = new InterestsFirebase();
                     inter = snapshot.getValue(InterestsFirebase.class);

                     for(String val : inter.arr)
                         array.add(val);

                     Log.d("CHEck","array ka size"+array.size());
                     Collections.sort(array);
                     progressBar.setVisibility(View.GONE);
                     adapter.notifyDataSetChanged();
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {

                 }
             });


        interestArrayList = new ArrayList<>();

        DatabaseReference references = database.getReference().child("interests");

        references.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    InterestsFirebase inter = new InterestsFirebase();
                    inter = dataSnapshot.getValue(InterestsFirebase.class);
                    hm.put(inter.uID.toString() , inter.arr);
                    interestArrayList.add(inter);
                }
                Log.d("fire" , hm.size()+" HashMapSize");
                progressBar1.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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





        logOut = findViewById(R.id.logout);




        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(HomeActivity.this, RegistrationActivity.class));
        }


       bottomNavigationView = findViewById(R.id.navigationButton);

        bottomNavigationView.setSelectedItemId(R.id.interest);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId())
                {
                    case R.id.interest:
                    return true;


                    case R.id.setting:
                     startActivity(new Intent(getApplicationContext() , HomeActivity3.class));
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


                    case R.id.find:
                        startActivity(new Intent(getApplicationContext() , HomeActivity2.class));
                        overridePendingTransition(0,0);
                        return true;

                }


                return false;
            }






        });



        recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(HomeActivity.this, usersArrayList , hm , array , friends , request);
        recycler.setAdapter(adapter);


    }

    public void onBackPressed()
    {
        Intent intent = new Intent(HomeActivity.this , MyChatActivity.class);
        startActivity(intent);
        finish();

    }
}