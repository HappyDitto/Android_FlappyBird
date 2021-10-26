package com.example.flappy_bird_basic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.Display;
import android.graphics.Point;

import java.util.Date;
import java.util.Random;

public class GameView extends View {

    // This is our custom view class
    Handler handler; // Handler is required to schedule a runnable after some delay
    Runnable runnable;
    final int UPDATE_MILLIS = 30;

    int score = 0;

    // maps
    Bitmap background;
    Bitmap over_pic;
    Bitmap toptube, bottomtube;
    Bitmap[] birds; // Bitmap array for the bird

    // displays
    Display display;
    Point point;
    int dWidth, dHeight; // Device width and height respectively
    Rect rect;

    // We need an integer variable to keep track of bird image/frame
    int birdFrame = 0;
    int velocity = 0, gravity = 3;
    // keep track of the bird position
    int birdX, birdY;
    boolean gameState = true;

    // tubes
    int gap = 650; // Gap between top tube and the bottom tube
    int minTubeOffset, maxTubeOffset;
    int numberOfTubes = 4;
    int distanceBetweenTubes;
    int[] tubeX = new int[numberOfTubes];
    int[] topTubeY = new int[numberOfTubes];
    Random random;
    int tubeVelocity = 8;

    int endX, endY;


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
        over_pic = BitmapFactory.decodeResource(getResources(), R.drawable.end);
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

        // game end picture
        endX = dWidth/2 - over_pic.getWidth()/2;
        endY = dHeight/2 - over_pic.getHeight()/2;

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
        canvas.drawBitmap(background,null, rect, null);

        // bird can move its wings
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

            if (birdY >= dHeight - birds[0].getHeight()){
                gameState = false;
                score = 0;
            }

            for (int i = 0; i < numberOfTubes; i++) {

                tubeX[i] -= tubeVelocity;
                if (tubeX[i] < -toptube.getWidth()){
                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                }

                int bottomtubeY = topTubeY[i] + gap;

                canvas.drawBitmap(toptube, tubeX[i], topTubeY[i] - toptube.getHeight(), null);
                canvas.drawBitmap(bottomtube, tubeX[i], bottomtubeY, null);

                // top tube bounce back conditions
                if (birdY - birds[0].getHeight()/2 < topTubeY[i]  &&
                        (tubeX[i] < birdX + birds[0].getWidth()/2) &&
                (birdX + birds[0].getWidth()/2)< tubeX[i] + toptube.getWidth()){
                    birdX = tubeX[i] - birds[0].getWidth();
                }

                // bottom tube bounce back conditions
                if (birdY + birds[0].getHeight()/2 > bottomtubeY &&
                        (tubeX[i] < birdX + birds[0].getWidth()/2) &&
                        (birdX + birds[0].getWidth()/2)< tubeX[i] + toptube.getWidth()){
                    birdX = tubeX[i] - birds[0].getWidth();
                }

                // add score to the background
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(60);
                canvas.drawText("Score: " + String.valueOf(score),dWidth-300,50,paint);

            }

        }else{
            canvas.drawBitmap(over_pic, endX, endY, null);
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

            if (gameState = true) {
                // move bird upward by some unit
                velocity = -30; // move 30 units upward
                score += 1;
            }
        }
        return true; // By returning ture, it indicates that we have done with touch event and no further action needed
    }
}
