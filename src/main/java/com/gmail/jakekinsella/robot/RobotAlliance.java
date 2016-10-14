package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.field.Field;
import com.gmail.jakekinsella.field.Score;
import com.gmail.jakekinsella.field.defense.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class RobotAlliance implements Paintable {

    private RobotAllianceColor color;
    private Score score;
    private ArrayList<Robot> robots;
    private ArrayList<Defense> defenses = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();

    public RobotAlliance(RobotAllianceColor color) {
        this.color = color;
        this.score = new Score(this.color);
        this.robots = new ArrayList<>();

        this.defenses.add(new LowbarDefense(1, color));
        this.defenses.add(new LowbarDefense(2, color));
        this.defenses.add(new LowbarDefense(3, color));
        this.defenses.add(new LowbarDefense(4, color));
        this.defenses.add(new LowbarDefense(5, color));
    }

    public RobotAllianceColor getColor() {
        return this.color;
    }

    public Score getScore() {
        return this.score;
    }

    public ArrayList<Robot> getRobots() {
        return this.robots;
    }

    public void addRobot(Robot robot) {
        this.robots.add(robot);
    }

    public JSONArray toJSONArray() {
        JSONArray array = new JSONArray();
        for (Robot robot : this.robots) {
            array.add(robot.toJSONObject());
        }

        return array;
    }

    public JSONObject scoreToJSONObject() {
        return this.score.toJSONObject();
    }

    public ArrayList<Defense> getDefenses() {
        return this.defenses;
    }

    public void setAllDefenses(ArrayList<Defense> defenses) {
        this.defenses = defenses;
    }

    public void start() {
        for (Robot robot : this.robots) {
            while (!robot.startGame()) {
                logger.error(robot.getRobotName() + " failed to start game! Re-trying");
            }
        }
    }

    public void stopGame() {
        for (Robot robot : this.robots) {
            robot.stopGame();
        }
    }

    public void startAuto() {
        for (Robot robot : this.robots) {
            while (!robot.startAuto()) {
                logger.error(robot.getRobotName() + " failed to start auto! Re-trying");
            }
        }
    }

    public void startTeleop() {
        for (Robot robot : this.robots) {
            robot.startTeleop();
        }
    }

    public void sendMapUpdate(JSONObject map) {
        for (Robot robot : this.robots) {
            robot.sendMapUpdate(map);
        }
    }

    public void run() {
        for (Robot robot : this.robots) {
            robot.tick();
        }
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        for (Robot robot : this.robots) {
            robot.paint(graphics, graphics2D);
        }

        for (Defense defense : this.defenses) {
            defense.paint(graphics, graphics2D);
        }

        score.paint(graphics, graphics2D);
    }
}
