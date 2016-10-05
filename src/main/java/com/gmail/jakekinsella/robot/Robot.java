package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.JSONFileReader;
import com.gmail.jakekinsella.Main;
import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.field.defense.Defense;
import com.gmail.jakekinsella.robotactions.Action;
import com.gmail.jakekinsella.robotactions.NoneAction;
import com.gmail.jakekinsella.robotactions.ShootAction;
import com.gmail.jakekinsella.socketcommands.Command;
import com.gmail.jakekinsella.socketcommands.booleancommands.ConnectCommand;
import com.gmail.jakekinsella.socketcommands.GetCommand;
import com.gmail.jakekinsella.socketcommands.booleancommands.StartAutoCommand;
import com.gmail.jakekinsella.socketcommands.booleancommands.StartCommand;
import com.gmail.jakekinsella.socketcommands.MapUpdateCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.StartTeleopCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.StopGameCommand;
import com.gmail.jakekinsella.socketcommands.parsecommand.ClientCommand;
import com.gmail.jakekinsella.socketcommands.parsecommand.ParseCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class Robot implements Paintable {

    private int x, y, width, height;
    private Socket robotSocket;
    private BufferedReader robotSocketReader;
    private RobotAllianceColor color;
    private String robotName;
    private int defensePosition;
    private JSONFileReader jsonFileReader;
    private Action currentAction = new NoneAction();

    private static final Logger logger = LogManager.getLogger();

    public Robot(Socket robotSocket, JSONFileReader jsonFileReader) {
        this.robotSocket = robotSocket;
        this.jsonFileReader = jsonFileReader;

        try {
            this.robotSocketReader = new BufferedReader(new InputStreamReader(this.robotSocket.getInputStream()));
        } catch (IOException e) {
            logger.error("Error in opening a robot socket", e);
        }

        this.setup();
    }

    public RobotAllianceColor getColor() {
        return this.color;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public boolean isReadAvailable() {
        try {
            return this.robotSocketReader.ready();
        } catch (IOException e) {
            logger.error("Error in checking if read is available from robot", e);
        }

        return false;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("name", this.robotName);
        obj.put("alliance-color", this.color.toString());
        obj.put("width", this.width);
        obj.put("height", this.height);
        obj.put("x", this.x);
        obj.put("y", this.y);
        obj.put("action", this.currentAction.toString()); // TODO: add actions to robot and send them

        return obj;
    }

    public void shutdownRobot() throws IOException {
        this.robotSocket.close();
    }

    public void processRobotInput() {
        if (!this.isReadAvailable()) { return; }

        Command commandInfo = null;
        try {
            commandInfo = new ParseCommand(this.robotSocket).run();
        } catch (Exception e) {
            logger.error("Error in processing input from robot", e);
        }

        logger.debug("Received: " + commandInfo + " from robot");

        switch (ClientCommand.valueOf(commandInfo.getName())) {
            case SHOOT:
                this.currentAction = new ShootAction(commandInfo);
                break;
            case TURN:
                logger.error("TURN action not implemented!");
                break;
            case MOVE:
                logger.error("MOVE action not implemented!");
                break;
            case PICKUP:
                logger.error("PICKUP action not implemented!");
                break;
        }
    }

    public boolean startGame() {
        try {
            return new StartCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error("Error in starting game", e);
        }

        return false;
    }

    public void stopGame() {
        try {
            new StopGameCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error("Error in stopping game", e);
        }
    }

    public boolean startAuto() {
        try {
            return new StartAutoCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error("Error in starting auto", e);
        }

        return false;
    }

    public void startTeleop() {
        try {
            new StartTeleopCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error("Error in starting teleop", e);
        }
    }

    public void sendMapUpdate(JSONObject map) {
        try {
            new MapUpdateCommand(this.robotSocket, map).run();
        } catch (IOException e) {
            logger.error("Error in sending a map update", e);
        }
    }

    @Override
    public void paint(Graphics graphics, Graphics2D graphics2D) {
        if (this.color.equals(RobotAllianceColor.BLUE)) {
            graphics.setColor(Color.BLUE);
        } else {
            graphics.setColor(Color.RED);
        }
        graphics.fillRect(this.x + (this.width / 2), this.y + (this.height / 2), this.width, this.height);
    }

    private void setup() {
        try {
            if (!new ConnectCommand(this.robotSocket).run()) {
                throw new Exception("Robot socket did not successfully connect!");
            }

            if ((double) new GetCommand(this.robotSocket, "api_version").run().getArg(0) != 1.0) {
                throw new Exception("Robot client isn't the correct API version");
            }

            this.robotName = (String) new GetCommand(this.robotSocket, "robot_name").run().getArg(0);
        } catch (Exception e) {
            logger.error("Error in setting up a robot", e);
        }

        getRobotProperties();
    }

    private void getRobotProperties() {
        JSONObject jsonRobot = (JSONObject) this.jsonFileReader.getJSONObject().get(this.robotName);

        this.color = RobotAllianceColor.valueOf((String) jsonRobot.get("allianceColor"));
        this.defensePosition = ((Long) jsonRobot.get("startDefense")).intValue();
        this.width = ((Long) jsonRobot.get("width")).intValue();
        this.height = ((Long) jsonRobot.get("height")).intValue();


        RobotAllianceColor startColor; // Robots start in front of the opposite defenses
        if (this.color == RobotAllianceColor.BLUE) {
            startColor = RobotAllianceColor.RED;
        } else {
            startColor = RobotAllianceColor.BLUE;
        }

        int[] position = Defense.getPosition(startColor, this.defensePosition);
        this.x = Main.FRAME_WIDTH / 2;

        if (this.color == RobotAllianceColor.BLUE) {
            this.x -= 10;
        } else {
            this.x -= (this.width * 2) - 10;
        }

        this.y = position[1] - (this.height / 2);
    }
}
