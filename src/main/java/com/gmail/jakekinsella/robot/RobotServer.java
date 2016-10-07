package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.JSONFileReader;
import com.gmail.jakekinsella.Mode;
import com.gmail.jakekinsella.field.Field;
import com.gmail.jakekinsella.field.defense.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class RobotServer {

    public static final int AUTO_TIME = 15000;
    public static final int TELEOP_TIME = 135000;
    public static final int MAP_UPDATE_INTERVAL = 1000;

    private long modeStartTime, lastMapUpdateTime;
    private ServerSocket serverSocket;
    private RobotAlliance redAlliance, blueAlliance;
    private Field field;
    private Mode mode;

    private static final Logger logger = LogManager.getLogger();

    public RobotServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.mode = Mode.STOPPED;

        this.setupGame();
    }

    public Field getField() {
        return this.field;
    }

    /**
     * Waits for connection from six robot programs
     */
    public void connectAllRobots() throws IOException {
        JSONFileReader jsonFileReader = new JSONFileReader("src/main/resources/robots.json");
        int numRobots = jsonFileReader.getJSONObject().size();

        for (int i = 0; i < numRobots; i++) {
            logger.debug("Waiting for robot socket connection");
            Socket robotSocket = serverSocket.accept();
            logger.debug("Robot socket connected");

            Robot robot = new Robot(robotSocket, jsonFileReader);

            if (robot.getColor() == RobotAllianceColor.RED) {
                robot.setRobotAlliance(redAlliance);
                redAlliance.addRobot(robot);
            } else {
                robot.setRobotAlliance(blueAlliance);
                blueAlliance.addRobot(robot);
            }
        }
    }

    public void start() {
        this.blueAlliance.start();
        this.redAlliance.start();

        this.mode = Mode.STARTING;
        logger.info("Game started");
    }

    public void stopGame() {
        this.blueAlliance.stopGame();
        this.redAlliance.stopGame();

        this.mode = Mode.STOPPED;
        logger.info("Game ended");
    }

    public void startAuto() {
        this.blueAlliance.startAuto();
        this.redAlliance.startAuto();

        modeStartTime = System.currentTimeMillis();
        lastMapUpdateTime = modeStartTime;
        this.mode = Mode.AUTO;
        logger.info("Autonomous mode started");
    }

    public void startTeleop() {
        this.blueAlliance.startTeleop();
        this.redAlliance.startTeleop();

        modeStartTime = System.currentTimeMillis();
        lastMapUpdateTime = modeStartTime;
        this.mode = Mode.TELEOP;
        logger.info("Teleop mode started");
    }

    public void sendMapUpdate() {
        logger.debug("Sending map update");

        JSONObject obj = new JSONObject();
        obj.put(blueAlliance.getColor(), blueAlliance.toJSONArray());
        obj.put(redAlliance.getColor(), redAlliance.toJSONArray());

        JSONObject scoreObj = new JSONObject();
        scoreObj.put(blueAlliance.getColor(), blueAlliance.scoreToJSONObject());
        scoreObj.put(redAlliance.getColor(), redAlliance.scoreToJSONObject());

        obj.put("SCORE", scoreObj);

        blueAlliance.sendMapUpdate(obj);
        redAlliance.sendMapUpdate(obj);
    }

    public void run() {
        if (this.mode.equals(Mode.AUTO) || this.mode.equals(Mode.TELEOP)) {
            this.blueAlliance.run();
            this.redAlliance.run();

            if (System.currentTimeMillis() >= lastMapUpdateTime + MAP_UPDATE_INTERVAL) {
                this.sendMapUpdate();
                lastMapUpdateTime = System.currentTimeMillis();
            }
        }

        if (this.mode.equals(Mode.AUTO) && System.currentTimeMillis() >= modeStartTime + this.AUTO_TIME) {
            logger.info("Autonomous mode ended");
            this.startTeleop();
        }

        if (this.mode.equals(Mode.TELEOP) && System.currentTimeMillis() >= modeStartTime + this.TELEOP_TIME) {
            logger.info("Teleop mode ended");
            this.stopGame();
        }
    }

    private void setupGame() {
        redAlliance = new RobotAlliance(RobotAllianceColor.RED);
        blueAlliance = new RobotAlliance(RobotAllianceColor.BLUE);

        this.setupDefenses();

        field = new Field(redAlliance, blueAlliance);
    }

    private void setupDefenses() {
        JSONFileReader jsonFileReader = new JSONFileReader("src/main/resources/defenses.json");
        JSONArray redStringDefenses = ((JSONArray) jsonFileReader.getJSONObject().get("RED"));
        JSONArray blueStringDefenses = ((JSONArray) jsonFileReader.getJSONObject().get("BLUE"));

        ArrayList<Defense> redDefenses = new ArrayList<>();

        redDefenses.add(this.createDefense((String) redStringDefenses.get(0), 1, redAlliance.getColor()));
        redDefenses.add(this.createDefense((String) redStringDefenses.get(1), 2, redAlliance.getColor()));
        redDefenses.add(this.createDefense((String) redStringDefenses.get(2), 3, redAlliance.getColor()));
        redDefenses.add(this.createDefense((String) redStringDefenses.get(3), 4, redAlliance.getColor()));
        redDefenses.add(this.createDefense((String) redStringDefenses.get(4), 5, redAlliance.getColor()));

        redAlliance.setAllDefenses(redDefenses);

        ArrayList<Defense> blueDefenses = new ArrayList<>();

        blueDefenses.add(this.createDefense((String) blueStringDefenses.get(0), 1, blueAlliance.getColor()));
        blueDefenses.add(this.createDefense((String) blueStringDefenses.get(1), 2, blueAlliance.getColor()));
        blueDefenses.add(this.createDefense((String) blueStringDefenses.get(2), 3, blueAlliance.getColor()));
        blueDefenses.add(this.createDefense((String) blueStringDefenses.get(3), 4, blueAlliance.getColor()));
        blueDefenses.add(this.createDefense((String) blueStringDefenses.get(4), 5, blueAlliance.getColor()));

        blueAlliance.setAllDefenses(blueDefenses);
    }

    private Defense createDefense(String defenseName, int defensePosition, RobotAllianceColor allianceColor) {
        Defense defense = null;

        switch (defenseName) {
            case "lowbar":
                defense = new LowbarDefense(defensePosition, allianceColor);
                break;
            case "portcullis":
                defense = new PortcullisDefense(defensePosition, allianceColor);
                break;
            case "cheval_de_frise":
                defense = new ChevalDeFriseDefense(defensePosition, allianceColor);
                break;
            case "moat":
                defense = new MoatDefense(defensePosition, allianceColor);
                break;
            case "ramparts":
                defense = new RampartsDefense(defensePosition, allianceColor);
                break;
            case "drawbridge":
                defense = new DrawbridgeDefense(defensePosition, allianceColor);
                break;
            case "sally_port":
                defense = new SallyPortDefense(defensePosition, allianceColor);
                break;
            case "rock_wall":
                defense = new RockWallDefense(defensePosition, allianceColor);
                break;
            case "rough_terrain":
                defense = new RoughTerrainDefense(defensePosition, allianceColor);
                break;
        }

        return defense;
    }
}
