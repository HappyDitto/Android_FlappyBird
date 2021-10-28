package com.example.flappy_bird_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class OnlineGamingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_gaming);
    }

    public void logoutExit(View view) {
        //logout logic here
        Toast.makeText(this,"Successfully Logout",Toast.LENGTH_LONG);
        startActivity(new Intent(this,MainActivity.class));
    }

    public void profileLogoutEntry(View view) {
        startActivity(new Intent(OnlineGamingActivity.this,ProfileLogoutActivity.class));
    }
}