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

import com.example.flappy_bird_basic.items.AIBird;
import com.example.flappy_bird_basic.items.Bird;
import com.example.flappy_bird_basic.items.Tube;

import utils.InGameUtils;
import utils.Utils;

public class TrainAIView extends View {

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

    EvolutionController evoControl;
    AIBird bird;
//    QLearningController qLearningController;

    // tubes
    Bitmap toptube_shape;
    Bitmap bottube_shape;
    ArrayList<Tube> tubes;

    Random random;

    boolean createNew;

    public TrainAIView(Context context) {
        super(context);

        setWillNotDraw(false);

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
        bird = new AIBird(birds, dWidth/2 - birds[0].getWidth()/2,dHeight/2 - birds[0].getHeight()/2);
//        qLearningController = new QLearningController(bird, 0.1, 0.7, 20, 10);
//        qLearningController.initQTable();
        evoControl = new EvolutionController(this, birds, dWidth/2 - birds[0].getWidth()/2,dHeight/2 - birds[0].getHeight()/2, 20);

        // game end picture
        random = new Random();

        for (int i = 0; i<INIT_TUBE_NUM; i++){
            int distBtwTubes = random.nextInt(dWidth*3/4 - dWidth/2) + dWidth/2;
            int x = i == 0 ? dWidth : tubes.get(i-1).getX() + distBtwTubes;
            int gap = MIN_GAP + random.nextInt(MAX_GAP-MIN_GAP);
            int topY = MIN_OFFSET + random.nextInt(MAX_OFFSET - MIN_OFFSET + 1) - toptube_shape.getHeight();
            int botY = topY + toptube_shape.getHeight() + gap;
            Tube tube = new Tube(x, topY, botY, gap, toptube_shape, bottube_shape);
            tubes.add(tube);
        }
    }

    public void resetGame() {
        tubes.clear();
        createNew = false;

        random = new Random();

        for (int i = 0; i<INIT_TUBE_NUM; i++){
            int distBtwTubes = random.nextInt(dWidth*3/4 - dWidth/4) + (dWidth/4);
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

        canvas.drawBitmap(background,null, rect, null);

        if(gameState) {
            if (createNew) {
                int distBtwTubes = random.nextInt(dWidth*3/4 - dWidth/4) + dWidth/4;
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
            }

            Tube next = null;
            for (Tube tube: tubes) {
                if (tube.getX() + toptube_shape.getWidth() > bird.getX()) {
                    if (next == null) {
                        next = tube;
                    } else{
                        if (tube.getX() < next.getX())
                            next = tube;
                    }
                }
            }

            /** QLearning - Deprecated **/
//            if (bird.isDead()) {
//                bird.reset(next.getTopY() + next.getTopTubeShape().getHeight() + next.getGap()/2);
//            }
//
//            bird.addTravelDistance(TUBE_VELOCITY);
//
//            int farFromTube = Math.abs(next.getX() - bird.getX());
//            int heightFromTopTube = Math.abs(next.getTopY() + next.getTopTubeShape().getHeight() - bird.getY());
//            int heightFromBotTube = Math.abs(next.getBotY() - bird.getY());
//            boolean jumpOrNot = qLearningController.run(bird,farFromTube,heightFromTopTube,heightFromBotTube);
//            if (jumpOrNot) {
//                bird.jump(25, 1);
//            }
//
//            if (bird.getY() < dHeight || bird.getVelocity() < 0) {
//                //As the bird falls, it gets faster and faster
//                // as the velocity value increments by gravity each time
//                bird.changeVelocity(GRAVITY);
//                bird.setPosition(bird.getX(), bird.getY() + bird.getVelocity());
//            }
//
//            for (Tube tube : tubes) {
//                if (InGameUtils.isCollided(bird.getCurrentFrame(false), bird.getX(), bird.getY(), tube.getTopTubeShape(), tube.getX(), tube.getTopY()) ||
//                        InGameUtils.isCollided(bird.getCurrentFrame(false), bird.getX(), bird.getY(), tube.getBottomTubeShape(), tube.getX(), tube.getBotY()) ||
//                        bird.getY() < -bird.getCurrentFrame(false).getHeight() ||
//                        bird.getY() >= dHeight) {
//                    bird.setIsDead(true);
//                }
//
//                if (bird.getX() > tube.getX() && !bird.getPassedTube().contains(tube.getId())) {
//                    bird.addPassedTube(tube.getId());
//                    bird.addScore(1);
//                }
//            }
//
//            canvas.drawBitmap(bird.getCurrentFrame(true), bird.getX(), bird.getY(), null);

            /** Genetic Algorithm **/
            for (Tube tube: tubes) {
                if (tube.getX() > evoControl.getPopulation().get(0).getX()) {
                    if (next == null) {
                        next = tube;
                    } else{
                        if (tube.getX() < next.getX())
                            next = tube;
                    }
                }
            }

            for (AIBird bird : evoControl.getPopulation()) {
                if (bird.isDead())
                    continue;

                bird.addTravelDistance(TUBE_VELOCITY);

                int farFromTube = next.getX() - bird.getX();
                int heightFromTop = next.getTopY()+toptube_shape.getHeight()-bird.getY();
                int heightFromBot = next.getBotY()-bird.getY();
                bird.doAction(farFromTube, heightFromTop, heightFromBot);

                if (bird.getY() < dHeight || bird.getVelocity() < 0) {
                    //As the bird falls, it gets faster and faster
                    // as the velocity value increments by gravity each time
                    bird.changeVelocity(GRAVITY);
                    bird.setPosition(bird.getX(), bird.getY() + bird.getVelocity());
                }

                for (Tube tube : tubes) {
                    if (InGameUtils.isCollided(bird.getCurrentFrame(false), bird.getX(), bird.getY(), tube.getTopTubeShape(), tube.getX(), tube.getTopY()) ||
                            InGameUtils.isCollided(bird.getCurrentFrame(false), bird.getX(), bird.getY(), tube.getBottomTubeShape(), tube.getX(), tube.getBotY()) ||
                            bird.getY() < -bird.getCurrentFrame(false).getHeight() ||
                            bird.getY() >= dHeight) {
                        bird.setIsDead(true);
                    }

                    if (bird.getX() > tube.getX() && !bird.getPassedTube().contains(tube.getId())) {
                        bird.addPassedTube(tube.getId());
                        bird.addScore(1);
                    }
                }

                canvas.drawBitmap(bird.getCurrentFrame(true), bird.getX(), bird.getY(), null);
            }

            evoControl.run(next.getTopY() + next.getTopTubeShape().getHeight() + next.getGap()/2);

        }
        else {
            int endX = dWidth/2 - over_pic.getWidth()/2;
            int endY = dHeight/2 - over_pic.getHeight()/2;

            canvas.drawBitmap(over_pic, endX, endY, null);

            //add bestscore
            if(onescore > bestscore){
                bestscore = onescore;
            }
        }
    }
}
