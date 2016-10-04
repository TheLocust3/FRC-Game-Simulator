package com.gmail.jakekinsella.socketcommands;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/13/16.
 */
public class GetCommand extends SocketCommand {

    String command = "GET";
    String variable;

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
            e.printStackTrace();
        }

        return response;
    }
}
