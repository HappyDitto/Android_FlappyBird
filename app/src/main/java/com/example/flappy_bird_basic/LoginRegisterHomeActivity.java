package com.example.flappy_bird_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginRegisterHomeActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_home);
        emailField =(EditText) findViewById(R.id.email_field);
        passwordField =(EditText) findViewById(R.id.password_field);
        loginBtn=(Button) findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();


    }

    public void registerEntry(View view) {
        String email_input=emailField.getText().toString();
        String password_input=passwordField.getText().toString();
        if (TextUtils.isEmpty(email_input)||TextUtils.isEmpty(password_input)) {
            Toast.makeText(this,"Empty email or password",Toast.LENGTH_SHORT);
        }else {
            registerOnDemand(email_input,password_input);
        }
        //register logic here
        Toast.makeText(this,"Successfully Register",Toast.LENGTH_LONG);
        startActivity(new Intent(this,OnlineGamingActivity.class));
        finish();
    }

    private void registerOnDemand(String email_input, String password_input) {
    }

    public void loginEntry(View view) {
        //login logic here
        Toast.makeText(this,"Successfully login",Toast.LENGTH_LONG);
        startActivity(new Intent(this,OnlineGamingActivity.class));
        finish();
    }
}