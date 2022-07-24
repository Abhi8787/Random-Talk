package com.example.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class checkProfile extends AppCompatActivity {

    TextView name;
    ImageView profile;
    TextView status;
    TextView interest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_profile);

        name = findViewById(R.id.name);
        profile = findViewById(R.id.profile);
        status = findViewById(R.id.status);


        name.setText(getIntent().getStringExtra("name"));
        status.setText(getIntent().getStringExtra("status"));

        String imageUri = getIntent().getStringExtra("imageUri");
        Picasso.get().load(imageUri).into(profile);


        interest = findViewById(R.id.interest);

        interest = findViewById(R.id.interest);
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(checkProfile.this , ListActivity.class);
                intent.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent);



            }
        });






    }

    public void onBackPressed()
    {
        Intent intent = new Intent(checkProfile.this , HomeActivity2.class);
        startActivity(intent);
        finish();

    }


}