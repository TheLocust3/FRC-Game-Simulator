package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.robot.RobotAllianceColor;

/**
 * Created by jakekinsella on 2/6/17.
 */
public class Hopper extends FieldElement {

    public Hopper(int position) {
        super(0, 0, 30, 30, 0, 10, RobotAllianceColor.NONE);

        int[] point = this.positionToPoint(position);
        this.x = point[0];
        this.y = point[1];
        this.updateRectangles();
    }

    private int[] positionToPoint(int position) {
        int x = 0, y = 0;
        switch (position) {
            case 0:
                x = 27;
                y = 50;
                break;
            case 1:
                x = 69;
                y = 28;
                break;
            case 2:
                x = 0;
                y = 163;
                break;
            case 3:
                x = 0;
                y = 163;
                break;
            case 4:
                x = 0;
                y = 163;
                break;
        }

        int[] point = {x, y};
        return point;
    }
}
