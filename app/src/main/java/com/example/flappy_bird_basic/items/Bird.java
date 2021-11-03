package com.example.flappy_bird_basic.items;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.flappy_bird_basic.R;

import java.util.ArrayList;

public class Bird {
    protected Bitmap[] shape;
    protected int x, y, velocity;
    protected int currentFrameIndex;
    protected int score;
    protected ArrayList<Integer> passedTubes;
    protected boolean isDead;

    public Bird(Bitmap[] shape, int x, int y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.currentFrameIndex = 0;
        this.score = 0;
        this.passedTubes = new ArrayList<>();
    }

    public Bitmap getCurrentFrame(boolean goNext) {
        if (goNext)
            currentFrameIndex = 1 - currentFrameIndex;

        return shape[currentFrameIndex];
    }

    public void jump(double value, double factor) {
        velocity = (int) (value * factor);
    }

    public void addScore(int value) {
        score += value;
    }

    public void changeVelocity(int value) {
        velocity += value;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() { return score; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getVelocity() {return velocity;}

    public void addPassedTube(int id) {
        passedTubes.add(id);
    }

    public ArrayList<Integer> getPassedTube() {return passedTubes;}

    public boolean isDead() {
        return isDead;
    }

    public void setIsDead(boolean dead) {
        isDead = dead;
    }
}
