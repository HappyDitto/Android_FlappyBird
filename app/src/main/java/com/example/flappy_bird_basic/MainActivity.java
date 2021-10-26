package com.example.flappy_bird_basic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends Activity {
    private ImageButton accountEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loginEntry
        accountEntry= (ImageButton) findViewById(R.id.empty_account);

        dataBaseInitTrying();
        Log.i("监听到变化:", "111111111111");
    }

    public void startGame(View view){
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
        finish();
    }

    /***
     * Firebase Runtime Database Connection Testing
     */
    private void dataBaseInitTrying(){
        // Database Init Test
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        //存数据
        myRef.setValue("数据库连接测试!!!");
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