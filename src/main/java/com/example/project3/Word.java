package com.example.project3;

import java.util.Comparator;

//build the objects from csv file
public class Word/* implements Comparator<Word> */{
    private final String token;
    private int frequency;
    private double probability;

    public Word(String token, int frequency, double probability){
        this.token=token;
        this.frequency=frequency;
        this.probability=probability;
    }
    public Word(String token, double probability){
        this.token=token;
        this.probability=probability;
    }

    public String getToken() {
        return token;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

   /* @Override
    public int compareTo(Word word) {
        return this.getProbability().compareTo(word.getProbability());
    }*/

   /* @Override
    public int compare(Word o1, Word o2) {
        return 0;
    }*/
}

