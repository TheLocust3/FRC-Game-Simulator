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
        this.updateRectangles();
    }

    private int[] positionToPoint(int position, RobotAllianceColor robotAllianceColor) {
        int x = 0, y = 0;
        switch (position) {
            case 0:
                y = 207;
                if (robotAllianceColor == RobotAllianceColor.BLUE) {
                    x = 612;
                } else {
                    x = 122;
                }
                break;
            case 1:
                y = 153;
                if (robotAllianceColor == RobotAllianceColor.BLUE) {
                    x = 518;
                } else {
                    x = 216;
                }
                break;
            case 2:
                y = 261;
                if (robotAllianceColor == RobotAllianceColor.BLUE) {
                    x = 518;
                } else {
                    x = 216;
                }
                break;
        }

        int[] point = {x, y};
        return point;
    }
}
