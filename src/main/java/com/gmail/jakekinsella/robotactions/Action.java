package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class Action {

    protected Command command;
    protected Robot robot;

    public Action(Command command, Robot robot) {
        this.command = command;
        this.robot = robot;
    }

    public void tick() {}

    public String toString() {
        return this.command.getName();
    }
}