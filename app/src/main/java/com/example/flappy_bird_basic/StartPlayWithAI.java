package com.example.flappy_bird_basic;

import static com.example.flappy_bird_basic.MainActivity.bestscore;
import static utils.DatabaseCRUD.setUserBestScore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import java.util.Timer;
import java.util.TimerTask;

import utils.DatabaseCRUD;
import utils.Utils;

public class StartPlayWithAI extends Activity {

    public static int onescore = 0;
    public static int rank = 1;
    public static final int AI_MODE = 0;
    public static final int SOLO_MODE = 1;
    private int databaseScore = 0;

    private PlayWithAIView gameView;
    private Handler handler = new Handler(); // handler object used for handler thread
    private boolean hasDied = false; // Used to help re-set game loop when user has died
    private final static long TIMER_INTERVAL = 30; // used to define delay time

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
    }

    public void startGame(View view){
        int mode = getIntent().getIntExtra("Mode", AI_MODE);
        gameView = new PlayWithAIView(this, mode);
        setContentView(gameView);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Running the Gameview class in handler
                handler.post(() -> {
                    gameView.invalidate();

                    if (!gameView.gameState && !hasDied) // if user dies
                    {
                        // update score to database
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {

                            String thisuid = user.getUid();

                            DatabaseCRUD.getUserBestScore(thisuid).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    DataSnapshot scoreData = (DataSnapshot) task.getResult();
                                    Log.i("best score：", scoreData.getValue().toString());
                                    databaseScore = new Long((Long) scoreData.getValue()).intValue();
                                    if (bestscore > databaseScore) {
                                        setUserBestScore(thisuid, bestscore);
                                    }

                                }
                            });
                        }

                        Intent intent=new Intent(StartPlayWithAI.this, result.class);// show results screen
                        intent.putExtra("score", onescore);
                        intent.putExtra("rank", rank);
                        startActivity(intent);

                        hasDied = true; // set this boolean to true for two reasons. 1. allows the user to use "try again" button multiple times
                        // 2. shows result screen once, instead of re-printing it continually while the if loop conditions are met
                        finish();
                    }

                });

            }
        }, 0, TIMER_INTERVAL);
    }


}

