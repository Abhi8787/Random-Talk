 package com.example.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

 public class LoginActivity extends AppCompatActivity {

    TextView signUp;
    EditText email;
    EditText password;
    TextView signIn;
    TextView getSignUp;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final SharedPreferences sharedPreferences = getSharedPreferences("Data" , Context.MODE_PRIVATE);

        signUp = findViewById(R.id.signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signIn);
        signUp = findViewById(R.id.signUp);
        auth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();

                String t_email  = email.getText().toString().trim();
                String t_password = password.getText().toString().trim();

                if(TextUtils.isEmpty(t_email) || TextUtils.isEmpty(t_password))
                {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }

                else if(!t_email.matches(emailPattern))
                {
                    progressDialog.dismiss();
                    email.setError("Invalid email");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }

                else if(t_password.length() < 6)
                {
                    progressDialog.dismiss();
                    password.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    auth.signInWithEmailAndPassword(t_email , t_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Email",t_email);
                                editor.putString("Password",t_password);
                                editor.commit();
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this ,InterestActivity.class));
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , RegistrationActivity.class));
            }
        });


    }
}