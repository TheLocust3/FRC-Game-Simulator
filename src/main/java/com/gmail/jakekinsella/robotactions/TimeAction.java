package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/17/16.
 */
public class TimeAction extends Action {

    private long remainingTime;
    private long lastTick;

    public TimeAction(Command command, Robot robot, int time) {
        super(command, robot);

        this.remainingTime = time;
        this.lastTick = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        actionStart();

        long delta = System.currentTimeMillis() - this.lastTick;
        this.remainingTime -= delta;

        if (remainingTime <= 0) { // Must be finished
            actionDone();
        }

        this.lastTick = System.currentTimeMillis();
    }

    public void actionDone() {

    }

    public void actionStart() {

    }
}
