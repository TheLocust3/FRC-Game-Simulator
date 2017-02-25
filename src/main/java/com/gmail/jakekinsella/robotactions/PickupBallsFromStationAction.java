package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.field.Gear;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.manipulators.ballsmanipulators.BallsPickupStationManipulator;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class PickupBallsFromStationAction extends TimeAction {

    private double successChance;

    private ArrayList<Ball> balls = new ArrayList<>();

    public PickupBallsFromStationAction(Command command, BallsPickupStationManipulator manipulator, int time, double successChance) {
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
            for (int i = 0; i < 30; i++) { // TODO: Change the number of balls recieved from player station
                boolean score = (Math.random() >= (1 - this.successChance));
                if (score) {
                    this.balls.add(new Ball(0, 0));
                }
            }
        }

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
