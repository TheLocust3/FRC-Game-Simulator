package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.robot.RobotAllianceColor;

/**
 * Created by jakekinsella on 2/6/17.
 */
public class AirshipStation extends FieldElement {

    public AirshipStation(int position, RobotAllianceColor robotAllianceColor) {
        super(0, 0, 10, 10, 0, 10, robotAllianceColor);

        int[] point = this.positionToPoint(position, robotAllianceColor);
        this.x = point[0];
        this.y = point[1];
        this.angle = point[2];
        this.updateRectangles();
    }

    private int[] positionToPoint(int position, RobotAllianceColor robotAllianceColor) {
        int x = 0, y = 0, angle = 0;
        switch (position) {
            case 0:
                y = 207;
                if (robotAllianceColor == RobotAllianceColor.BLUE) {
                    x = 602;
                } else {
                    x = 132;
                }
                break;
            case 1:
                y = 160;
                if (robotAllianceColor == RobotAllianceColor.BLUE) {
                    angle = 27;
                    x = 575;
                } else {
                    angle = 63;
                    x = 156;
                }
                break;
            case 2:
                y = 255;
                if (robotAllianceColor == RobotAllianceColor.BLUE) {
                    angle = 63;
                    x = 575;
                } else {
                    angle = 27;
                    x = 156;
                }
                break;
        }

        return new int[]{x, y, angle};
    }
}
