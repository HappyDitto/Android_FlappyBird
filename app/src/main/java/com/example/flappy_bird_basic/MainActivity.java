package com.example.flappy_bird_basic;

import static utils.DatabaseCRUD.*;
import static utils.Utils.*;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.speech.RecognizerIntent;


import java.util.ArrayList;
import java.util.Locale;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import userInfo.User;


public class MainActivity extends Activity {
    private Button playDirectlyBtn;
    private Button loginBtn;
    private TextView txvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // speech recognition
        txvResult = (TextView) findViewById(R.id.textView);

        // databases
        playDirectlyBtn = findViewById(R.id.startDirectlyBtn);
        loginBtn = findViewById(R.id.goLoginBtn);

    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            toast(this,"Your Device Don't Support Speech Input");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));
                }
                break;
        }
    }


    public void goStart(View view){
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
    }

    /***
     * Firebase Runtime Database Connection Testing
     */
    private void dataBaseInitTrying(){
        // Database Init Test
        // Write a message to the database
        DatabaseReference myRef = getFirebaseRef();

        //存数据
        addUser(this,myRef,new User("1"));
        //监听到数据库有变化取出来打印
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.i("监听到变化:", value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("监听到错误:", error.toException().toString());
            }
        });

    }

    public void accountLoginRegister(View view) {
        Intent intentForLoginRegister= new Intent(this,LoginRegisterHomeActivity.class);
        startActivity(intentForLoginRegister);
    }


}