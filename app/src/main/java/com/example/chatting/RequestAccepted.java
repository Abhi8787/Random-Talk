package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RequestAccepted extends AppCompatActivity {

    TextView name;
    TextView interest;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted);


        String name_str = getIntent().getStringExtra("name");
        name = findViewById(R.id.name);
        name.setText(name_str);


        interest = findViewById(R.id.interest);
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestAccepted.this , ListActivity.class);
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);



            }
        });


        profile = findViewById(R.id.profile);
        String imageUri = getIntent().getStringExtra("imageUri");
        Log.d("uri" , imageUri);
        Picasso.get().load(imageUri).into(profile);





    }


    public void onBackPressed()
    {
        Intent intent = new Intent(RequestAccepted.this , RequestActivity.class);
        startActivity(intent);
        finish();

    }

}