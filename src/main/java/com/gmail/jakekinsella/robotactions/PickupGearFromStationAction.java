package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.Gear;
import com.gmail.jakekinsella.robot.manipulators.gearmanipulators.GearManipulator;
import com.gmail.jakekinsella.robot.manipulators.gearmanipulators.GearPickupStationManipulator;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class PickupGearFromStationAction extends TimeAction {

    private double successChance;

    private Gear gear;

    public PickupGearFromStationAction(Command command, GearPickupStationManipulator manipulator, int time, double successChance) {
        super(command, manipulator, time);

        this.successChance = successChance;
    }

    public Gear getGear() {
        return this.gear;
    }

    @Override
    public void actionDone() {
        this.success = (Math.random() >= (1 - this.successChance));

        if (this.manipulator.isInRange()) {
            this.gear = new Gear(0, 0); // Random location
        }

        this.manipulator.actionFinish();
    }

    @Override
    public void actionStart() {
        if (((GearManipulator) this.manipulator).hasGear()) { // Don't really like casting up
            success = false;
            this.manipulator.actionFinish();
        } else if (!this.manipulator.isInRange()) {
            success = false;
            this.manipulator.actionFinish();
        }
    }

    @Override
    String getSuccessString() {
        return "has picked up a gear from the Loading Station";
    }

    @Override
    String getFailureString() {
        return "has failed to pick up a gear from the Loading Station";
    }
}
