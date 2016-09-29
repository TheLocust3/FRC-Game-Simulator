package com.gmail.jakekinsella.field.defense;

import com.gmail.jakekinsella.robot.RobotAllianceColor;

import java.awt.*;

/**
 * Created by jakekinsella on 9/20/16.
 */
public class LowbarDefense extends Defense {

    public LowbarDefense(int defensePosition, RobotAllianceColor allianceColor) {
        super(defensePosition, allianceColor);

        this.defenseName = "LOWBAR";
    }
}
