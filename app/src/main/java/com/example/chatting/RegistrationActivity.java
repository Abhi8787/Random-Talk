package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {


    CircleImageView profile;
    EditText name;
    EditText email;
    EditText password;
    EditText confirm_password;
    TextView signUp;
    TextView signIn;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Uri imageUri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        profile = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        signUp = findViewById(R.id.signup);
        signIn = findViewById(R.id.signIn);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                String t_name = name.getText().toString().trim();
                String t_email = email.getText().toString().trim();
                String t_password = password.getText().toString().trim();
                String t_confirm_Password = confirm_password.getText().toString().trim();
                String t_status = "Hey There I'm Using This Application";

                if (TextUtils.isEmpty(t_name) || TextUtils.isEmpty(t_email) || TextUtils.isEmpty(t_password)
                        || TextUtils.isEmpty(t_confirm_Password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                } else if (!t_email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    email.setError("Invalid Email Id");
                    Toast.makeText(RegistrationActivity.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
                } else if (!t_password.equals(t_confirm_Password)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Confirm Password Not matched", Toast.LENGTH_SHORT).show();
                } else if (t_password.length() < 6) {
                    progressDialog.dismiss();
                    Toast.makeText(RegistrationActivity.this, "Password Length too Small", Toast.LENGTH_SHORT).show();
                } else {

                    auth.createUserWithEmailAndPassword(t_email, t_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // Now storing the data into database.

                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                if(imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users users = new Users(auth.getUid(), t_name, t_email, imageURI , t_status );
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {



                                                                    final SharedPreferences sharedPreferences = getSharedPreferences("Data" , Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.putString("Email",t_email);
                                                                    editor.putString("Password",t_password);
                                                                    editor.commit();

                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(RegistrationActivity.this, InterestActivity.class));
                                                                    finish();
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(RegistrationActivity.this, "Error in Creating a new User", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }
                                    });
                                }

                                else
                                {
                                    String t_status = "Hey There I'm Using This Application";
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/chatting-93fb2.appspot.com/o/profile_image.png?alt=media&token=0e27a2a5-cfa5-42cd-8af2-dd87ebd0b7d2";
                                    Users users = new Users(auth.getUid() ,t_name , t_email ,imageURI , t_status );
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if(task.isSuccessful())
                                            {

                                                final SharedPreferences sharedPreferences = getSharedPreferences("Data" , Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("Email",t_email);
                                                editor.putString("Password",t_password);
                                                editor.commit();

                                                progressDialog.dismiss();
                                                startActivity(new Intent(RegistrationActivity.this , InterestActivity.class));
                                                finish();
                                            }
                                            else
                                            {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistrationActivity.this, "Error in Creating a new User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }


                            else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "!Registration Failed", Toast.LENGTH_SHORT).show();
                            }

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


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10)
        {
            if(data != null)
            {
                imageUri = data.getData();
                profile.setImageURI(imageUri);
            }
        }
    }
}