package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.manipulators.gearmanipulators.GearManipulator;
import com.gmail.jakekinsella.robot.manipulators.gearmanipulators.GearPlaceManipulator;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class PlaceGearAction extends TimeAction {

    private final double successChance;

    public PlaceGearAction(Command command, GearPlaceManipulator manipulator, int time, double successChance) {
        super(command, manipulator, time);

        this.successChance = successChance;
    }

    @Override
    public void actionDone() {
        this.success = (Math.random() >= (1 - this.successChance));

        this.manipulator.getScore().placeGear();
        this.manipulator.actionFinish();
    }

    @Override
    public void actionStart() {
        this.success = (Math.random() >= (1 - this.successChance));

        if (((GearManipulator) this.manipulator).hasGear()) {
            success = false;
            this.manipulator.actionFinish();
        } else if (this.manipulator.isInRange()) {
            success = false;
            this.manipulator.actionFinish();
        }
    }

    @Override
    String getSuccessString() {
        return "placed a gear";
    }

    @Override
    String getFailureString() {
        return "failed to place a gear";
    }
}
