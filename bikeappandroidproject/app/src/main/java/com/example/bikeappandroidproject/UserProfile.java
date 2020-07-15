package com.example.bikeappandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    TextInputLayout fullName,email,phoneNo,password;
    TextView fullNameLabel, usernameLabel;
    //Global vars to hold user data inside the activity
    String user_username, user_name, user_email, user_phoneNo, user_password;

    DatabaseReference reference;

    Button toMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        //Hooks
        fullName = findViewById(R.id.full_name_profile);
        email = findViewById(R.id.email_profile);
        phoneNo = findViewById(R.id.phone_no_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.full_name);
        usernameLabel = findViewById(R.id.description);

        toMap = findViewById(R.id.toMap);

        //show all data
        showAllUserData();

        toMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showAllUserData(){
        Intent intent = getIntent();
         user_username = intent.getStringExtra("username");
         user_name = intent.getStringExtra("name");
         user_email = intent.getStringExtra("email");
         user_phoneNo = intent.getStringExtra("phoneNo");
         user_password = intent.getStringExtra("password");

        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phoneNo.getEditText().setText(user_phoneNo);
        password.getEditText().setText(user_password);

    }

    public void update(View view){

        if(isNameChanged() || isPasswordChanged() | isEmailChanged() | isPhoneNoChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Data is the same and cannot be updated", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPasswordChanged() {
        if(!user_password.equals(password.getEditText().getText().toString())){
            reference.child(user_username).child("password").setValue(password.getEditText().getText().toString());
            user_password = password.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isEmailChanged() {
        if(!user_email.equals(email.getEditText().getText().toString())){
            reference.child(user_username).child("email").setValue(email.getEditText().getText().toString());
            user_email = email.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isPhoneNoChanged() {
        if(!user_phoneNo.equals(phoneNo.getEditText().getText().toString())){
            reference.child(user_username).child("phoneNo").setValue(phoneNo.getEditText().getText().toString());
            user_phoneNo = phoneNo.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }

    private boolean isNameChanged() {
        if(!user_name.equals(fullName.getEditText().getText().toString())){
            reference.child(user_username).child("name").setValue(fullName.getEditText().getText().toString());
            user_name = fullName.getEditText().getText().toString();
            return true;
        }else{
            return false;
        }
    }
}
