package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.manipulators.Manipulator;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/5/16.
 */
public abstract class Action {

    protected Command command;
    protected Manipulator manipulator;
    protected Robot robot;
    protected boolean success;

    public Action(Command command, Manipulator manipulator) {
        this.command = command;
        this.manipulator = manipulator;
        this.success = false;
    }

    public Action(Command command, Robot robot) {
        this.command = command;
        this.robot = robot;
        this.success = false;
    }

    public abstract void tick();

    public String getResponseString() {
        if (this.getSuccess()) {
            return getSuccessString();
        } else {
            return getFailureString();
        }
    }

    public boolean getSuccess() {
        return success;
    }

    public String toString() {
        return this.command.getName();
    }

    abstract String getSuccessString();

    abstract String getFailureString();
}
