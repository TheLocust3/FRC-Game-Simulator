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
    public static final Rectangle2D.Double bottomBlueTowerGoal = new Rectangle2D.Double(-1, 197, 32, 2);
    public static final Rectangle2D.Double topBlueTowerGoal = new Rectangle2D.Double(-1, 145, 32, 2);
    public static final Rectangle2D.Double middleBlueTowerGoal = new Rectangle2D.Double(27, 157, 2, 32);

    public static final Rectangle2D.Double bottomRedTowerGoal = new Rectangle2D.Double(730, 221, 32, 2);
    public static final Rectangle2D.Double topRedTowerGoal = new Rectangle2D.Double(730, 171, 32, 2);
    public static final Rectangle2D.Double middleRedTowerGoal = new Rectangle2D.Double(732, 181, 2, 32);

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

    public ArrayList<Rectangle2D.Double> checkIfHighGoalInRange(Shape rectangle) {
        ArrayList<Rectangle2D.Double> goals = new ArrayList<>();

        if (rectangle.intersects(bottomBlueTowerGoal)) {
            goals.add(bottomBlueTowerGoal);
        }

        if (rectangle.intersects(bottomRedTowerGoal)) {
            goals.add(bottomRedTowerGoal);
        }

        if (rectangle.intersects(middleBlueTowerGoal)) {
            goals.add(middleBlueTowerGoal);
        }

        if (rectangle.intersects(middleRedTowerGoal)) {
            goals.add(middleRedTowerGoal);
        }

        if (rectangle.intersects(topBlueTowerGoal)) {
            goals.add(topBlueTowerGoal);
        }

        if (rectangle.intersects(topRedTowerGoal)) {
            goals.add(topRedTowerGoal);
        }

        return goals;
    }

    public ArrayList<Rectangle2D.Double> checkIfLowGoalInRange(Shape rectangle) {
        ArrayList<Rectangle2D.Double> goals = new ArrayList<>();

        if (rectangle.intersects(bottomBlueTowerGoal)) {
            goals.add(bottomBlueTowerGoal);
        }

        if (rectangle.intersects(bottomRedTowerGoal)) {
            goals.add(bottomRedTowerGoal);
        }

        if (rectangle.intersects(topBlueTowerGoal)) {
            goals.add(topBlueTowerGoal);
        }

        if (rectangle.intersects(topRedTowerGoal)) {
            goals.add(topRedTowerGoal);
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

        AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(-30), bottomBlueTowerGoal.getCenterX(), bottomBlueTowerGoal.getCenterY());
        graphics2D.fill(at.createTransformedShape(bottomBlueTowerGoal));

        at = AffineTransform.getRotateInstance(Math.toRadians(30), topBlueTowerGoal.getCenterX(), topBlueTowerGoal.getCenterY());
        graphics2D.fill(at.createTransformedShape(topBlueTowerGoal));

        graphics2D.fill(middleBlueTowerGoal);

        at = AffineTransform.getRotateInstance(Math.toRadians(30), bottomRedTowerGoal.getCenterX(), bottomRedTowerGoal.getCenterY());
        graphics2D.fill(at.createTransformedShape(bottomRedTowerGoal));

        at = AffineTransform.getRotateInstance(Math.toRadians(-30), topRedTowerGoal.getCenterX(), topRedTowerGoal.getCenterY());
        graphics2D.fill(at.createTransformedShape(topRedTowerGoal));

        graphics2D.fill(middleRedTowerGoal);

        graphics2D.drawString(Integer.toString(this.fps), backgroundImageWidth - 40, backgroundImageHeight - 15);
    }

    private void setupField() {
        int interval = backgroundImageHeight / (BALL_NUMBER + 1);
        for (int i = interval; i < backgroundImageHeight - 1; i += interval) { // The - 1 prevents a 7th ball from being drawn
            this.balls.add(new Ball(backgroundImageWidth / 2 - (Ball.BALL_DIAMETER / 2), i - (Ball.BALL_DIAMETER / 2)));
        }
    }
}
