package com.gmail.jakekinsella.socketcommands.parsecommand;

import com.gmail.jakekinsella.socketcommands.Command;
import com.gmail.jakekinsella.socketcommands.SocketCommand;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jakekinsella on 10/3/16.
 */
public class ParseCommand extends SocketCommand {

    public ParseCommand(Socket socket) throws IOException {
        super(socket);
    }

    public Command run() throws Exception {
        Command command = parseLine();

        boolean found = false;
        for (ClientCommand clientCommand : ClientCommand.values()) {
            if (clientCommand.toString().equals(command.getName())) {
                found = true;
                break;
            }
        }
        if (!found) { throw new Exception("Client send unknown command: " + command.getName()); }

        return command;
    }
}
