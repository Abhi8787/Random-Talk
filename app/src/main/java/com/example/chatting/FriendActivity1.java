package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class FriendActivity1 extends AppCompatActivity {

    Button sendBtn;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseDatabase database1;
    String ReceiverImage;
    String ReceiverUID;
    String ReceiverName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend1);


        ReceiverName = getIntent().getStringExtra("name");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverUID = getIntent().getStringExtra("uid");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                auth = FirebaseAuth.getInstance();
                database = FirebaseDatabase.getInstance();
                database1 = FirebaseDatabase.getInstance();


                database.getReference().child("requests").child(auth.getUid()).child(ReceiverUID).
                        setValue(ReceiverUID).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        String str = auth.getUid()+ ReceiverUID;
                        database1.getReference().child("acceptRequest").child(str).setValue(str)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                });
            }
        });



    }
}