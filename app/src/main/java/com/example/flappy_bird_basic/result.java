package com.example.flappy_bird_basic;

import static com.example.flappy_bird_basic.MainActivity.bestscore;
import static com.example.flappy_bird_basic.StartGame.onescore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class result extends AppCompatActivity {
    private ImageButton button;
    private ImageButton goTopListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

//        Intent reintent = getIntent();
//        int score = reintent.getIntExtra("score", 0);
//        int bestscore = reintent.getIntExtra("bestscore", 0);

        TextView RefreshTextObject = (TextView) findViewById(R.id.scoretext);
        int score = getIntent().getIntExtra("score", 0);
        RefreshTextObject.setText(String.valueOf(score));
        TextView RefreshbestTextObject = (TextView) findViewById(R.id.bestscoretext);
        RefreshbestTextObject.setText(String.valueOf(bestscore));

        //WebView
        WebView browser=(WebView)findViewById(R.id.webView);
//        TextView thistext = (TextView) findViewById(R.id.scoretext);
//        int thisscore = Integer.parseInt(thistext.getText().toString());

//        browser.setBackgroundResource(R.drawable.number1);
        browser.setBackgroundResource(R.drawable.number11);
        browser.setBackgroundColor(Color.argb(0, 0, 0, 0));
        WebSettings settings = browser.getSettings();
        settings.setTextZoom(115);
        browser.loadUrl("http://numbersapi.com/" + score);

        ImageView medal = findViewById(R.id.medal0);
        int rank = getIntent().getIntExtra("rank", 1);
        switch(rank) {
            case 1:
                medal.setImageDrawable(getDrawable(R.drawable.medals_0));
            case 2:
                medal.setImageDrawable(getDrawable(R.drawable.medals_1));
            case 3:
                medal.setImageDrawable(getDrawable(R.drawable.medals_2));
            case 4:
                medal.setImageDrawable(getDrawable(R.drawable.medals_3));
        }

        button = (ImageButton) findViewById(R.id.startbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openMainActivity();
                finish();
            }
        });

        goTopListButton = (ImageButton) findViewById(R.id.listbutton);
        goTopListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(result.this, TopUserList.class);
                startActivity(intent);
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}