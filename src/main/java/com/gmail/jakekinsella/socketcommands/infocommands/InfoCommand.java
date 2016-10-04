package com.gmail.jakekinsella.socketcommands.infocommands;

import com.gmail.jakekinsella.socketcommands.SocketCommand;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakekinsella on 9/29/16.
 */
public class InfoCommand extends SocketCommand {

    String command;
    String[] args;

    public InfoCommand(Socket socket, String commandName, String... args) throws IOException {
        super(socket);
        this.command = commandName;
        this.args = args;
    }

    public void run() {
        this.sendCommand(this.command, this.args);
    }
}
