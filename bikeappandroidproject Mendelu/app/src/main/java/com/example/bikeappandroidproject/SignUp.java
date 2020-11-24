package com.example.bikeappandroidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLoginBtn;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks
        regName = findViewById(R.id.regName);
        regUsername = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPhoneNo = findViewById(R.id.regPhoneNo);
        regPassword = findViewById(R.id.regPassword);
        regBtn = findViewById(R.id.regBtn);
        regToLoginBtn = findViewById(R.id.regToLoginBtn);

        //Saving data into Fire database
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  rootNode = FirebaseDatabase.getInstance();
                  reference = rootNode.getReference("users");

                  if(!validateName() | !validatePassword() | !validatePhoneNo() | !validateEmail() | !validateUsername()){
                      return;
                  }
                    //Get the values
                String name = regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phoneNo = regPhoneNo.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                UserHelperClass helperClass = new UserHelperClass(name,username,email,phoneNo,password);

                    reference.child(username).setValue(helperClass);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Account was created!",
                        Toast.LENGTH_SHORT);

                toast.show();

                /* переход на страницу пользователя, просто проверить дизайн
                Intent intent = new Intent(SignUp.this, UserProfile.class);
                startActivity(intent);
                finish();*/
            }
        });

        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();

        if(val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }
        else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        } else if(val.length()>=15){
            regUsername.setError("Username too long");
            return false;
        }
        else if(!val.matches(noWhiteSpace)){
            regUsername.setError("White spaces are not allowed");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else{
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhoneNo(){
        String val = regPhoneNo.getEditText().getText().toString();

        if(val.isEmpty()){
            regPhoneNo.setError("Field cannot be empty");
            return false;
        }
        else{
            regPhoneNo.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" + //any letter
                "(?=.*[@#$%^&+=])" + //at least one special character
                "(?=\\S+$)" + //no white spaces
                ".{4,}" + //at least 4 characters
                "$";


        if(val.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(passwordVal)){
            regPassword.setError("Password is too weak");
            return false;
        }
        else{
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

}
