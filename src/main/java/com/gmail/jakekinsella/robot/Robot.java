package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.JSONFileReader;
import com.gmail.jakekinsella.Main;
import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.field.defense.Defense;
import com.gmail.jakekinsella.robotactions.*;
import com.gmail.jakekinsella.socketcommands.*;
import com.gmail.jakekinsella.socketcommands.booleancommands.ConnectCommand;
import com.gmail.jakekinsella.socketcommands.booleancommands.StartAutoCommand;
import com.gmail.jakekinsella.socketcommands.booleancommands.StartCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.StartTeleopCommand;
import com.gmail.jakekinsella.socketcommands.infocommands.StopGameCommand;
import com.gmail.jakekinsella.socketcommands.parsecommand.ClientCommand;
import com.gmail.jakekinsella.socketcommands.parsecommand.ParseCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class Robot implements Paintable {

    public final int REACHABLE_PIXELS = 20; // TODO: Should be settable in XML
    public final int SHOOT_RANGE = 100;

    private int width, height;
    private double angle;
    private String robotName;
    private int defensePosition;

    private Rectangle2D.Double rectangle;
    private RobotSide pickupSide;
    private Socket robotSocket;
    private BufferedReader robotSocketReader;
    private RobotAllianceColor color;
    private RobotAlliance robotAlliance;
    private JSONFileReader jsonFileReader;
    private Action currentAction = new NoneAction();
    private Ball ball;

    private static final Logger logger = LogManager.getLogger();

    public Robot(Socket robotSocket, JSONFileReader jsonFileReader) {
        this.robotSocket = robotSocket;
        this.jsonFileReader = jsonFileReader;
        this.rectangle = new Rectangle2D.Double(0, 0, 0, 0);

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
        return (int) this.rectangle.getX();
    }

    public int getCenterX() {
        return (int) this.rectangle.getCenterX();
    }

    public void setX(int x) {
        this.rectangle.setRect(x, this.getY(), this.width, this.height);

        if (this.ball != null) {
            this.ball.setX(this.getCenterX());
            this.ball.setY(this.getCenterY());
        }
    }

    public int getY() {
        return (int) this.rectangle.getY();
    }

    public int getCenterY() {
        return (int) this.rectangle.getCenterY();
    }

    public void setY(int y) {
        rectangle.setRect(this.getX(), y, this.width, this.height);
    }

    public double getAngle() {
        return this.angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Ball getBall() {
        return this.ball;
    }

    public RobotAlliance getRobotAlliance() {
        return this.robotAlliance;
    }

    public void setRobotAlliance(RobotAlliance robotAlliance) {
        this.robotAlliance = robotAlliance;
    }

    public String getRobotName() {
        return this.robotName;
    }

    public Shape getRectangle() {
        return this.rectangle;
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
        obj.put("x", this.getCenterX());
        obj.put("y", this.getCenterY());
        obj.put("action", this.currentAction.toString()); // TODO: add actions to robot and send them

        return obj;
    }

    public ArrayList<Ball> getBallsInFrontOf(RobotSide robotSide) {
        return RobotServer.getField().detectAllBallsInRect(this.createDetectionRect(robotSide, this.REACHABLE_PIXELS));
    }

    public Shape createDetectionRect(RobotSide robotSide, int range) {
        int rectWidth = this.width, rectHeight = range, offset = 0;
        double rectAngle = 0;

        switch (robotSide) {
            case FRONT:
                rectAngle = this.getAngle();
                break;
            case BACK:
                rectAngle = this.getAngle() - 180;
                break;
            case LEFT:
                rectWidth = this.height;
                rectAngle = this.getAngle() - 90;
                offset = -Math.abs(this.height - this.width) + 5; // This 5 is a tuned value. Idk why it is needed
                break;
            case RIGHT:
                rectWidth = this.height;
                rectAngle = this.getAngle() - 270;
                offset = -Math.abs(this.height - this.width) + 5;
                break;
        }

        int rectX = this.getCenterX() - (rectWidth / 2);
        int rectY = this.getCenterY() + (this.height / 2) + offset;

        Shape rect = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rectAngle), this.getCenterX(), this.getCenterY());
        return at.createTransformedShape(rect);
    }

    public void shutdownRobot() throws IOException {
        this.robotSocket.close();
    }

    // Process all any new commands and give actions time to update
    public void tick() {
        currentAction.tick();
        processRobotInput();
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
                logger.info(this.getRobotName() + " has started to shoot a ball");
                this.currentAction = new ShootAction(commandInfo, this, this.pickupSide, this.SHOOT_RANGE);
                break;
            case TURN:
                logger.error("TURN action not implemented!");
                break;
            case MOVE:
                logger.error("MOVE action not implemented!");
                break;
            case PICKUP:
                logger.info(this.getRobotName() + " has started to pickup a ball");
                this.currentAction = new PickupAction(commandInfo, this, this.pickupSide);
                break;
            case LOWGOAL:
                logger.info(this.getRobotName() + " has started to shoot a lowgoal");
                this.currentAction = new LowgoalAction(commandInfo, this, this.pickupSide, this.REACHABLE_PIXELS);
                break;
        }
    }

    public void sendActionResponse() {
        JSONObject command = new JSONObject();
        command.put("command", SocketCommand.COMMAND_RESPONSE);

        JSONArray array = new JSONArray();
        array.add(this.currentAction.toString());
        array.add(this.currentAction.getSuccess());
        command.put("args", array);

        Command response = new Command(command);
        try {
            new ResponseCommand(this.robotSocket, response).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionFinish() {
        if (this.currentAction.getSuccess()) {
            if (this.currentAction.toString().equals("PICKUP")) {
                this.ball = ((PickupAction) this.currentAction).getBall();

                this.ball.setX(this.getCenterX());
                this.ball.setY(this.getCenterY());
            } else if (this.currentAction.toString().equals("SHOOT")) {
                // TODO: Drop ball near the tower
            } else if (this.currentAction.toString().equals("LOWGOAL")) {
                // TODO: Drop ball near the tower
            }
        }

        this.currentAction = new NoneAction();
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
            graphics2D.setColor(Color.BLUE);
        } else {
            graphics2D.setColor(Color.RED);
        }

        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(this.getAngle()), this.getCenterX(), this.getCenterY());
        Shape shape = at.createTransformedShape(this.rectangle);
        graphics2D.fill(shape);
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
        this.pickupSide = RobotSide.valueOf((String) jsonRobot.get("pickupSide"));
        this.defensePosition = ((Long) jsonRobot.get("startDefense")).intValue();
        this.width = ((Long) jsonRobot.get("width")).intValue();
        this.height = ((Long) jsonRobot.get("height")).intValue();
        this.rectangle.getBounds2D().setRect(this.getX(), this.getY(), this.width, this.height);


        RobotAllianceColor startColor; // Robots start in front of the opposite defenses
        if (this.color == RobotAllianceColor.BLUE) {
            startColor = RobotAllianceColor.RED;
        } else {
            startColor = RobotAllianceColor.BLUE;
        }

        int[] position = Defense.getPosition(startColor, this.defensePosition);
        this.setX(Main.FRAME_WIDTH / 2);

        if (this.color == RobotAllianceColor.BLUE) {
            this.setX(this.getX() + 20);
        } else {
            this.setX(this.getX() - ((this.width * 2) - 20));
        }

        this.setY(position[1]);

        if (this.color == RobotAllianceColor.BLUE) {
            this.setAngle(-90);
        } else {
            this.setAngle(angle = 90);
        }
    }
}
