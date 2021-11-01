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
import android.widget.TextView;

public class result extends AppCompatActivity {
    private ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

//        Intent reintent = getIntent();
//        int score = reintent.getIntExtra("score", 0);
//        int bestscore = reintent.getIntExtra("bestscore", 0);

        TextView RefreshTextObject = (TextView) findViewById(R.id.scoretext);
        RefreshTextObject.setText(String.valueOf(onescore));
        TextView RefreshbestTextObject = (TextView) findViewById(R.id.bestscoretext);
        RefreshbestTextObject.setText(String.valueOf(bestscore));

        //WebView
        WebView browser=(WebView)findViewById(R.id.webView);
//        TextView thistext = (TextView) findViewById(R.id.scoretext);
//        int thisscore = Integer.parseInt(thistext.getText().toString());

        browser.setBackgroundResource(R.drawable.number1);
        browser.setBackgroundColor(Color.argb(0, 0, 0, 0));
        WebSettings settings = browser.getSettings();
        settings.setTextZoom(115);
        browser.loadUrl("http://numbersapi.com/" + onescore);

        button = (ImageButton) findViewById(R.id.startbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMainActivity();
            }
        });
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}