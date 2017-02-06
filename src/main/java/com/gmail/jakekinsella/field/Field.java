package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.field.defense.Defense;
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

    public static final int BALL_NUMBER = 6;
    public final int WALL_WIDTH = 6;

    public static final Rectangle2D.Double redBoiler = new Rectangle2D.Double(17, 355, 45, 40);
    public static final Rectangle2D.Double blueBoiler = new Rectangle2D.Double(682, 355, 45, 40);

    public Rectangle2D.Double leftWall, rightWall, topWall, bottomWall;

    private int fps = 0;

    private Image backgroundImage;
    private static int backgroundImageWidth, backgroundImageHeight;

    private ArrayList<Ball> balls = new ArrayList<>();
    private RobotAlliance blueAlliance, redAlliance;

    private static final Logger logger = LogManager.getLogger();

    public Field(RobotAlliance blueAlliance, RobotAlliance redAlliance) {
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/field.png"));
            backgroundImageWidth = backgroundImage.getWidth(this) / 2;
            backgroundImageHeight = backgroundImage.getHeight(this) / 2;

            leftWall = new Rectangle2D.Double(-(this.WALL_WIDTH / 2), 0, this.WALL_WIDTH, backgroundImageHeight);
            rightWall = new Rectangle2D.Double(backgroundImageWidth - (this.WALL_WIDTH / 2), 0, this.WALL_WIDTH, backgroundImageHeight);

            topWall = new Rectangle2D.Double(0, -(this.WALL_WIDTH / 2), backgroundImageWidth, this.WALL_WIDTH);
            bottomWall = new Rectangle2D.Double(0, backgroundImageHeight - (this.WALL_WIDTH / 2), backgroundImageWidth, this.WALL_WIDTH);
        } catch (IOException e) {
            logger.error("Error in reading the background image", e);
        }

        this.blueAlliance = blueAlliance;
        this.redAlliance = redAlliance;

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
            if (rectangle.intersects(ball.getRectangle())) {
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

    public ArrayList<Defense> detectAllDefensesInRect(Shape rectangle) {
        ArrayList<Defense> detectedDefenses = new ArrayList<>();

        ArrayList<Defense> defenses = new ArrayList<>(this.redAlliance.getDefenses());
        defenses.addAll(this.blueAlliance.getDefenses());

        for (Defense defense : defenses) {
            if (rectangle.intersects(defense.getRectangle())) {
                detectedDefenses.add(defense);
            }
        }

        return detectedDefenses;
    }

    public Defense robotInDefenseDetector(Shape rectangle) {
        ArrayList<Defense> defenses = new ArrayList<>(this.redAlliance.getDefenses());
        defenses.addAll(this.blueAlliance.getDefenses());

        for (Defense defense : defenses) {
            if (defense.getDetectionBox().intersects(rectangle.getBounds().getCenterX(), rectangle.getBounds().getCenterY(), 1, 11)) {
                return defense;
            }
        }

        return null;
    }

    public boolean touchingWall(Shape rectangle) {
        return rectangle.intersects(this.leftWall) || rectangle.intersects(this.rightWall) || rectangle.intersects(this.topWall) || rectangle.intersects(this.bottomWall);
    }

    public ArrayList<Rectangle2D.Double> checkIfBoilerInRange(Shape rectangle) {
        ArrayList<Rectangle2D.Double> goals = new ArrayList<>();

        if (rectangle.intersects(blueBoiler)) {
            goals.add(blueBoiler);
        }

        if (rectangle.intersects(redBoiler)) {
            goals.add(redBoiler);
        }

        return goals;
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics.drawImage(this.backgroundImage, 0, 0, backgroundImageWidth, backgroundImageHeight, this);

        blueAlliance.paint(graphics, (Graphics2D) graphics2D.create());
        redAlliance.paint(graphics, (Graphics2D) graphics2D.create());

        for (Ball ball : this.balls) {
            ball.paint(graphics, (Graphics2D) graphics2D.create());
        }

        graphics.setColor(Color.BLUE);
        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(-45), blueBoiler.getCenterX(), blueBoiler.getCenterY());
        graphics2D.fill(at.createTransformedShape(blueBoiler));

        graphics.setColor(Color.RED);
        at = AffineTransform.getRotateInstance(Math.toRadians(45), redBoiler.getCenterX(), redBoiler.getCenterY());
        graphics2D.fill(at.createTransformedShape(redBoiler));

        graphics2D.drawString(Integer.toString(this.fps), backgroundImageWidth - 40, backgroundImageHeight - 25);
    }

    private void setupField() {
        int interval = backgroundImageHeight / (BALL_NUMBER + 1);
        for (int i = interval; i < backgroundImageHeight - 1; i += interval) { // The - 1 prevents a 7th ball from being drawn
            this.balls.add(new Ball(backgroundImageWidth / 2 - (Ball.BALL_DIAMETER / 2), i - (Ball.BALL_DIAMETER / 2)));
        }
    }
}
