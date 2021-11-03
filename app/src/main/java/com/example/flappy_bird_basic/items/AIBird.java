package com.example.flappy_bird_basic.items;

import android.graphics.Bitmap;

public class AIBird extends Bird{

    private LEVEL level;
    private Chromosome chromosome;
    private boolean isDead;
    private int travelDistance;

    public AIBird(Bitmap[] shape, int x, int y) {
        super(shape, x, y);
        chromosome = new Chromosome(1, 1, 1, 1);
        isDead = false;
    }

    public AIBird(Bitmap[] shape, int x, int y, Chromosome chromosome) {
        super(shape, x, y);
        isDead = false;
        this.chromosome = chromosome;
    }

    public AIBird(Bitmap[] shape, int x, int y, LEVEL level) {
        super(shape, x, y);
        isDead = false;
        this.level = level;
        switch (level) {
            case EASY:
                this.chromosome = new Chromosome(
                        163,
                        -0.20236066354063453,
                        -0.9624847017030345,
                        -0.042926623574742306);
                break;
            case NORMAL:
                this.chromosome = new Chromosome(
                        134,
                        0.1038609466778957,
                        -0.9975443008572791,
                        -0.000765973574569534);
                break;
            case HARD:
                this.chromosome = new Chromosome(
                        157.0,
                        -0.33770466541754796,
                        -0.8358936261941812,
                        -0.11539050523276018);
                break;
        }

    }

    public void doAction(int farFromTube, int heightFromTop, int heightFromBot) {
        double result = chromosome.getWeight1() * farFromTube +
                     chromosome.getWeight2() * heightFromTop +
                     chromosome.getWeight3() * heightFromBot;

        if (result > chromosome.getThreshold()) {
            this.jump(-25, 1);
        }
    }

    public void reset(int respawnY) {
        this.isDead = false;
        this.travelDistance = 0;
        this.score = 0;
        this.velocity = 0;
        this.y = respawnY;
        this.passedTubes.clear();
    }

    public boolean isDead() {return isDead;}

    public void setIsDead(boolean dead) {this.isDead = dead;}

    public Chromosome getChromosome() { return chromosome; }

    public void setChromosome(Chromosome chromosome) {this.chromosome = chromosome;}

    public void addTravelDistance(int distance) {
        travelDistance += distance;
    }

    public int getTravelDistance() {
        return travelDistance;
    }

    public enum LEVEL {
        EASY,
        NORMAL,
        HARD
    }
}
