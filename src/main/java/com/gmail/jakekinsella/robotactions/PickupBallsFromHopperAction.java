package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.manipulators.ballsmanipulators.BallsPickupHopperManipulator;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class PickupBallsFromHopperAction extends TimeAction {

    private double successChance;

    private ArrayList<Ball> balls = new ArrayList<>();

    public PickupBallsFromHopperAction(Command command, BallsPickupHopperManipulator manipulator, int time, double successChance) {
        super(command, manipulator, time);

        this.successChance = successChance;
    }

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    @Override
    public void actionDone() {
        this.success = true;

        if (this.manipulator.isInRange()) {
            for (int i = 0; i < 50; i++) {
                boolean score = (Math.random() >= (1 - this.successChance));
                if (score) {
                    this.balls.add(new Ball(0, 0));
                } else {
                    // TODO: Spawn balls by the hopper
                }
            }
        }

        // TODO: Make hopper empty

        this.manipulator.actionFinish();
    }

    @Override
    public void actionStart() {
        if (!this.manipulator.isInRange()) {
            success = false;
            this.manipulator.actionFinish();
        }
    }

    @Override
    String getSuccessString() {
        return "has picked up " + this.balls.size() + " balls";
    }

    @Override
    String getFailureString() {
        return "";
    }
}
