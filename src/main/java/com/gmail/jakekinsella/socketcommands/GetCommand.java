package com.gmail.jakekinsella.socketcommands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/13/16.
 */
public class GetCommand extends SocketCommand {

    String command = "GET";
    String variable;

    private static final Logger logger = LogManager.getLogger();

    public GetCommand(Socket socket, String variable) throws IOException {
        super(socket);

        this.variable = variable;
    }

    public Command run() {
        this.sendCommand(this.command, this.variable);
        Command response = null;
        try {
            response = this.getResponse();
        } catch (Exception e) {
            logger.error("Error in reading response from robot", e);
        }

        return response;
    }
}
