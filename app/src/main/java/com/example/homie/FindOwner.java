package com.example.homie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindOwner extends AppCompatActivity {
    Button buttonAdd;
    EditText Name;
    EditText carNumber;
    EditText phoneNumber;

    DatabaseReference databaseOwnerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_owner);
        databaseOwnerInfo = FirebaseDatabase.getInstance().getReference("ownerInfo");


        buttonAdd=findViewById(R.id.addButton);
        Name=findViewById(R.id.ownerName);
        carNumber=findViewById(R.id.carNumber);
        phoneNumber=findViewById(R.id.phoneNumber);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOwnerinfo();
            }
        });
    }

    private void addOwnerinfo(){
      String name = Name.getText().toString().trim();
      String ownerNumber = phoneNumber.getText().toString().trim();
      String vehicalNumber = carNumber.getText().toString().trim();

      if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(ownerNumber) && !TextUtils.isEmpty(vehicalNumber)){

          String id = databaseOwnerInfo.push().getKey();

          ownerInfo ownerInfo = new ownerInfo(id,name,vehicalNumber,ownerNumber);

          databaseOwnerInfo.child(id).setValue(ownerInfo);

          Toast.makeText(this, "Information Added", Toast.LENGTH_SHORT).show();

      }else {
          Toast.makeText(this, "Please Enter the Proper Information", Toast.LENGTH_SHORT).show();
      }

    }
}