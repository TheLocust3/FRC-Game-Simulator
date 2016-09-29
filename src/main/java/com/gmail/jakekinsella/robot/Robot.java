package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.JSONReader;
import com.gmail.jakekinsella.Main;
import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.field.defense.Defense;
import com.gmail.jakekinsella.socketcommands.booleancommands.ConnectCommand;
import com.gmail.jakekinsella.socketcommands.GetCommand;
import com.gmail.jakekinsella.socketcommands.booleancommands.StartAutoCommand;
import com.gmail.jakekinsella.socketcommands.booleancommands.StartCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.MapUpdateCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.StartTeleopCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.StopGameCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.*;
import java.net.Socket;

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
    private JSONReader jsonReader;

    private static final Logger logger = LogManager.getLogger();

    public Robot(Socket robotSocket, JSONReader jsonReader) {
        this.robotSocket = robotSocket;
        this.jsonReader = jsonReader;

        try {
            this.robotSocketReader = new BufferedReader(new InputStreamReader(this.robotSocket.getInputStream()));
        } catch (IOException e) {
            logger.error(e.toString());
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
            logger.error(e.toString());
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
        obj.put("action", "NONE"); // TODO: add actions to robot and send them

        return obj;
    }

    public void shutdownRobot() throws IOException {
        this.robotSocket.close();
    }

    public void processRobotInput() {
        if (!this.isReadAvailable()) { return; }


    }

    public boolean startGame() {
        try {
            return new StartCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error(e.toString());
        }

        return false;
    }

    public void stopGame() {
        try {
            new StopGameCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public boolean startAuto() {
        try {
            return new StartAutoCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error(e.toString());
        }

        return false;
    }

    public void startTeleop() {
        try {
            new StartTeleopCommand(this.robotSocket).run();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public void sendMapUpdate(String map) {
        try {
            new MapUpdateCommand(this.robotSocket, map).run();
        } catch (IOException e) {
            logger.error(e.toString());
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

            if (!new GetCommand(this.robotSocket, "api_version").run().equals("1.0")) {
                throw new Exception("Robot client isn't the correct API version");
            }

            this.robotName = new GetCommand(this.robotSocket, "robot_name").run();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        getRobotProperties();
    }

    private void getRobotProperties() {
        JSONObject jsonRobot = (JSONObject) this.jsonReader.getJSONObject().get(this.robotName);

        this.color = RobotAllianceColor.valueOf((String) jsonRobot.get("allianceColor"));
        this.defensePosition = Integer.parseInt((String) jsonRobot.get("startDefense"));
        this.width = Integer.parseInt((String) jsonRobot.get("width"));
        this.height = Integer.parseInt((String) jsonRobot.get("height"));

        int[] position = Defense.getPosition(this.color, this.defensePosition);
        this.x = Main.FRAME_WIDTH / 2 - (this.width / 2);
        this.y = position[1] - (this.height / 2);
    }
}
