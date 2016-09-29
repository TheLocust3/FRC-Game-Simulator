package com.gmail.jakekinsella;

import com.gmail.jakekinsella.robot.RobotServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class ControlWorker extends SwingWorker {

    private RobotServer robotServer;

    private static final Logger logger = LogManager.getLogger();

    public ControlWorker(RobotServer robotServer) {
        this.robotServer = robotServer;
    }

    @Override
    protected Object doInBackground() throws Exception {
        logger.info("Trying connect to all robots");
        robotServer.connectAllRobots();

        Thread.sleep(1000);

        robotServer.start();
        robotServer.startAuto();

        while (true) {
            robotServer.run();

            Thread.sleep(50);
        }
    }
}
