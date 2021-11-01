package com.example.flappy_bird_basic.items;

import android.graphics.Bitmap;

public class Tube {
    private static int idCounter = 0;

    int id;
    int x, topY, botY, gap;
    Bitmap topTubeShape;
    Bitmap bottomTubeShape;
    boolean isPassed;

    public Tube(int x, int topY, int botY, int gap, Bitmap topTubeShape, Bitmap bottomTubeShape) {
        this.id = idCounter;
        idCounter++;
        this.x = x;
        this.topY = topY;
        this.botY = botY;
        this.gap = gap;
        this.topTubeShape = topTubeShape;
        this.bottomTubeShape = bottomTubeShape;
        this.isPassed = false;
    }

    public void moveTube(int value){
        x += value;
    }

    public int getX() {return x;}
    public int getTopY() {return topY;}
    public int getBotY() {return botY;}

    public int getGap() {
        return gap;
    }

    public Bitmap getBottomTubeShape() {
        return bottomTubeShape;
    }

    public Bitmap getTopTubeShape() {
        return topTubeShape;
    }

    public void setPassed(boolean passed) {
        this.isPassed = passed;
    }

    public boolean getPassed() {return isPassed;}

    public int getId() {return id;}

    public static int CreateId() {
        return idCounter++;
    }

    public void reuse(int id, int x, int topY, int botY, int gap) {
        this.id = id;
        this.x = x;
        this.topY = topY;
        this.botY = botY;
        this.gap = gap;
    }

}
