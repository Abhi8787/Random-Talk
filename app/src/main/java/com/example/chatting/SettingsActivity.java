
package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    ImageView save;
    ImageView profile;
    EditText name;
    EditText status;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri selectedImageUri;
    String femail;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        save = findViewById(R.id.save);
        profile = findViewById(R.id.profile);
        name = findViewById(R.id.settingName);
        status = findViewById(R.id.settingStatus);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                femail = snapshot.child("email").getValue().toString();
                String fname = snapshot.child("name").getValue().toString();
                String fstatus = snapshot.child("status").getValue().toString();
                String fimage = snapshot.child("imageUri").getValue().toString();

                name.setText(fname);
                status.setText(fstatus);
                Picasso.get().load(fimage).into(profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                String t_name = name.getText().toString();
                String t_status = status.getText().toString();

                if (selectedImageUri != null) {
                    storageReference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String finalImageUri = uri.toString();

                                    Users users = new Users(auth.getUid(), t_name, femail, finalImageUri, t_status);

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });


                                }
                            });

                        }
                    });
                } else {

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String finalImageUri = uri.toString();

                            Users users = new Users(auth.getUid(), t_name, femail, finalImageUri, t_status);

                            reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Data Successfully Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(SettingsActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }
                    });

                }

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
        {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 10)
            {
                if(data != null)
                {
                    selectedImageUri = data.getData();
                    profile.setImageURI(selectedImageUri);
                }
            }
        }


}