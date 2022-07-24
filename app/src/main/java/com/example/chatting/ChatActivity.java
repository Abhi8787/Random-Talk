package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReceiverImage;
    String ReceiverUID;
    String ReceiverName;
    String SenderUID;
    CircleImageView profileImage;
    TextView receiverName;

    FirebaseDatabase database;
    FirebaseAuth auth;

    public static String senderImage;
    public static String receiverImage;

    CardView sendBtn;
    EditText editTextMsg;

    String senderRoom;
    String receiverRoom;

    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;

    MessagesAdapter adapter;

    Boolean flag=false;
    ImageView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        ReceiverName = getIntent().getStringExtra("name");
        ReceiverImage = getIntent().getStringExtra("ReceiverImage");
        ReceiverUID = getIntent().getStringExtra("uid");

        messagesArrayList = new ArrayList<>();

        profileImage = findViewById(R.id.profileImage);
        receiverName = findViewById(R.id.receiverName);

        messageAdapter = findViewById(R.id.messageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this );
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter(ChatActivity.this, messagesArrayList);
        messageAdapter.setAdapter(adapter);

        sendBtn = findViewById(R.id.sendBtn);
        editTextMsg = findViewById(R.id.editTextMsg);

        Picasso.get().load(ReceiverImage).into(profileImage);
        receiverName.setText(""+ReceiverName);

        SenderUID = auth.getUid();

        senderRoom = SenderUID+ReceiverUID;
        receiverRoom = ReceiverUID+SenderUID;



        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");


        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                senderImage = snapshot.child("imageUri").getValue().toString();
                receiverImage = ReceiverImage;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMsg.getText().toString();

                if(message.isEmpty())
                {
                    Toast.makeText(ChatActivity.this, "Please Enter Valid Message.", Toast.LENGTH_SHORT).show();
                    return;
                }

                editTextMsg.setText("");


                Date date = new Date();

                Messages msg = new Messages(message , SenderUID , date.getTime() );

                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages").push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        database = FirebaseDatabase.getInstance();
                        database.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }
                });
            }
        });




        list = findViewById(R.id.list);

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ChatActivity.this , ListActivity.class);
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);

            }
        });


    }
}