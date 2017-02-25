package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.manipulators.Manipulator;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/17/16.
 */
public abstract class TimeAction extends Action {

    protected long remainingTime;
    private long lastTick;
    private int i = 0;

    public TimeAction(Command command, Manipulator manipulator, int time) {
        super(command, manipulator);

        this.remainingTime = time;
        this.lastTick = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        if (i == 0) {
            actionStart();
        }

        long delta = System.currentTimeMillis() - this.lastTick;
        this.remainingTime -= delta;

        actionTick();

        if (remainingTime <= 0) { // Must be finished
            actionDone();
        }

        this.lastTick = System.currentTimeMillis();

        i++;
    }

    public void actionDone() {

    }

    public void actionStart() {

    }

    public void actionTick() {

    }
}
