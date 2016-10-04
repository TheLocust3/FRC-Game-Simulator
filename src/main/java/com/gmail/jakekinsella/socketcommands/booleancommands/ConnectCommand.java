package com.gmail.jakekinsella.socketcommands.booleancommands;

import com.gmail.jakekinsella.socketcommands.Command;
import com.gmail.jakekinsella.socketcommands.SocketCommand;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/13/16.
 */
public class ConnectCommand extends SocketCommand {

    String command = "CONNECTED";

    public ConnectCommand(Socket socket) throws IOException {
        super(socket);
    }

    public boolean run() {
        this.sendCommand(this.command);
        Command response = null;
        try {
            response = this.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response.getArgs().get(0).equals(this.command)) {
            return true;
        }

        return false;
    }
}
