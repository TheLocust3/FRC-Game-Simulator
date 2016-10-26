package com.gmail.jakekinsella;

import com.gmail.jakekinsella.field.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * Created by jakekinsella on 9/12/16.
 */
public class RenderWorker extends SwingWorker {

    private static final Logger logger = LogManager.getLogger();

    private Field field;

    public RenderWorker(Field field) {
        this.field = field;
    }

    @Override
    protected Object doInBackground() throws Exception {
        while (true) {
            this.field.repaint();
            Thread.sleep(100);
        }
    }
}
