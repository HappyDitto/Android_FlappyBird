package com.example.flappy_bird_basic;

import com.example.flappy_bird_basic.items.AIBird;
import com.google.common.collect.TreeBasedTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@Deprecated
public class QLearningController {
    private AIBird bird;
    private double stepSize;
    private double discount;
    private State prevState;
    private boolean prevAction;
    private int hurdle;
    private int scaleFactor;
    private TreeBasedTable<State, Boolean, Double> QTable;

    public QLearningController(AIBird bird, double stepSize, double discount, int hurdle, int scaleFactor) {
        this.bird = bird;
        this.stepSize = stepSize;
        this.discount = discount;
        this.hurdle = hurdle;
        this.scaleFactor = scaleFactor;
        this.QTable = TreeBasedTable.create();
    }

    public void initQTable() {
        for (int i = 0; i < (500+scaleFactor)/scaleFactor; i++) {
            for (int j = 0; j < (500+scaleFactor)/scaleFactor; j++) {
                for (int k = 0; k < (500+scaleFactor)/scaleFactor; k++) {
                    State s = new State(i, j, k);
                    QTable.put(s, true, 0.);
                    QTable.put(s, false, 0.);
                }
            }
        }
    }

    public boolean run(AIBird bird, int horiDistFromTube, int distFromTopTube, int distFromBotTube) {
        if (bird.getScore() > hurdle) {
            // store or log QTable
        }

        int horiDistFromTubeScaled = horiDistFromTube > 500 ? 500/scaleFactor : horiDistFromTube/scaleFactor;
        int distFromTopTubeScaled = distFromTopTube > 500 ? 500/scaleFactor : distFromTopTube/scaleFactor;
        int distFromBotTubeScaled = distFromBotTube > 500 ? 500/scaleFactor : distFromBotTube/scaleFactor;

        State currentState = new State(horiDistFromTubeScaled, distFromTopTubeScaled, distFromBotTubeScaled);
        boolean jumpOrNot = QTable.get(currentState, true) > QTable.get(currentState, false);

        if (prevState != null) {
            int reward = bird.isDead() ? 1 : -1000;
            double experience = Math.max(QTable.get(currentState, true), QTable.get(prevState, false));
            double QValue =  (1-stepSize) *  QTable.get(prevState, prevAction) +
                    stepSize * (reward + discount * experience);

            // update QTable
            QTable.put(prevState, prevAction, QValue);
        }

        prevState = currentState;
        prevAction = jumpOrNot;

        return jumpOrNot;
    }

    private class State implements Comparable<State>{
        int horiDistFromTube;
        int distFromTopTube;
        int distFromBotTube;

        public State(int distFromBotTube, int distFromTopTube, int horiDistFromTube) {
            this.distFromBotTube = distFromBotTube;
            this.distFromTopTube = distFromTopTube;
            this.horiDistFromTube = horiDistFromTube;
        }

        public int getDistFromTopTube() {
            return distFromTopTube;
        }

        public void setDistFromTopTube(int distFromTopTube) {
            this.distFromTopTube = distFromTopTube;
        }

        public int getHoriDistFromTube() {
            return horiDistFromTube;
        }

        public void setHoriDistFromTube(int horiDistFromTube) {
            this.horiDistFromTube = horiDistFromTube;
        }


        public int getDistFromBotTube() {
            return distFromBotTube;
        }

        public void setDistFromBotTube(int distFromBotTube) {
            this.distFromBotTube = distFromBotTube;
        }


        @Override
        public int compareTo(State s) {
            if (this.horiDistFromTube > s.horiDistFromTube) return 1;
            if (this.horiDistFromTube < s.horiDistFromTube) return -1;

            if (this.distFromTopTube > s.distFromTopTube) return 1;
            if (this.distFromTopTube < s.distFromTopTube) return -1;

            if (this.distFromBotTube > s.distFromBotTube) return 1;
            if (this.distFromBotTube < s.distFromBotTube) return -1;

            return 0;
        }
    }
}
