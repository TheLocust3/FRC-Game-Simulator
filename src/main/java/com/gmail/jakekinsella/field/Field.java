package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.robot.*;
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

    public ArrayList<Object> detectObjectInRectangle(Shape rectangle) {
        ArrayList<Object> objects = new ArrayList<>();

        for (Ball ball : this.balls) {
            if (rectangle.intersects(ball.getX(), ball.getY(), ball.BALL_DIAMETER, ball.BALL_DIAMETER)) {
                objects.add(ball);
                System.out.println("Found ball!");
            }
        }

        for (com.gmail.jakekinsella.robot.Robot robot : this.redAlliance.getRobots()) {
            if (rectangle.intersects(robot.getX(), robot.getY(), robot.getWidth(), robot.getHeight())) {
                objects.add(robot);
                System.out.println("Found red robot!");
            }
        }


        for (com.gmail.jakekinsella.robot.Robot robot : this.blueAlliance.getRobots()) {
            if (rectangle.intersects(robot.getX(), robot.getY(), robot.getWidth(), robot.getHeight())) {
                objects.add(robot);
                System.out.println("Found blue robot!");
            }
        }

        return objects;
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
