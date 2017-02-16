package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.field.AirshipStation;
import com.gmail.jakekinsella.field.Boiler;
import com.gmail.jakekinsella.field.LoadingStation;
import com.gmail.jakekinsella.field.Score;
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
    private Boiler boiler;
    private ArrayList<LoadingStation> loadingStations = new ArrayList<>();
    private ArrayList<AirshipStation> airshipStations = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();

    public RobotAlliance(RobotAllianceColor color) {
        this.color = color;
        this.score = new Score(this.color);
        this.robots = new ArrayList<>();

        this.boiler = new Boiler(this.getColor());

        this.loadingStations.add(new LoadingStation(0, this.getColor()));
        this.loadingStations.add(new LoadingStation(1, this.getColor()));
        this.loadingStations.add(new LoadingStation(2, this.getColor()));

        this.airshipStations.add(new AirshipStation(0, this.getColor()));
        this.airshipStations.add(new AirshipStation(1, this.getColor()));
        this.airshipStations.add(new AirshipStation(2, this.getColor()));
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

    public Boiler getBoiler() {
        return this.boiler;
    }

    public ArrayList<LoadingStation> getLoadingStations() {
        return this.loadingStations;
    }

    public ArrayList<AirshipStation> getAirshipStations() {
        return this.airshipStations;
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

        this.score.placeGear(); // Place the free gear
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

        this.boiler.paint(graphics, graphics2D);

        for (LoadingStation loadingStation : this.loadingStations) {
            loadingStation.paint(graphics, graphics2D);
        }

        for (AirshipStation airshipStation : this.airshipStations) {
            airshipStation.paint(graphics, graphics2D);
        }

        this.score.paint(graphics, graphics2D);
    }
}
