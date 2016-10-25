package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/25/16.
 */
public class TurnAction extends Action {

    private double startingAngle, finalAngle;
    private double degreePerMillisecond;
    private long lastTick;

    private static final Logger logger = LogManager.getLogger();

    public TurnAction(Command command, Robot robot, double degreePerMillisecond) {
        super(command, robot);

        this.startingAngle = this.robot.getAngle();
        this.finalAngle = this.robot.getAngle() + ((Long) this.command.getArg(0));

        this.degreePerMillisecond = degreePerMillisecond;
        if (this.startingAngle > this.finalAngle) {
            this.degreePerMillisecond *= -1;
        }
        this.lastTick = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        long delta = System.currentTimeMillis() - this.lastTick;

        double newAngle = this.robot.getAngle() + (this.degreePerMillisecond * delta);

        if ((this.finalAngle < this.startingAngle && newAngle < this.finalAngle) || (this.finalAngle > this.startingAngle && newAngle > this.finalAngle)) {
            this.robot.setAngle(this.finalAngle);
            this.success = true;

            logger.info(this.robot.getRobotName() + " has stopped turning");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        } else {
            this.robot.setAngle(this.robot.getAngle() + (this.degreePerMillisecond * delta));
        }

        this.lastTick = System.currentTimeMillis();
    }
}
