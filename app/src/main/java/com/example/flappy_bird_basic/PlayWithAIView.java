package com.example.flappy_bird_basic;

import android.app.Activity;
import android.content.SharedPreferences;
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
import static com.example.flappy_bird_basic.StartPlayWithAI.onescore;
import static com.example.flappy_bird_basic.StartPlayWithAI.rank;

import com.example.flappy_bird_basic.items.AIBird;
import com.example.flappy_bird_basic.items.Bird;
import com.example.flappy_bird_basic.items.Tube;

//import org.checkerframework.checker.units.qual.A;

import utils.InGameUtils;
import utils.Utils;

public class PlayWithAIView extends View {

    public static int MIN_GAP = 300;
    public static int MAX_GAP = 650;
    public static int MIN_OFFSET = 500;
    public static int MAX_OFFSET = 1750;
    public static final int TUBE_VELOCITY = 8;
    public static final int INIT_TUBE_NUM = 4;
    public static final int EASY_BOT_NUM = 1;
    public static final int NORMAL_BOT_NUM = 1;
    public static final int HARD_BOT_NUM = 1;
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

    public boolean gameState;

    Bird player;
    ArrayList<Bird> allBirds;

    // tubes
    Bitmap toptube_shape;
    Bitmap bottube_shape;
    ArrayList<Tube> tubes;

    Random random;

    boolean createNew;

    public PlayWithAIView(Context context, int mode) {
        super(context);

        tubes = new ArrayList<>();
        allBirds = new ArrayList<>();
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

        MAX_GAP = dHeight/4;
        MIN_GAP = dHeight/6;
        MAX_OFFSET = 1500-dHeight/10;
        MIN_OFFSET = 500-dHeight/10;

        // game end picture
        random = new Random();

        for (int i = 0; i<INIT_TUBE_NUM; i++){
            createNewTube();
        }

        if (mode == StartPlayWithAI.AI_MODE) {
            Bitmap[] botBirds = new Bitmap[2];
            botBirds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird_bot_1);
            botBirds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird_bot_2);

            for (int i = 0; i < EASY_BOT_NUM; i++) {
                AIBird bot = new AIBird(botBirds, dWidth / 2 - botBirds[0].getWidth() / 2,
                        dHeight / 2 - botBirds[0].getHeight() / 2, AIBird.LEVEL.EASY);
                allBirds.add(bot);
            }
            for (int i = 0; i < NORMAL_BOT_NUM; i++) {
                AIBird bot = new AIBird(botBirds, dWidth / 2 - botBirds[0].getWidth() / 2,
                        dHeight / 2 - botBirds[0].getHeight() / 2, AIBird.LEVEL.NORMAL);
                allBirds.add(bot);
            }
            for (int i = 0; i < HARD_BOT_NUM; i++) {
                AIBird bot = new AIBird(botBirds, dWidth / 2 - botBirds[0].getWidth() / 2,
                        dHeight / 2 - botBirds[0].getHeight() / 2, AIBird.LEVEL.HARD);
                allBirds.add(bot);
            }
        }

        Bitmap[] birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.bird_1);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.bird_2);
        player = new Bird(birds, dWidth/2 - birds[0].getWidth()/2, dHeight/2 - birds[0].getHeight()/2);
        allBirds.add(player);
    }

    // a method to create new tube and add it to tube list
    public void createNewTube() {
        int distBtwTubes = random.nextInt(dWidth*3/4 - dWidth/2) + dWidth/2;
        int x = tubes.size() == 0 ? dWidth : tubes.get(tubes.size()-1).getX() + distBtwTubes;
        int gap = MIN_GAP + random.nextInt(MAX_GAP-MIN_GAP);
        int topY = MIN_OFFSET + random.nextInt(MAX_OFFSET - MIN_OFFSET + 1) - toptube_shape.getHeight();
        int botY = topY + toptube_shape.getHeight() + gap;

        Tube tube = new Tube(x, topY, botY, gap, toptube_shape, bottube_shape);
        tubes.add(tube);
    }


    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // draw the background on canvas
        canvas.drawBitmap(background,null, rect, null);

        if(gameState) {
            // if createNew flag is raised, it means a tube has been out of boundary,
            // we should generate a new tube and remove the tube out of boundary.
            if (createNew) {
                createNewTube();
                tubes.remove(0);
                createNew = false;
            }

            // keep a reference of next tube
            Tube next = null;
            for (Tube tube : tubes) {
                // track the next tube
                if (tube.getX() + toptube_shape.getWidth() > player.getX()) {
                    if (next == null) {
                        next = tube;
                    } else{
                        if (tube.getX() < next.getX())
                            next = tube;
                    }
                }

                // move tube
                tube.moveTube(-TUBE_VELOCITY);
                if (tube.getX() < -tube.getTopTubeShape().getWidth()){
                    createNew = true;
                }

                // draw tube
                canvas.drawBitmap(tube.getTopTubeShape(), tube.getX(), tube.getTopY(), null);
                canvas.drawBitmap(tube.getBottomTubeShape(), tube.getX(), tube.getBotY(), null);
            }

            // go through all birds including player and bots
            for (Bird bird : allBirds) {
                if (bird.getY() < dHeight || bird.getVelocity() < 0) {
                    //As the bird falls, it gets faster and faster
                    // as the velocity value increments by gravity each time
                    bird.changeVelocity(GRAVITY);
                    bird.setPosition(bird.getX(), bird.getY() + bird.getVelocity());
                }

                // if the current bird is a bot, let it do some action
                if (bird instanceof AIBird) {
                    int farFromTube = next.getX() - bird.getX();
                    int heightFromTop = next.getTopY() + toptube_shape.getHeight() - bird.getY();
                    int heightFromBot = next.getBotY() - bird.getY();
                    ((AIBird) bird).doAction(farFromTube, heightFromTop, heightFromBot);
                }

                // check if the bird collide with or pass through any tube
                for (Tube tube : tubes) {
                    if (InGameUtils.isCollided(bird.getCurrentFrame(false), bird.getX(), bird.getY(), tube.getTopTubeShape(), tube.getX(), tube.getTopY()) ||
                            InGameUtils.isCollided(bird.getCurrentFrame(false), bird.getX(), bird.getY(), tube.getBottomTubeShape(), tube.getX(), tube.getBotY()) ||
                            bird.getY() < -bird.getCurrentFrame(false).getHeight() ||
                            bird.getY() >= dHeight) {
                        bird.setIsDead(true);
                        if (!(bird instanceof AIBird)) {
                            gameState = false;

                            onescore = player.getScore();

                            rank = 1;
                            for (Bird temp: allBirds) {
                                if (!temp.isDead()) {
                                    rank += 1;
                                }
                            }

                            //add bestscore
                            if(onescore > bestscore){
                                saveInfo("best_score", onescore);
                                bestscore = onescore;
                            }
                        }
                    }


                    if (bird.getX() > tube.getX() && !bird.getPassedTube().contains(tube.getId())) {
                        bird.addPassedTube(tube.getId());
                        bird.addScore(1);
                    }
                }

                if (!bird.isDead())
                    canvas.drawBitmap(bird.getCurrentFrame(true), bird.getX(), bird.getY(), null);
            }

            // add score to the background
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(60);
            canvas.drawText("Score: " + player.getScore(), dWidth - 300, 50, paint);
        }

        else{
            int endX = dWidth/2 - over_pic.getWidth()/2;
            int endY = dHeight/2 - over_pic.getHeight()/2;

            canvas.drawBitmap(over_pic, endX, endY, null);
        }
    }

    public void saveInfo(String key, int value) {
        SharedPreferences userInfo = getContext().getSharedPreferences("BEST_SCORE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // get the touch event
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN){ // that is tap is detected on screen

            if (gameState = true) {
                // move bird upward by some unit
                // move 30 units upward, but with stronger light, it can have a stronger jump ability.
                player.jump(-25, ( 1 + light/1000));
            }
        }
        return true; // By returning ture, it indicates that we have done with touch event and no further action needed
    }
}
