package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class ShootAction extends Action {

    private long remainingTime;
    private double successChance;
    private long lastTick;

    private static final Logger logger = LogManager.getLogger();

    public ShootAction(Command command, Robot robot) {
        super(command, robot);

        this.remainingTime = (long) command.getArg(0);
        this.successChance = (double) command.getArg(1);
        this.lastTick = System.currentTimeMillis();

        tick();
    }

    @Override
    public void tick() {
        long delta = System.currentTimeMillis() - this.lastTick;
        this.remainingTime -= delta;

        if (remainingTime <= 0) { // Must be finished
            boolean score = (Math.random() >= (1 - this.successChance));

            if (score) {
                logger.info(this.robot.getRobotName() + " has shot a ball and scored in the highgoal");
            } else {
                logger.info(this.robot.getRobotName() + " has shot a ball and missed the highgoal");
            }

            this.robot.actionFinish();
        }
        
        this.lastTick = System.currentTimeMillis();
    }
}
