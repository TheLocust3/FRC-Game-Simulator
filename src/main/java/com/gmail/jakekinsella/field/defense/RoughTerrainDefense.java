package com.gmail.jakekinsella.field.defense;

import com.gmail.jakekinsella.robot.RobotAllianceColor;

/**
 * Created by jakekinsella on 9/20/16.
 */
public class RoughTerrainDefense extends Defense {

    public RoughTerrainDefense(int defensePosition, RobotAllianceColor allianceColor) {
        super(defensePosition, allianceColor);

        this.defenseName = "ROUGH_TERRAIN";
    }
}
