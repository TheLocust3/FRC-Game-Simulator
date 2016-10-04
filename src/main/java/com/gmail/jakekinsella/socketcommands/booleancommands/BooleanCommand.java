package com.gmail.jakekinsella.socketcommands.booleancommands;

import com.gmail.jakekinsella.socketcommands.Command;
import com.gmail.jakekinsella.socketcommands.SocketCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/28/16.
 */
public class BooleanCommand extends SocketCommand {

    private String command;

    private static final Logger logger = LogManager.getLogger();

    public BooleanCommand(Socket socket, String command) throws IOException {
        super(socket);
        this.command = command;
    }

    public boolean run() {
        this.sendCommand(this.command);
        Command response = null;
        try {
            response = this.getResponse();
        } catch (Exception e) {
            logger.error("Error in reading response", e);
        }

        return (boolean) response.getArg(0);
    }
}
