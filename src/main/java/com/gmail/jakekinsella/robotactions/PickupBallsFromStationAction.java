package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.field.Gear;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class PickupBallsFromStationAction extends TimeAction {

    private double successChance;

    private ArrayList<Ball> balls;

    private static final Logger logger = LogManager.getLogger();

    public PickupBallsFromStationAction(Command command, Robot robot, int time, double successChance) {
        super(command, robot, time);

        this.successChance = successChance;
    }

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    @Override
    public void actionDone() {
        this.success = true;

        if (this.robot.isReadyToReceiveBallsFromStation()) {
            for (int i = 0; i < 30; i++) { // TODO: Change the number of balls recieved from player station
                boolean score = (Math.random() >= (1 - this.successChance));
                if (score) {
                    this.balls.add(new Ball(0, 0));
                }
            }
        }

        logger.info(this.robot.getRobotName() + " has picked up " + this.balls.size() + " balls");

        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }

    @Override
    public void actionStart() {
        if (!this.robot.isReadyToReceiveBallsFromStation()) {
            success = false;
            logger.info(this.robot.getRobotName() + " isn't in range of a human player station");
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        }
    }
}
