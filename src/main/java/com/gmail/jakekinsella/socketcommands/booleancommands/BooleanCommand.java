package com.gmail.jakekinsella.socketcommands.booleancommands;

import com.gmail.jakekinsella.socketcommands.SocketCommand;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/28/16.
 */
public class BooleanCommand extends SocketCommand {

    private String command;

    public BooleanCommand(Socket socket, String command) throws IOException {
        super(socket);
        this.command = command;
    }

    public boolean run() {
        this.sendCommand(this.command);
        String response = null;
        try {
            response = this.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.equals(this.BOOLEAN_TRUE)) {
            return true;
        }

        return false;
    }
}
