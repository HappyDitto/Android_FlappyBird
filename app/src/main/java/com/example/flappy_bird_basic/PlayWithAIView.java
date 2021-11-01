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
import android.view.Display;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;
import static com.example.flappy_bird_basic.MainActivity.bestscore;
import static com.example.flappy_bird_basic.StartGame.onescore;

import com.example.flappy_bird_basic.items.Bird;
import com.example.flappy_bird_basic.items.Tube;

import utils.InGameUtils;
import utils.Utils;

public class PlayWithAIView extends View {

    public static final int MIN_GAP = 300;
    public static final int MAX_GAP = 650;
    public static final int MIN_OFFSET = 500;
    public static final int MAX_OFFSET = 1750;
    public static final int TUBE_VELOCITY = 8;
    public static final int INIT_TUBE_NUM = 4;
    public static final int GRAVITY = 3;

    // get the light intensity from the main activity
    int light = MainActivity.light_intensity;

    // define multiple background
    Bitmap background;
    Bitmap background_night;
    Bitmap background_day;
    Bitmap background_magic;
    Bitmap background_temple;

    Bitmap over_pic;

    // displays
    int dWidth, dHeight; // Device width and height respectively
    Rect rect;

    public static boolean gameState;

    Bird player;

    // tubes
    Bitmap toptube_shape;
    Bitmap bottube_shape;
    ArrayList<Tube> tubes;

    Random random;

    boolean createNew;

    public PlayWithAIView(Context context) {
        super(context);

        tubes = new ArrayList<>();
        gameState = true;
        createNew = false;

        // load background pictures
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        background_night = BitmapFactory.decodeResource(getResources(), R.drawable.backgroud2);
        background_day = BitmapFactory.decodeResource(getResources(), R.drawable.bg_day);
        background_magic = BitmapFactory.decodeResource(getResources(), R.drawable.bg_magic);
        background_temple = BitmapFactory.decodeResource(getResources(), R.drawable.bg_temple);

        over_pic = BitmapFactory.decodeResource(getResources(), R.drawable.end);
        toptube_shape = BitmapFactory.decodeResource(getResources(),R.drawable.toptube_extended);
        bottube_shape = BitmapFactory.decodeResource(getResources(), R.drawable.bottomtube_extended);

        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth, dHeight);

        Bitmap[] birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.bird_1);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.bird_2);
        player = new Bird(birds, dWidth/2 - birds[0].getWidth()/2, dHeight/2 - birds[0].getHeight()/2);

        // game end picture
        random = new Random();

        for (int i = 0; i<INIT_TUBE_NUM; i++){
            int distBtwTubes = random.nextInt((int)(dWidth*3/4 - dWidth/4)) + (int)(dWidth/4);
            int x = i == 0 ? dWidth : tubes.get(i-1).getX() + distBtwTubes;
            int gap = MIN_GAP + random.nextInt(MAX_GAP-MIN_GAP);
            int topY = MIN_OFFSET + random.nextInt(MAX_OFFSET - MIN_OFFSET + 1) - toptube_shape.getHeight();
            int botY = topY + toptube_shape.getHeight() + gap;
            Tube tube = new Tube(x, topY, botY, gap, toptube_shape, bottube_shape);
            tubes.add(tube);
        }
    }


    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        // we will draw our view inside onDraw()

        // draw the background on canvas
        canvas.drawBitmap(background,null, rect, null);

        Log.d("player Y", "" + player.getY());
        Log.d("Limit", ""+(dHeight));

        if(gameState) {

            if (player.getY() < dHeight || player.getVelocity() < 0) {
                //As the bird falls, it gets faster and faster
                // as the velocity value increments by gravity each time
                player.changeVelocity(GRAVITY);
                player.setPosition(player.getX(), player.getY() + player.getVelocity());
            }

            if (createNew) {
                int distBtwTubes = random.nextInt((int)(dWidth*3/4 - dWidth/4)) + (int)(dWidth/4);
                int x = tubes.get(tubes.size()-1).getX() + distBtwTubes;
                int gap = MIN_GAP + random.nextInt(MAX_GAP-MIN_GAP);
                int topY = MIN_OFFSET + random.nextInt(MAX_OFFSET - MIN_OFFSET + 1) - toptube_shape.getHeight();
                int botY = topY + toptube_shape.getHeight() + gap;

                Tube tube = new Tube(x, topY, botY, gap, toptube_shape, bottube_shape);
                tubes.add(tube);
                tubes.remove(0);
                createNew = false;
            }

            for (Tube tube : tubes) {

                tube.moveTube(-TUBE_VELOCITY);
                if (tube.getX() < -tube.getTopTubeShape().getWidth()){
                    createNew = true;
                }

                canvas.drawBitmap(tube.getTopTubeShape(), tube.getX(), tube.getTopY(), null);
                canvas.drawBitmap(tube.getBottomTubeShape(), tube.getX(), tube.getBotY(), null);

                if (InGameUtils.isCollided(player.getCurrentFrame(false), player.getX(), player.getY(), tube.getTopTubeShape(), tube.getX(), tube.getTopY()) ||
                        InGameUtils.isCollided(player.getCurrentFrame(false), player.getX(), player.getY(), tube.getBottomTubeShape(), tube.getX(), tube.getBotY()) ||
                        player.getY() < -player.getCurrentFrame(false).getHeight() ||
                        player.getY() >= dHeight) {
                    gameState = false;
                    Log.d("boom", "collision detected");
                }

                if (player.getX() > tube.getX() && !player.getPassedTube().contains(tube.getId())) {
                    player.addPassedTube(tube.getId());
                    player.addScore(1);
                }

                // add score to the background
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(60);
                canvas.drawText("Score: " + String.valueOf(player.getScore()),dWidth-300,50,paint);
                if(player.getScore() > onescore){
                    onescore = player.getScore();
                }
            }

        }else{
            int endX = dWidth/2 - over_pic.getWidth()/2;
            int endY = dHeight/2 - over_pic.getHeight()/2;

            canvas.drawBitmap(over_pic, endX, endY, null);

            //add bestscore
            if(onescore > bestscore){
                bestscore = onescore;
            }
        }

        // Display bird at the center of the screen
        // bird_1 and bird_2 have the same dimension
        canvas.drawBitmap(player.getCurrentFrame(true), player.getX(), player.getY(), null);
    }

    // get the touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){ // that is tap is detected on screen

            if (gameState = true) {
                // move bird upward by some unit
                // move 30 units upward, but with stronger light, it can have a stronger jump ability.
                player.jump(-25, ( 1 + (int) (light/1000)));
            }
        }
        return true; // By returning ture, it indicates that we have done with touch event and no further action needed
    }
}
