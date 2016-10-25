package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;

/**
 * Created by jakekinsella on 10/25/16.
 */
public class MovementAction extends Action {

    private double maxVelocity, percentSpeed;

    public MovementAction(Command command, Robot robot, double maxVelocity) {
        super(command, robot);

        this.maxVelocity = maxVelocity;
        this.percentSpeed = (Double) this.command.getArg(0);
    }

    @Override
    public void tick() {
        this.robot.setVelocity(this.maxVelocity * this.percentSpeed);
        this.success = true;

        this.robot.actionFinish();
        this.robot.sendActionResponse();
    }
}
