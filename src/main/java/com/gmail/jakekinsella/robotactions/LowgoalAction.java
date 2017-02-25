package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.manipulators.ballsmanipulators.BallsManipulator;
import com.gmail.jakekinsella.robot.manipulators.ballsmanipulators.LowgoalManipulator;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class LowgoalAction extends TimeAction {

    private double accuracy;
    private int ballsPerSecond;
    private int ballsScored;

    public LowgoalAction(Command command, LowgoalManipulator manipulator, int ballsPerSecond, double accuracy) {
        super(command, manipulator, 0);

        this.ballsPerSecond = ballsPerSecond;
        this.accuracy = accuracy;
    }

    public int getBallsScored() {
        return this.ballsScored;
    }

    @Override
    public void actionDone() {
        // TODO: Currently scores all balls at the end of the action and not throughout it

        for (int i = 0; i < ((BallsManipulator) this.manipulator).getBalls().size(); i++) {
            boolean score = (Math.random() >= (1 - this.accuracy));
            if (score) {
                this.ballsScored++;
                this.manipulator.getScore().scoreLowgoal();
            }
        }

        this.success = true;

        this.manipulator.actionFinish();
    }

    @Override
    public void actionStart() {
        if (((BallsManipulator) this.manipulator).hasBalls()) {
            success = false;
            this.manipulator.actionFinish();
        } else if (this.manipulator.isInRange()) {
            success = false;
            this.manipulator.actionFinish();
        }

        this.remainingTime = ((BallsManipulator) this.manipulator).getBalls().size() / this.ballsPerSecond;
    }

    @Override
    String getSuccessString() {
        return "has shot " + this.getBallsScored() + " balls into the lowgoal";
    }

    @Override
    String getFailureString() {
        return "";
    }
}
