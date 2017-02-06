package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.robot.RobotAllianceColor;

/**
 * Created by jakekinsella on 9/20/16.
 */
public class LoadingStation extends FieldElement {

    public LoadingStation(int x, int y, RobotAllianceColor allianceColor) {
        super(x, y, 33, 25, 0, 10, allianceColor);
    }

    public LoadingStation(int position, RobotAllianceColor allianceColor) {
        this(0, 0, allianceColor);

        int[] point = this.positionToPoint(position, allianceColor);
        this.x = point[0];
        this.y = point[1];
        this.angle = point[2];
        this.updateRectangles();
    }

    private int[] positionToPoint(int position, RobotAllianceColor allianceColor) {
        int x = 0, y = 0, angle = 0;
        switch (position) {
            case 0:
                y = 50;
                angle = 64;
                if (allianceColor == RobotAllianceColor.BLUE) {
                    x = 27;
                } else {
                    x = 685;
                    angle *= -1;
                }

                break;
            case 1:
                y = 28;
                angle = 64;
                if (allianceColor == RobotAllianceColor.BLUE) {
                    x = 69;
                } else {
                    x = 641;
                    angle *= -1;
                }

                break;
            case 2:
                y = 163;
                if (allianceColor == RobotAllianceColor.BLUE) {
                    x = 710;
                } else {
                    x = 0;
                }

                break;
        }

        int[] point = {x, y, angle};
        return point;
    }
}
