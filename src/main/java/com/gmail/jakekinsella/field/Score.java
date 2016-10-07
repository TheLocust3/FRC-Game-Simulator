package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.robot.RobotAllianceColor;
import org.json.simple.JSONObject;

import java.awt.*;

/**
 * Created by jakekinsella on 10/7/16.
 */
public class Score implements Paintable {

    private int score;
    private RobotAllianceColor robotAllianceColor;

    public Score(RobotAllianceColor robotAllianceColor) {
        this.robotAllianceColor = robotAllianceColor;
        this.score = 0;
    }

    public int getScore() {
        return this.score;
    }

    public void scoreHighgoal() {
        this.score += 5;
    }

    public void scoreLowgoal() {
        this.score += 2;
    }

    public void scoreDefenseCross() {
        this.score += 5;
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("score", this.score);

        return jsonObject;
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        graphics.setFont(new Font("Arial", Font.PLAIN, 24));
        if (this.robotAllianceColor == RobotAllianceColor.BLUE) {
            graphics.drawString("Blue: " + this.getScore(), 0, 20);
        } else {
            graphics.drawString("Red: " + this.getScore(), Field.getBackgroundImageWidth() - 100, 20);
        }
    }
}
