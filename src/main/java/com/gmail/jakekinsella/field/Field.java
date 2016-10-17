package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.field.defense.Defense;
import com.gmail.jakekinsella.robot.*;
import com.gmail.jakekinsella.robot.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class Field extends JPanel {

    public final int BALL_NUMBER = 6;

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

    public static int getBackgroundImageWidth() {
        return backgroundImageWidth;
    }

    public static int getBackgroundImageHeight() {
        return backgroundImageHeight;
    }

    public ArrayList<Ball> detectAllBallsInRect(Shape rectangle) {
        ArrayList<Ball> detectedBalls = new ArrayList<>();

        for (Ball ball : this.balls) {
            if (rectangle.intersects(ball.getX(), ball.getY(), ball.BALL_DIAMETER, ball.BALL_DIAMETER)) {
                detectedBalls.add(ball);
                System.out.println("Found ball!");
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
                System.out.println("Found robot!");
            }
        }

        return detectedRobots;
    }

    public ArrayList<Defense> detectAllDefensesInRect(Shape rectangle) {
        ArrayList<Defense> detectedDefenses = new ArrayList<>();

        ArrayList<Defense> defenses = new ArrayList<>(this.redAlliance.getDefenses());
        defenses.addAll(this.blueAlliance.getDefenses());

        for (Defense defense : defenses) {
            // TODO: Implement detection of defenses
        }

        return detectedDefenses;
    }

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics.drawImage(this.backgroundImage, 0, 0, backgroundImageWidth, backgroundImageHeight, this);

        blueAlliance.paint(graphics, (Graphics2D) graphics2D.create());
        redAlliance.paint(graphics, (Graphics2D) graphics2D.create());

        for (Ball ball : this.balls) {
            ball.paint(graphics, (Graphics2D) graphics2D.create());
        }
    }

    private void setupField() {
        int interval = backgroundImageHeight / (this.BALL_NUMBER + 1);
        for (int i = interval; i < backgroundImageHeight - 1; i += interval) { // The - 1 prevents a 7th ball from being drawn
            this.balls.add(new Ball(backgroundImageWidth / 2, i));
        }
    }
}
