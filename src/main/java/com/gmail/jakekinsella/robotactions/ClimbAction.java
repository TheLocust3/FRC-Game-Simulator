package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.manipulators.ClimberManipulator;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class ClimbAction extends TimeAction {

    private double successChance;

    public ClimbAction(Command command, ClimberManipulator manipulator, int time, double successChance) {
        super(command, manipulator, time);

        this.successChance = successChance;
    }

    @Override
    public void actionDone() {
        boolean score = (Math.random() >= (1 - this.successChance));

        this.success = score;

        this.manipulator.getScore().climb();
        this.manipulator.actionFinish();
    }

    @Override
    public void actionStart() {
        if (this.manipulator.isInRange()) {
            success = false;
            this.manipulator.actionFinish();
        }

        // TODO: Check time
    }

    @Override
    String getSuccessString() {
        return "has climbed";
    }

    @Override
    String getFailureString() {
        return "has failed to climb";
    }
}
