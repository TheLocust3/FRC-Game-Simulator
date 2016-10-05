package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class Action {

    protected Command command;

    public Action(Command command) {
        this.command = command;
    }
}
