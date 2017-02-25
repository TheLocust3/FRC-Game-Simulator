package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.manipulators.ballsmanipulators.HighgoalManipulator;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class ShootAction extends TimeAction {

    private double accuracy;
    private int ballsPerSecond;
    private int ballsScored;

    public ShootAction(Command command, HighgoalManipulator manipulator, int ballsPerSecond, double accuracy) {
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

        for (int i = 0; i < ((HighgoalManipulator) this.manipulator).getBalls().size(); i++) {
            boolean score = (Math.random() >= (1 - this.accuracy));
            if (score) {
                this.ballsScored++;
                this.manipulator.getScore().scoreHighgoal();
            }
        }


        this.success = true;

        this.manipulator.actionFinish();
    }

    @Override
    public void actionStart() {
        if (((HighgoalManipulator) this.manipulator).getBalls().size() == 0) {
            success = false;
            this.manipulator.actionFinish();
        } else if (this.manipulator.isInRange()) {
            success = false;
            this.manipulator.actionFinish();
        }

        this.remainingTime = ((HighgoalManipulator) this.manipulator).getBalls().size() / this.ballsPerSecond;
    }

    @Override
    String getSuccessString() {
        return "has shot " + this.getBallsScored() + " balls into the highgoal";
    }

    @Override
    String getFailureString() {
        return "";
    }
}
