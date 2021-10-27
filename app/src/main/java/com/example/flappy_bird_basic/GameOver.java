package com.example.flappy_bird_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameOver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

    }
//    public void goGameOver(View view){
//        Intent intent = new Intent(GameView.this, GameOver.class);
//        intent.putExtra("score", score);
//        startActivity(intent);
//    }

}