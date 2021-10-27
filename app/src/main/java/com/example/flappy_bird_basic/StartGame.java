package com.example.flappy_bird_basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

public class StartGame extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
    }

    public void startGame(View view){
        gameView = new GameView(this);
        setContentView(gameView);
    }
}
