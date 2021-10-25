package com.example.flappy_bird_basic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.Display;
import android.graphics.Point;

import java.util.Random;

public class GameView extends View {

    // This is our custom view class
    Handler handler; // Handler is required to schedule a runnable after some delay
    Runnable runnable;
    final int UPDATE_MILLIS = 30;
    Bitmap background;
    Bitmap toptube, bottomtube;
    Display display;
    Point point;
    int dWidth, dHeight; // Device width and height respectively
    Rect rect;
    // Bitmap array for the bird
    Bitmap[] birds;
    // We need an integer variable to keep track of bird image/frame
    int birdFrame = 0;
    int velocity = 0, gravity = 3;
    // keep track of the bird position
    int birdX, birdY;
    boolean gameState = false;
    int gap = 400; // Gap between top tube and the bottom tube
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] topTubeY = new int[numberOfTubes];
    Random random;
    int tubeVelocity = 8;



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
        toptube = BitmapFactory.decodeResource(getResources(),R.drawable.toptube);
        bottomtube = BitmapFactory.decodeResource(getResources(), R.drawable.bottomtube);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth, dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.bird_1);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.bird_2);

        // With bird in center at the beginning
        birdX = dWidth/2 - birds[0].getWidth()/2;
        birdY = dHeight/2 - birds[0].getHeight()/2;
        distanceBetweenTubes = dWidth*3/4; // our assumption
        minTubeOffset = gap/2;
        maxTubeOffset = dHeight - minTubeOffset - gap;
        random = new Random();

        for (int i = 0; i<numberOfTubes; i++){
            // to start off, tube should come from the right edge of the screen
            tubeX[i] = dWidth + i*distanceBetweenTubes;
            // Y axis of top tube would vary between maxTubeOffset and minTubeOffset
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
        }



    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // we will draw our view inside onDraw()
        // draw the background on canvas
        //canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(background,null, rect, null); // fixed
        if(birdFrame == 0){
            birdFrame = 1;
        } else {
            birdFrame = 0;
        }
        if(gameState) {

            // keep the bird on the screen (doesn't go beyond boundary)
            if (birdY < dHeight - birds[0].getHeight() || velocity < 0) {
                //As the bird falls, it gets faster and faster
                // as the velocity value increments by gravity each time
                velocity += gravity;
                birdY += velocity;
            }
            for (int i = 0; i < numberOfTubes; i++) {
                tubeX[i] -= tubeVelocity;
                if (tubeX[i] < -toptube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                }
                canvas.drawBitmap(toptube, tubeX[i], topTubeY[i] - toptube.getHeight(), null);
                canvas.drawBitmap(bottomtube, tubeX[i], topTubeY[i] + gap, null);
            }
        }

        // Display bird at the center of the screen
        // bird_1 and bird_2 have the same dimension
        canvas.drawBitmap(birds[birdFrame], birdX, birdY, null );
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    // get the touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){ // that is tap is detected on screen
            // move bird upward by some unit
            velocity = -30; // move 30 units upward
            gameState = true;
        }
        return true; // By returning ture, it indicates that we have done with touch event and no further action needed
    }
}
