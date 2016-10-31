package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.field.defense.Defense;
import com.gmail.jakekinsella.robot.Robot;
import com.gmail.jakekinsella.robot.RobotAllianceColor;
import com.gmail.jakekinsella.robot.RobotServer;
import com.gmail.jakekinsella.socketcommands.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jakekinsella on 10/31/16.
 */
public class DefenseAction extends TimeAction {

    private int direction = 1;

    private static final Logger logger = LogManager.getLogger();

    public DefenseAction(Command command, Robot robot) {
        super(command, robot, ((Long) command.getArg(0)).intValue());

        this.success = true;
    }

    @Override
    public void actionStart() {

        if (this.robot.getVelocity() != 0) {
            success = false;
            logger.info(this.robot.getRobotName() + " is at a bad velocity to cross a defense!");
        }

        Defense defense = RobotServer.getField().robotInDefenseDetector(this.robot.getRectangle());
        if (defense != null) {
            if (defense.getAllianceColor().equals(RobotAllianceColor.BLUE)) {
                this.direction = -1;
            }

            if (this.robot.getAngle() != this.direction * 90) {
                success = false;
                logger.info(this.robot.getRobotName() + " is at a bad angle to cross a defense!");
            }
        } else {
            success = false;
            logger.info(this.robot.getRobotName() + " is not in position to cross a defense!");
        }

        if (!success) {
            this.robot.actionFinish();
            this.robot.sendActionResponse();
        } else {
            this.robot.setVelocity((-1 * this.direction * 50.0) / (((Long) command.getArg(0)).intValue() / 1000.0));
        }
    }

    @Override
    public void actionDone() {
        logger.info(this.robot.getRobotName() + " has crossed a defense!");

        this.robot.setVelocity(0);
        this.robot.getRobotAlliance().getScore().scoreDefenseCross();
        this.robot.sendActionResponse();
        this.robot.actionFinish();
    }
}
