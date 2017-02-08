package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.robot.RobotAllianceColor;

/**
 * Created by jakekinsella on 2/6/17.
 */
public class Boiler extends FieldElement {

    public Boiler(RobotAllianceColor robotAllianceColor) {
        super(0, 0, 45, 40, 45, 10, robotAllianceColor);

        int[] point = this.positionToPoint(robotAllianceColor);
        this.x = point[0];
        this.y = point[1];
        this.angle = point[2];
        this.updateRectangles();
    }

    private int[] positionToPoint(RobotAllianceColor robotAllianceColor) {
        int x;
        int y = 355;
        int angle;
        if (robotAllianceColor == RobotAllianceColor.RED) {
            x = 17;
            angle = 45;
        } else {
            x = 682;
            angle = -45;
        }

        int[] point = {x, y, angle};
        return point;
    }
}
