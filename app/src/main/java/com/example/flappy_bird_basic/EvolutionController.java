package com.example.flappy_bird_basic;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.example.flappy_bird_basic.items.AIBird;
import com.example.flappy_bird_basic.items.Chromosome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EvolutionController {
    public static final int POP_NUM = 100;
    private int generation;
    private int hurdle;
    private HashMap<AIBird, Float> rouletteWheel;
    private ArrayList<AIBird> population;
    private ArrayList<Chromosome> offspring;
    private TrainAIView view;

    public EvolutionController(TrainAIView view, Bitmap[] shape, int x, int y, int hurdle) {
        generation = 0;
        this.hurdle = hurdle;
        rouletteWheel = new HashMap<>();
        population = new ArrayList<>();
        offspring = new ArrayList<>();
        initPopulation(shape, x, y);
        this.view = view;
    }

    public void run(int respawnY) {
        int deathCounter = 0;
        for (AIBird pop: population) {
            if (pop.isDead()) {
                deathCounter += 1;
            }
        }

        if (deathCounter >= 0.9*POP_NUM) {
            fitnessAnalysis();

            offspring.clear();

            for (int i = 0; i < Math.ceil((double) deathCounter/2); i++) {
                Chromosome parentA = parentSelection();
                Chromosome parentB = parentSelection();
                while (parentA.equals(parentB)) {
                    parentB = parentSelection();
                }

                breed(parentA, parentB);
            }

            survivorSelection(respawnY);

            generation++;
            Log.d("Evolution", "Generation " + generation);
            view.resetGame();
        }

        Termination();
    }

    public void initPopulation(Bitmap[] shape, int x, int y) {
        while (population.size() < POP_NUM) {
            Chromosome chro = new Chromosome();
            chro.autoGenerate();

            AIBird bird = new AIBird(shape, x, y, chro);
            population.add(bird);
        }
    }

    public void fitnessAnalysis() {
        int totalScore = 0;
        for (AIBird pop : population) {
            totalScore += pop.getTravelDistance();
        }

        for (AIBird pop: population) {
            float possibility = (float) pop.getTravelDistance() / totalScore;
            rouletteWheel.put(pop, possibility);

        }
    }

    public Chromosome parentSelection() {
        Chromosome parent = null;

        while (parent == null) {
            float pointer = new Random().nextFloat();

            for (Map.Entry<AIBird, Float> entry : rouletteWheel.entrySet()) {
                pointer -= entry.getValue();
                if (pointer <= 0) {
                    parent = entry.getKey().getChromosome();
                    break;
                }
            }
        }

        return parent;
    }

    public void breed(Chromosome parentA, Chromosome parentB) {
        Chromosome chro1 = parentA.crossover(parentB);
        chro1.mutation();

        Chromosome chro2 = parentB.crossover(parentA);
        chro2.mutation();

        offspring.add(chro1);
        offspring.add(chro2);
    }

    public void survivorSelection(int respawnY) {
        int index = 0;
        Random random = new Random();
        for (AIBird pop : population) {
            if (pop.isDead()) {
                if (index < offspring.size()) {
                    pop.setChromosome(offspring.get(index));
                    index++;
                }
                else {
                    pop.setChromosome(offspring.get(random.nextInt(offspring.size())));
                }
                pop.reset(respawnY);
            }
        }
    }

    public void Termination() {
        for (AIBird pop: population) {
            if (pop.getScore() > hurdle) {
                Log.d("Evolution", "Hurdle " + hurdle + ": " +
                        "Threshold: " + pop.getChromosome().getThreshold() +
                        ", Weight1: " + pop.getChromosome().getWeight1() +
                        ", Weight2: " + pop.getChromosome().getWeight2() +
                        ", Weight3: " + pop.getChromosome().getWeight3());
                return;
            }
        }
    }


    public ArrayList<AIBird> getPopulation() {
        return population;
    }


}
