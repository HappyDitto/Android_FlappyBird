package com.example.flappy_bird_basic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.Display;
import android.graphics.Point;

public class GameView extends View {

    // This is our custom view class
    Handler handler; // Handler is required to schedule a runnable after some delay
    Runnable runnable;
    final int UPDATE_MILLIS = 30;
    Bitmap background;
    Display display;


    public GameView(Context context) {
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate(); // this will call onDraw
            }
        };
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // we will draw our view inside onDraw()
        // draw the background on canvas
        canvas.drawBitmap(background, 0, 0, null);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
}
