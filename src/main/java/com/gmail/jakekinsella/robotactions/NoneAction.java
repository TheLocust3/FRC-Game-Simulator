package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.manipulators.Manipulator;
import com.gmail.jakekinsella.socketcommands.Command;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class NoneAction extends Action {

    public NoneAction() {
        super(new Command("NONE", new ArrayList<>()), (Manipulator) null);
    }

    @Override
    public void tick() {

    }

    @Override
    String getSuccessString() {
        return null;
    }

    @Override
    String getFailureString() {
        return null;
    }
}
