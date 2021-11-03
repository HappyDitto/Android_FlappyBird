package com.example.flappy_bird_basic.items;

import java.util.Random;

public class Chromosome {
    private double threshold;
    private double weight1;
    private double weight2;
    private double weight3;

    public Chromosome() {

    }

    public Chromosome(double threshold, double weight1, double weight2, double weight3) {
        this.threshold = threshold;
        this.weight1 = weight1;
        this.weight2 = weight2;
        this.weight3 = weight3;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getWeight1() {
        return weight1;
    }

    public void setWeight1(double weight1) {
        this.weight1 = weight1;
    }

    public double getWeight2() {
        return weight2;
    }

    public void setWeight2(double weight2) {
        this.weight2 = weight2;
    }

    public double getWeight3() {
        return weight3;
    }

    public void setWeight3(double weight3) {
        this.weight3 = weight3;
    }

    public Chromosome crossover(Chromosome parentB) {
        Chromosome newChro = new Chromosome();
        newChro.setThreshold(this.threshold);
        newChro.setWeight1(parentB.getWeight1());
        newChro.setWeight2(this.weight2);
        newChro.setWeight3(parentB.getWeight3());

        return newChro;
    }

    public void autoGenerate(int weightIndex) {
        Random random = new Random();
        switch (weightIndex) {
            case 0:
                this.threshold = 100 + random.nextInt(2000);
                break;
            case 1:
                this.weight1 = -1 + 2 * random.nextDouble();
                break;
            case 2:
                this.weight2 = -1 + 2 * random.nextDouble();
                break;
            case 3:
                this.weight3 = -1 + 2 * random.nextDouble();
                break;
        }
    }

    public void autoGenerate() {
        Random random = new Random();

        this.threshold = 100 + random.nextInt(2000);
        this.weight1 = -1 + 2 * random.nextDouble();
        this.weight2 = -1 + 2 * random.nextDouble();
        this.weight3 = -1 + 2 * random.nextDouble();
    }


    public void mutation() {
        Random random = new Random();
        if (random.nextBoolean()) {
            int selector = random.nextInt(5);

            autoGenerate(selector);
        }
    }

}
