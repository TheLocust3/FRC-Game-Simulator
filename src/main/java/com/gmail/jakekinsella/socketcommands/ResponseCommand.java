package com.gmail.jakekinsella.socketcommands;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 10/11/16.
 */
public class ResponseCommand extends SocketCommand {

    private Command command;

    public ResponseCommand(Socket socket, Command command) throws IOException {
        super(socket);

        this.command = command;
    }

    public void run() {
        this.sendCommand(this.command);
    }
}
