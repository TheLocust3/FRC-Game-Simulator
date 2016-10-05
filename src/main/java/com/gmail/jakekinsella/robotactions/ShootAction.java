package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class ShootAction extends Action {

    private int actionTime;
    private double successChance;

    public ShootAction(Command command) {
        super(command);

        this.actionTime = (int) command.getArg(0);
        this.successChance = (double) command.getArg(1);
    }
}
