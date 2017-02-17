package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.RobotAllianceColor;
import org.json.simple.JSONObject;

import java.awt.*;

/**
 * Created by jakekinsella on 10/7/16.
 */
public class Score implements Paintable {

    private final int ROTOR3 = 1;
    private final int ROTOR4 = 2;
    private final int LINE_SPACING = 20;
    private final int SCORE_Y = 15;

    private int score;
    private double pressure;
    private int gears;
    private int climbs;
    private RobotAllianceColor robotAllianceColor;
    private boolean teleop = false;

    public Score(RobotAllianceColor robotAllianceColor) {
        this.robotAllianceColor = robotAllianceColor;
        this.score = 0;
        this.pressure = 0;
        this.gears = 0;
        this.climbs = 0;
    }

    public int getScore() {
        return this.score;
    }

    public void switchScoringToTeleop() {
        this.teleop = true;
    }

    public void scoreHighgoal() {
        if (this.teleop) {
            this.pressure += 1.0 / 3.0;
        } else {
            this.pressure += 1.0;
        }

        this.updateScores();
    }

    public void scoreLowgoal() {
        if (this.teleop) {
            this.pressure += 1.0 / 9.0;
        } else {
            this.pressure += 1.0 / 3.0;
        }

        this.updateScores();
    }

    public void placeGear() {
        this.gears++;
        this.updateScores();
    }

    public void climb() {
        this.climbs++;
        this.updateScores();
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("score", this.score);

        return jsonObject;
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));

        int x = 0;
        if (this.robotAllianceColor == RobotAllianceColor.BLUE) {
            x = Field.getBackgroundImageWidth() - 250;
            graphics.drawString("Blue", x, this.SCORE_Y);
        } else {
            graphics.drawString("Red", x, this.SCORE_Y);
        }

        graphics.drawString("Score: " + this.getScore(), x + this.LINE_SPACING * 2, this.SCORE_Y);
        graphics.drawString("kPa: " + (int) this.pressure, x + this.LINE_SPACING * 6, this.SCORE_Y);
        graphics.drawString("Gears: " + this.gears, x + this.LINE_SPACING * 9, this.SCORE_Y);
    }

    private void updateScores() {
        // This is not very clean
        int gearScore = 0;
        if (this.gears - 1 >= 0) {
            gearScore += 60;
        }

        if (this.gears - 3 >= 0) {
            gearScore += 60;
        }

        if (this.gears - (7 - this.ROTOR3) >= 0) {
            gearScore += 60;
        }

        if (this.gears - (13 - this.ROTOR3 - this.ROTOR4) >= 0) {
            gearScore += 60;
        }

        this.score = (int) this.pressure + gearScore + (this.climbs * 50);
    }
}
