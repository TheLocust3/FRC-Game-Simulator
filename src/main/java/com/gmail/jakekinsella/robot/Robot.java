package com.gmail.jakekinsella.robot;

import com.gmail.jakekinsella.JSONFileReader;
import com.gmail.jakekinsella.Main;
import com.gmail.jakekinsella.Paintable;
import com.gmail.jakekinsella.field.Ball;
import com.gmail.jakekinsella.field.Gear;
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

    private int width, height;
    private double angle;
    private double velocity; // in pixels per second
    private String robotName;

    private int pickupGearRange, pickupBallsRange, highgoalRange, lowgoalRange;
    private double pickupGearChance, pickupBallsChance, highgoalChance, lowgoalChance, degreePerMillisecond, maxVelocity;
    private int pickupGearTime, placeGearTime, pickupBallsTime, highgoalBallsPerSecond, lowgoalBallsPerSecond;
    private long lastTick;

    private Rectangle2D.Double rectangle;
    private RobotSide pickupGearSide, pickupBallsSide, highgoalSide, lowgoalSide;
    private Socket robotSocket;
    private BufferedReader robotSocketReader;
    private RobotAllianceColor color;
    private RobotAlliance robotAlliance;
    private JSONFileReader jsonFileReader;
    private Action currentAction = new NoneAction();
    private ArrayList<Ball> balls;
    private Gear gear;

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

    public double getVelocity() {
        return this.velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
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

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    public Gear getGear() {
        return this.gear;
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

    public boolean isReadyToReceiveGearFromStation() {
        return RobotServer.getField().checkIfStationInRange(this.getGearDetectionBox());
    }

    public boolean isReadyToReceiveBallsFromStation() {
        return RobotServer.getField().checkIfStationInRange(this.getBallsDetectionBox());
    }

    public boolean isReadyToReceiveBallsFromHopper() {
        return RobotServer.getField().checkIfNonEmptyHopperInRange(this.getBallsDetectionBox());
    }

    public boolean isInRangeOfAirshipStation() {
        return RobotServer.getField().checkIfAirshipStationInRange(this.getGearDetectionBox());
    }

    public boolean isInRangeOfHighGoal() {
        return RobotServer.getField().checkIfBoilerInRange(this.getHighGoalDetectionBox());
    }

    public boolean isInRangeOfLowGoal() {
        return RobotServer.getField().checkIfBoilerInRange(this.getLowGoalDetectionBox());
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
        return RobotServer.getField().detectAllBallsInRect(this.createDetectionRect(robotSide, this.pickupGearRange));
    }

    public void shutdownRobot() throws IOException {
        this.robotSocket.close();
    }

    public void movementTick(long delta) {
        double deltaSeconds = delta / 1000.0;

        double radians = Math.toRadians(this.getAngle());
        double deltaX = deltaSeconds * (this.getVelocity() * Math.sin(radians));
        double deltaY = -deltaSeconds * (this.getVelocity() * Math.cos(radians));

        this.setX((int) (this.getX() + deltaX));
        this.setY((int) (this.getY() + deltaY));

        ArrayList<Ball> balls = RobotServer.getField().detectAllBallsInRect(this.getRectangle());

        if (balls.size() > 0) {
            for (Ball ball : balls) {
                if (!ball.isStuck()) {
                    ball.setAngle(this.getAngle());
                    ball.setVelocity(this.getVelocity());
                } else {
                    this.setVelocity(0);

                    if (this.currentAction.toString().equals("TURN")) {
                        this.currentAction = new NoneAction();
                    }
                }
            }
        }

        if (RobotServer.getField().touchingWall(this.getRectangle())) {
            this.setVelocity(0);
        }

        if (RobotServer.getField().checkIfBoilerInRange(this.getRectangle())) {
            this.setVelocity(0);
        }
     }

    // Process all any new commands and give actions time to update
    public void tick() {
        long delta = System.currentTimeMillis() - this.lastTick;

        movementTick(delta);
        currentAction.tick();
        processRobotInput();

        this.lastTick = System.currentTimeMillis();
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
                this.currentAction = new ShootAction(commandInfo, this, this.highgoalBallsPerSecond, this.highgoalChance);
                break;
            case TURN:
                logger.info(this.getRobotName() + " has started to turn");
                this.currentAction = new TurnAction(commandInfo, this, this.degreePerMillisecond);
                break;
            case MOVE:
                logger.info(this.getRobotName() + " has changed velocity");
                this.currentAction = new MovementAction(commandInfo, this, this.maxVelocity);
                break;
            case PLACE_GEAR:
                logger.info(this.getRobotName() + " has started to place a gear");
                this.currentAction = new PlaceGearAction(commandInfo, this, this.placeGearTime);
                break;
            case PICKUP_GEAR_STATION:
                logger.info(this.getRobotName() + " has started to pickup a gear from the human player station");
                this.currentAction = new PickupGearFromStationAction(commandInfo, this, this.pickupGearTime, this.pickupGearChance);
                break;
            case PICKUP_BALLS_STATION:
                logger.info(this.getRobotName() + " has started to pickup balls from the human player station");
                this.currentAction = new PickupBallsFromStationAction(commandInfo, this, this.pickupBallsTime, this.pickupBallsChance);
                break;
            case PICKUP_BALLS_HOPPER:
                logger.info(this.getRobotName() + " has started to pickup balls from a hopper");
                this.currentAction = new PickupBallsFromHopperAction(commandInfo, this, this.pickupBallsTime, this.pickupBallsChance);
                break;
            case LOWGOAL:
                logger.info(this.getRobotName() + " has started to shoot a lowgoal");
                this.currentAction = new LowgoalAction(commandInfo, this, this.lowgoalBallsPerSecond, this.lowgoalBallsPerSecond, this.lowgoalChance);
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
            if (this.currentAction.toString().equals("PICKUP_GEAR_STATION")) {
                this.gear = ((PickupGearFromStationAction) this.currentAction).getGear();

                this.gear.setX(this.getCenterX());
                this.gear.setY(this.getCenterY());
            } else if (this.currentAction.toString().equals("PICKUP_BALLS_STATION")) {
                this.balls.addAll(((PickupBallsFromStationAction) this.currentAction).getBalls());
            } else if (this.currentAction.toString().equals("PICKUP_BALLS_HOPPER")) {
                this.balls.addAll(((PickupBallsFromHopperAction) this.currentAction).getBalls());
            } else if (this.currentAction.toString().equals("SHOOT")) {
                this.balls = new ArrayList<>();
                // TODO: Drop balls
            } else if (this.currentAction.toString().equals("LOWGOAL")) {
                this.balls = new ArrayList<>();
                // TODO: Drop balls
            } else if (this.currentAction.toString().equals("PLACE_GEAR")) {
                this.gear = null;
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

        // TODO: Indicate how many balls the robot has
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

        this.balls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.balls.add(new Ball(0,0)); // Ball location is irrelevant
        }

        getRobotProperties();
    }

    private void getRobotProperties() {
        JSONObject jsonRobot = (JSONObject) this.jsonFileReader.getJSONObject().get(this.robotName);

        this.color = RobotAllianceColor.valueOf((String) jsonRobot.get("allianceColor"));
        int startY = ((Long) jsonRobot.get("startY")).intValue();
        this.width = ((Long) jsonRobot.get("width")).intValue();
        this.height = ((Long) jsonRobot.get("height")).intValue();
        this.rectangle.getBounds2D().setRect(this.getX(), this.getY(), this.width, this.height);

        this.pickupGearSide = RobotSide.valueOf((String) jsonRobot.get("pickupGearSide"));
        this.pickupBallsSide = RobotSide.valueOf((String) jsonRobot.get("pickupBallsSide"));
        this.highgoalSide = RobotSide.valueOf((String) jsonRobot.get("highgoalSide"));
        this.lowgoalSide = RobotSide.valueOf((String) jsonRobot.get("lowgoalSide"));

        this.pickupGearRange = ((Long) jsonRobot.get("pickupGearRange")).intValue();
        this.pickupBallsRange = ((Long) jsonRobot.get("pickupBallsRange")).intValue();
        this.highgoalRange = ((Long) jsonRobot.get("highgoalRange")).intValue();
        this.lowgoalRange = ((Long) jsonRobot.get("lowgoalRange")).intValue();

        this.pickupGearChance = (Double) jsonRobot.get("pickupGearChance");
        this.pickupBallsChance = (Double) jsonRobot.get("pickupBallsChance");
        this.highgoalChance = (Double) jsonRobot.get("highgoalChance");
        this.lowgoalChance = (Double) jsonRobot.get("lowgoalChance");

        this.pickupGearTime = ((Long) jsonRobot.get("pickupGearTime")).intValue();
        this.placeGearTime = ((Long) jsonRobot.get("placeGearTime")).intValue();
        this.pickupBallsTime = ((Long) jsonRobot.get("pickupBallsTime")).intValue();

        this.highgoalBallsPerSecond = ((Long) jsonRobot.get("highgoalBallsPerSecond")).intValue();
        this.lowgoalBallsPerSecond = ((Long) jsonRobot.get("lowgoalBallsPerSecond")).intValue();

        this.degreePerMillisecond = (Double) jsonRobot.get("degreePerMillisecond");
        this.maxVelocity = (Double) jsonRobot.get("maxVelocity");

        this.setY(startY);
        if (this.color == RobotAllianceColor.RED) {
            this.setX(40);
            this.setAngle(90);
        } else {
            this.setX((int) (Main.FRAME_WIDTH - (1.5 * this.getHeight()))); // TODO: figure out why the heck this is 1.5
            this.setAngle(270);
        }
    }

    private Shape getGearDetectionBox() {
        return this.createDetectionRect(this.pickupGearSide, this.pickupGearRange);
    }

    private Shape getBallsDetectionBox() {
        return this.createDetectionRect(this.pickupBallsSide, this.pickupBallsRange);
    }

    private Shape getHighGoalDetectionBox() {
        return this.createDetectionRect(this.highgoalSide, this.highgoalRange);
    }

    private Shape getLowGoalDetectionBox() {
        return this.createDetectionRect(this.lowgoalSide, this.lowgoalRange);
    }

    private Shape createDetectionRect(RobotSide robotSide, int range) {
        int rectWidth = this.width, rectHeight = range, offset = 0;
        double rectAngle = 0;

        switch (robotSide) {
            case FRONT:
                rectAngle = this.getAngle() - 180;
                break;
            case BACK:
                rectAngle = this.getAngle();
                break;
            case LEFT:
                rectWidth = this.height;
                rectAngle = this.getAngle() - 270;
                offset = -Math.abs(this.height - this.width) + 5; // This 5 is a tuned value. Idk why it is needed
                break;
            case RIGHT:
                rectWidth = this.height;
                rectAngle = this.getAngle() - 90;
                offset = -Math.abs(this.height - this.width) + 5;
                break;
        }

        int rectX = this.getCenterX() - (rectWidth / 2);
        int rectY = this.getCenterY() + (this.height / 2) + offset;

        Rectangle2D.Double rect = new Rectangle2D.Double(rectX, rectY, rectWidth, rectHeight);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rectAngle), this.getCenterX(), this.getCenterY());
        return at.createTransformedShape(rect);
    }
}
