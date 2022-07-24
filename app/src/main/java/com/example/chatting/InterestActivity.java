package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class InterestActivity extends AppCompatActivity  {

    ArrayList<String> arrr;

    ArrayList<Interests> interestsArrayList;
    RecyclerView interestRecycler;
    InterestAdapter adapter;

    FloatingActionButton fab;

    FirebaseDatabase database;
    FirebaseAuth auth;

    ProgressBar progressBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        interestsArrayList = new ArrayList<>();

        int image1 = getResources().getIdentifier("@drawable/cricket",null ,this.getPackageName());
        int image2 = getResources().getIdentifier("@drawable/badminton",null,this.getPackageName());
        int image3 = getResources().getIdentifier("@drawable/football",null,this.getPackageName());
        int image4 = getResources().getIdentifier("@drawable/khokho",null,this.getPackageName());
        int image5 = getResources().getIdentifier("@drawable/basketball",null,this.getPackageName());
        int image6 = getResources().getIdentifier("@drawable/hockey",null,this.getPackageName());
        int image7 = getResources().getIdentifier("@drawable/cloudcomputing",null,this.getPackageName());
        int image8 = getResources().getIdentifier("@drawable/android",null,this.getPackageName());
        int image9 = getResources().getIdentifier("@drawable/cyber",null,this.getPackageName());
        int image10 = getResources().getIdentifier("@drawable/web",null,this.getPackageName());
        int image11 = getResources().getIdentifier("@drawable/artificial",null,this.getPackageName());
        int image12 = getResources().getIdentifier("@drawable/datascience",null,this.getPackageName());
        int image13 = getResources().getIdentifier("@drawable/politics",null,this.getPackageName());
        int image14 = getResources().getIdentifier("@drawable/sports",null,this.getPackageName());
        int image15 = getResources().getIdentifier("@drawable/bollywood",null,this.getPackageName());
        int image16 = getResources().getIdentifier("@drawable/hollywood",null,this.getPackageName());
        int image17 = getResources().getIdentifier("@drawable/maths",null,this.getPackageName());
        int image18 = getResources().getIdentifier("@drawable/physics",null,this.getPackageName());
        int image19 = getResources().getIdentifier("@drawable/chemistry",null,this.getPackageName());
        int image20 = getResources().getIdentifier("@drawable/biology",null,this.getPackageName());
        int image21 = getResources().getIdentifier("@drawable/social",null,this.getPackageName());




        interestsArrayList.add(new Interests(image1 , "Cricket",0));
        interestsArrayList.add(new Interests(image2 , "Badminton",0));
        interestsArrayList.add(new Interests(image3 , "Football",0));
        interestsArrayList.add(new Interests(image4 , "Kho Kho",0));
        interestsArrayList.add(new Interests(image5 , "Basketball",0));
        interestsArrayList.add(new Interests(image6 , "Hockey",0));
        interestsArrayList.add(new Interests(image7 , "Cloud Computing",0));
        interestsArrayList.add(new Interests(image8 , "Android Development",0));
        interestsArrayList.add(new Interests(image9 , "Cyber Security",0));
        interestsArrayList.add(new Interests(image10 , "Web Development",0));
        interestsArrayList.add(new Interests(image11 , "Artificial Intelligence",0));
        interestsArrayList.add(new Interests(image12 , "Data Science",0));
        interestsArrayList.add(new Interests(image13 , "Politics",0));
        interestsArrayList.add(new Interests(image14 , "Sports",0));
        interestsArrayList.add(new Interests(image15 , "Bollywood",0));
        interestsArrayList.add(new Interests(image16 , "Hollywood",0));
        interestsArrayList.add(new Interests(image17 , "Mathematics",0));
        interestsArrayList.add(new Interests(image18 , "Physics",0));
        interestsArrayList.add(new Interests(image19 , "Chemistry",0));
        interestsArrayList.add(new Interests(image20 , "Biology",0));
        interestsArrayList.add(new Interests(image21 , "Social Studies",0));



        interestRecycler = findViewById(R.id.interestRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        interestRecycler.setLayoutManager(linearLayoutManager);
        adapter = new InterestAdapter(interestsArrayList,this);
        interestRecycler.setAdapter(adapter);

       progressBar = findViewById(R.id.progressBar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        fab = findViewById(R.id.fab);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelecteed().size() > 0)
                {
                    progressDialog.show();
                     arrr = new ArrayList<>();

                    for(Interests interestss : adapter.getSelecteed())
                    {
                        arrr.add(interestss.text.toString().trim());
                    }

                    for(String str : arrr)
                    {
                        Log.d("success", str);
                    }


                    InterestsFirebase inter = new InterestsFirebase(auth.getUid().toString(),arrr);

                    database.getReference().child("interests").child(auth.getUid()).setValue(inter).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            startActivity(new Intent(InterestActivity.this, HomeActivity.class));
                        }
                    });
                    adapter.notifyDataSetChanged();
               }

                else
                {
                    Toast.makeText(InterestActivity.this, "Select your Interest", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

}