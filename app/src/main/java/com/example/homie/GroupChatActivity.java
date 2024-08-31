package com.example.homie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.MessageAdapter;
import Models.AllMethods;
import Models.Message;
import Models.User;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth Auth;
    FirebaseDatabase Database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    User U ;
    List<Message> messages;

    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imgButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        init();
    }
    private  void init(){
        Auth= FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance();
        U = new User();

        rvMessage = (RecyclerView) findViewById(R.id.rvMessage);
        etMessage =(EditText) findViewById(R.id.etMessage);
        imgButton = (ImageButton) findViewById(R.id.btnsend);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(etMessage.getText().toString())){
            Message message = new Message(etMessage.getText().toString(),U.getName());
            etMessage.setText("");
            messagedb.push().setValue(message);
        }
        else {
            Toast.makeText(getApplicationContext(), "You can't send blank message", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.menuLogout){
            Auth.signOut();
            finish();
            startActivity(new Intent(GroupChatActivity.this,MainActivity.class));
        }
        if(item.getItemId()==R.id.findOwner){
            Intent i = new Intent(GroupChatActivity.this,FindOwner.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = Auth.getCurrentUser();
        U.setUid(currentUser.getUid());
        U.setEmail(currentUser.getEmail());

        Database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                U = snapshot.getValue(User.class);
                U.setUid(currentUser.getUid());
                AllMethods.name= U.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagedb = Database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessage(messages);


            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages =  new ArrayList<Message>();

                for (Message m :messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }
                    else {
                        newMessages.add(m);

                    }
                }
                messages=newMessages;

                displayMessage(messages);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();
                for (Message m :messages){
                    if (!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessage(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();

    }

    private void displayMessage(List<Message> messages) {
         rvMessage.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
         messageAdapter = new MessageAdapter(GroupChatActivity.this,messages,messagedb);
         rvMessage.setAdapter(messageAdapter);

    }


}