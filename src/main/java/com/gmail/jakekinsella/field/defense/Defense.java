package com.gmail.jakekinsella.field.defense;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.RobotAllianceColor;

import java.awt.*;

/**
 * Created by jakekinsella on 9/20/16.
 */
public abstract class Defense implements Paintable {
    protected String defenseName = "";

    protected int defensePosition, x, y;
    protected RobotAllianceColor allianceColor;

    public Defense(int defensePosition, RobotAllianceColor allianceColor) {
        this.defensePosition = defensePosition;
        this.allianceColor = allianceColor;

        int[] position = this.getPosition(allianceColor, defensePosition);
        this.x = position[0];
        this.y = position[1];
    }

    public String getDefenseName() {
        return this.defenseName;
    }

    public int getDefensePosition() {
        return this.defensePosition;
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        graphics.setFont(new Font("Arial", Font.PLAIN, 6));
        graphics.setColor(Color.BLACK);
        graphics.drawString(this.defenseName, this.x, this.y);
    }

    public static int[] getPosition(RobotAllianceColor allianceColor, int defensePosition) {
        int[] position = new int[2];

        if (allianceColor.equals(RobotAllianceColor.BLUE)) {
            position[0] = 224;
        } else {
            position[0] = 481;
        }

        switch (defensePosition) {
            case 1:
                if (allianceColor.equals(RobotAllianceColor.BLUE)) {
                    position[1] = 320;
                } else {
                    position[1] = 7;
                }

                break;
            case 2:
                if (allianceColor.equals(RobotAllianceColor.BLUE)) {
                    position[1] = 256;
                } else {
                    position[1] = 64;
                }

                break;
            case 3:
                if (allianceColor.equals(RobotAllianceColor.BLUE)) {
                    position[1] = 193;
                } else {
                    position[1] = 127;
                }

                break;
            case 4:
                if (allianceColor.equals(RobotAllianceColor.BLUE)) {
                    position[1] = 130;
                } else {
                    position[1] = 190;
                }

                break;
            case 5:
                if (allianceColor.equals(RobotAllianceColor.BLUE)) {
                    position[1] = 66;
                } else {
                    position[1] = 254;
                }

                break;
        }

        return position;
    }
}
