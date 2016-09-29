package com.gmail.jakekinsella.socketcommands;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/15/16.
 */
public class SetCommand extends SocketCommand {

    String command = "SET", variable;

    public SetCommand(Socket socket, String variable) throws IOException {
        super(socket);

        this.variable = variable;
    }

    public void run(String value) {
        this.sendCommand(this.command, this.variable,  "\"" + value + "\"");
    }
}
