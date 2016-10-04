package com.gmail.jakekinsella.socketcommands.parsecommand;

import com.gmail.jakekinsella.socketcommands.SocketCommand;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jakekinsella on 10/3/16.
 */
public class ParseCommand extends SocketCommand {

    public final String prefix = "COMMAND";

    public ParseCommand(Socket socket) throws IOException {
        super(socket);
    }

    public ArrayList<String> run() throws Exception {
        ArrayList<String> commandInfo = new ArrayList<>();

        String command = readln();

        if (command.indexOf(this.prefix + " ") == -1) {
            throw new Exception("Client sent invalid command. Received: " + this.prefix);
        }
        command = command.replace(this.prefix + " ", "");

        String commandName = command.substring(0, command.indexOf("("));
        command = command.substring(command.indexOf("(") + 1, command.indexOf(")"));
        String[] commandArgs = command.split(",");

        boolean found = false;
        for (ClientCommand clientCommand : ClientCommand.values()) {
            if (clientCommand.toString().equals(commandName)) {
                found = true;
                break;
            }
        }
        if (!found) { throw new Exception("Client send unknown command: " + commandName); }

        commandInfo.add(commandName);
        Collections.addAll(commandInfo, commandArgs);

        return commandInfo;
    }
}
