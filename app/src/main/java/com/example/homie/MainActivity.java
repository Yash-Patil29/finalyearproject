package com.example.homie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TextInputEditText textEmail,textPassword;
    ProgressBar progressBar;

    FirebaseAuth Auth;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth = FirebaseAuth.getInstance();
        if(Auth.getCurrentUser() !=null){
            Intent i =  new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(i);
        }
        else {
            setContentView(R.layout.activity_main);
            textEmail = findViewById(R.id.email_ed_login);
            textPassword = findViewById(R.id.password_ed_login);


        }

    }

    public  void LoginUser(View v){

        String Email = textEmail.getText().toString();
        String Password = textPassword.getText().toString();

        if (!Email.equals(" ") && Password.equals(" ")){
            Auth.signInWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                             if (task.isSuccessful()){

                                 Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                                 Intent i =  new Intent(MainActivity.this,GroupChatActivity.class);
                                 startActivity(i);

                             }
                             else {
                                 Toast.makeText(MainActivity.this, "Wrong Email / Password. Try Again", Toast.LENGTH_SHORT).show();

                             }
                        }
                    });
        }
    }

    public void gotoRegister(View v){
        Intent i = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(i);
    }

    public void forgotPassword(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

        LinearLayout container = new LinearLayout(MainActivity.this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        ip.setMargins(50,0,0,100);

        final EditText input = new EditText(MainActivity.this);
        input.setLayoutParams(ip);
        input.setGravity(Gravity.TOP | Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setLines(1);
        input.setMaxLines(1);
        container.addView(input,ip);


        alert.setMessage("Enter your register Email");
        alert.setTitle("Forgot Password?");
        alert.setView(container);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String entered_email = input.getText().toString();
                Auth.sendPasswordResetEmail(entered_email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {
                           if (task.isSuccessful()){
                               dialog.dismiss();
                               Toast.makeText(MainActivity.this, "Email has been send please check your Email", Toast.LENGTH_SHORT).show();
                           }
                            }
                        });
            }
        });

    }
}