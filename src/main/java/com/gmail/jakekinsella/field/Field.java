package com.gmail.jakekinsella.field;

import com.gmail.jakekinsella.field.defense.*;
import com.gmail.jakekinsella.robot.RobotAlliance;
import com.gmail.jakekinsella.robot.RobotAllianceColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class Field extends JPanel {

    public final int BALL_NUMBER = 6;

    private Image backgroundImage;
    private int backgroundImageWidth, backgroundImageHeight;

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

    public void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics.drawImage(this.backgroundImage, 0, 0, this.backgroundImageWidth, this.backgroundImageHeight, this);

        blueAlliance.paint(graphics, graphics2D);
        redAlliance.paint(graphics, graphics2D);

        for (Ball ball : this.balls) {
            ball.paint(graphics, graphics2D);
        }
    }

    private void setupField() {
        int interval = this.backgroundImageHeight / (this.BALL_NUMBER + 1);
        for (int i = interval; i < this.backgroundImageHeight - 1; i += interval) { // The - 1 prevents a 7th ball from being drawn
            this.balls.add(new Ball(this.backgroundImageWidth / 2, i));
        }
    }
}
