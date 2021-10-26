package com.example.flappy_bird_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginRegisterHomeActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register_home);
        emailField =(EditText) findViewById(R.id.email_field);
        passwordField =(EditText) findViewById(R.id.password_field);
    }

    public void registerEntry(View view) {
        //register logic here
        Toast.makeText(this,"Successfully Register",Toast.LENGTH_LONG);
        startActivity(new Intent(this,OnlineGamingActivity.class));
        finish();
    }

    public void loginEntry(View view) {
        //login logic here
        Toast.makeText(this,"Successfully login",Toast.LENGTH_LONG);
        startActivity(new Intent(this,OnlineGamingActivity.class));
        finish();
    }
}