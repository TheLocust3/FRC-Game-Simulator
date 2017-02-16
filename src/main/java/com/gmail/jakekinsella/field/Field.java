package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.robot.*;
import com.gmail.jakekinsella.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class Field extends JPanel {

    private final int BALL_NUMBER = 6;
    private final int WALL_WIDTH = 6;

    public Rectangle2D.Double leftWall, rightWall, topWall, bottomWall, leftCornerTopWall, leftCornerBottomWall, rightCornerTopWall, rightCornerBottomWall;

    private int fps = 0;

    private Image backgroundImage;
    private static int backgroundImageWidth, backgroundImageHeight;

    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Hopper> hoppers;
    private RobotAlliance blueAlliance, redAlliance;

    private static final Logger logger = LogManager.getLogger();

    public Field(RobotAlliance blueAlliance, RobotAlliance redAlliance) {
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/field.png"));
            backgroundImageWidth = backgroundImage.getWidth(this) / 2;
            backgroundImageHeight = backgroundImage.getHeight(this) / 2;
        } catch (IOException e) {
            logger.error("Error in reading the background image", e);
        }

        this.blueAlliance = blueAlliance;
        this.redAlliance = redAlliance;

        this.setupWalls();
        this.setupHoppers();
        setupField();
    }

    public ArrayList<Ball> getBalls() {
        return this.balls;
    }

    public void setFPS(int fps) {
        this.fps = fps;
    }

    public static int getBackgroundImageWidth() {
        return backgroundImageWidth;
    }

    public static int getBackgroundImageHeight() {
        return backgroundImageHeight;
    }

    public ArrayList<Ball> detectAllBallsInRect(Shape rectangle) {
        ArrayList<Ball> detectedBalls = new ArrayList<>();

        for (Ball ball : this.balls) {
            if (rectangle.intersects(ball.getDetectionBox())) {
                detectedBalls.add(ball);
            }
        }

        return detectedBalls;
    }

    public ArrayList<Robot> detectAllRobotsInRect(Shape rectangle) {
        ArrayList<Robot> detectedRobots = new ArrayList<>();

        ArrayList<Robot> robots = new ArrayList<>(this.redAlliance.getRobots());
        robots.addAll(this.blueAlliance.getRobots());

        for (Robot robot : robots) {
            if (rectangle.intersects(robot.getX(), robot.getY(), robot.getWidth(), robot.getHeight())) {
                detectedRobots.add(robot);
            }
        }

        return detectedRobots;
    }

    public boolean touchingWall(Shape rectangle) {
        return rectangle.intersects(this.leftWall) || rectangle.intersects(this.rightWall) || rectangle.intersects(this.topWall) || rectangle.intersects(this.bottomWall)
                || rectangle.intersects(this.leftCornerTopWall) || rectangle.intersects(this.leftCornerBottomWall) || rectangle.intersects(this.rightCornerTopWall) || rectangle.intersects(this.rightCornerBottomWall);
    }

    public boolean checkIfBoilerInRange(Shape rectangle) {
        if (rectangle.intersects(this.blueAlliance.getBoiler().getDetectionBox())) {
            return true;
        }

        if (rectangle.intersects(this.redAlliance.getBoiler().getDetectionBox())) {
            return true;
        }

        return false;
    }

    public boolean checkIfStationInRange(Shape rectangle) {
        // TODO: Check if the robot is at the right one

        for (LoadingStation loadingStation : this.blueAlliance.getLoadingStations()) {
            if (rectangle.intersects(loadingStation.getDetectionBox())) {
                return true;
            }
        }

        for (LoadingStation loadingStation : this.redAlliance.getLoadingStations()) {
            if (rectangle.intersects(loadingStation.getDetectionBox())) {
                return true;
            }
        }

        return false;
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics.drawImage(this.backgroundImage, 0, 0, backgroundImageWidth, backgroundImageHeight, this);

        for (Ball ball : this.balls) {
            ball.paint(graphics, (Graphics2D) graphics2D.create());
        }

        // draw walls
        graphics.setColor(Color.BLACK);
        graphics2D.fill(leftWall);
        graphics2D.fill(rightWall);
        graphics2D.fill(topWall);
        graphics2D.fill(bottomWall);

        graphics2D.fill(this.rotateRect(leftCornerTopWall, -27));
        graphics2D.fill(this.rotateRect(rightCornerTopWall, 207));
        graphics2D.fill(this.rotateRect(leftCornerBottomWall, 45));
        graphics2D.fill(this.rotateRect(rightCornerBottomWall, -45));

        for (Hopper hopper : this.hoppers) {
            hopper.paint(graphics, graphics2D);
        }

        blueAlliance.paint(graphics, (Graphics2D) graphics2D.create());
        redAlliance.paint(graphics, (Graphics2D) graphics2D.create());

        graphics2D.drawString(Integer.toString(this.fps), backgroundImageWidth - 40, backgroundImageHeight - 25);
    }

    private Shape rotateRect(Rectangle2D.Double rect, double degrees) {
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(degrees), rect.getCenterX(), rect.getCenterY());
        return at.createTransformedShape(rect);
    }

    private void setupWalls() {
        leftWall = new Rectangle2D.Double(-(this.WALL_WIDTH / 2) + 32, 84, this.WALL_WIDTH, backgroundImageHeight - 165);
        rightWall = new Rectangle2D.Double(backgroundImageWidth - (this.WALL_WIDTH / 2) - 32, 83, this.WALL_WIDTH, backgroundImageHeight - 165);

        topWall = new Rectangle2D.Double(110, -(this.WALL_WIDTH / 2) + 42, backgroundImageWidth - 216, this.WALL_WIDTH);
        bottomWall = new Rectangle2D.Double(70, backgroundImageHeight - (this.WALL_WIDTH / 2) - 45, backgroundImageWidth - 140, this.WALL_WIDTH);

        leftCornerTopWall = new Rectangle2D.Double(25, -(this.WALL_WIDTH / 2) + 63, 90, this.WALL_WIDTH);
        rightCornerTopWall = new Rectangle2D.Double(backgroundImageWidth - (this.WALL_WIDTH / 2) - 110, -(this.WALL_WIDTH / 2) + 64, 90, this.WALL_WIDTH);

        leftCornerBottomWall = new Rectangle2D.Double(23, backgroundImageHeight - (this.WALL_WIDTH / 2) - 67, 60, this.WALL_WIDTH);
        rightCornerBottomWall = new Rectangle2D.Double(backgroundImageWidth - 80, backgroundImageHeight - (this.WALL_WIDTH / 2) - 67, 60, this.WALL_WIDTH);
    }

    private void setupHoppers() {
        this.hoppers = new ArrayList<>();
        this.hoppers.add(new Hopper(0));
        this.hoppers.add(new Hopper(1));
        this.hoppers.add(new Hopper(2));
        this.hoppers.add(new Hopper(3));
        this.hoppers.add(new Hopper(4));
    }

    private void setupField() {
        int interval = backgroundImageHeight / (BALL_NUMBER + 1);
        for (int i = interval; i < backgroundImageHeight - 1; i += interval) { // The - 1 prevents a 7th ball from being drawn
            this.balls.add(new Ball(backgroundImageWidth / 2 - (Ball.BALL_DIAMETER / 2), i - (Ball.BALL_DIAMETER / 2)));
        }
    }
}
