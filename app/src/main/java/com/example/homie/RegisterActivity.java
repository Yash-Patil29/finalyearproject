package com.example.homie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Models.User;

public class RegisterActivity extends AppCompatActivity {
    TextInputEditText textEmail,textPassword,textName;
    TextView gotologin;
    ProgressBar progressBar;
    Button button;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textEmail = (TextInputEditText)findViewById(R.id.email_ed_register);
        textPassword = (TextInputEditText) findViewById(R.id.password_ed_register);
        textName = (TextInputEditText) findViewById(R.id.name_ed_register);
        gotologin=(TextView)findViewById(R.id.gotoLogin);
        button = (Button) findViewById(R.id.btnRegister);
        auth= FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference().child("Users");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();

            }
        });
        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLogin();
            }
        });

    }
    public void RegisterUser(){

        final String email = textEmail.getText().toString();
        final  String password = textPassword.getText().toString();
        final String name = textName.getText().toString();

        if (!email.equals(" ")&& !password.equals("")&& password.length()>6){
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               // insert Value in DataBase
                               FirebaseUser firebaseUser = auth.getCurrentUser();
                               User U = new User();
                               U.setName(name);
                               U.setEmail(email);

                               reference.child(firebaseUser.getUid()).setValue(U)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                             if (task.isSuccessful()){
                                                 Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                                                 finish();
                                                 Intent i = new Intent(RegisterActivity.this,GroupChatActivity.class);
                                                 startActivity(i);
                                             }
                                             else {

                                                 Toast.makeText(RegisterActivity.this, "User Could not be Created", Toast.LENGTH_SHORT).show();
                                             }
                                           }
                                       });
                           }
                        }
                    });
        }

    }

    public void gotoLogin(){
        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(i);
    }

}