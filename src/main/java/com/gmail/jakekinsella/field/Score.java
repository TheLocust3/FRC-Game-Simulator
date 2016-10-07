package com.gmail.jakekinsella.field;

/**
 * Created by jakekinsella on 10/7/16.
 */
public class Score {

    private int score;

    public Score() {
        this.score = 0;
    }

    public int getScore() {
        return this.score;
    }

    public void scoreHighgoal() {
        this.score += 5;
    }

    public void scoreLowgoal() {
        this.score += 2;
    }

    public void scoreDefenseCross() {
        this.score += 5;
    }
}
