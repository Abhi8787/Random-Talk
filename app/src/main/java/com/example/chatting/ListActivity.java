package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<String> interest;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ListAdapter adapter;
    RecyclerView interestRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        interest = new ArrayList<>();

        String uid = getIntent().getStringExtra("uid");


        database.getReference().child("interests").child(uid).
                addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        InterestsFirebase inter = new InterestsFirebase();
                        inter = snapshot.getValue(InterestsFirebase.class);

                        for(String val : inter.arr)
                            interest.add(val);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        interestRecycler = findViewById(R.id.interestRecycler);

        interestRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(ListActivity.this ,interest );
        interestRecycler.setAdapter(adapter);


    }
}