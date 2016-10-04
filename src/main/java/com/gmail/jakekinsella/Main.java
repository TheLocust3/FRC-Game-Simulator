package com.gmail.jakekinsella;

import com.gmail.jakekinsella.robot.RobotServer;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class Main {

    public static final int FRAME_WIDTH = 760;
    public static final int FRAME_HEIGHT = 394;

    public static void main(String args[]) throws IOException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        RobotServer robotServer = new RobotServer(2345);

        frame.add(robotServer.getField());
        frame.setVisible(true);

        RenderWorker renderWorker = new RenderWorker(robotServer.getField());
        ControlWorker controlWorker = new ControlWorker(robotServer);

        controlWorker.execute();
        renderWorker.execute();
    }
}
