package com.gmail.jakekinsella.socketcommands.booleancommands;

import com.gmail.jakekinsella.socketcommands.Command;
import com.gmail.jakekinsella.socketcommands.SocketCommand;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/13/16.
 */
public class ConnectCommand extends BooleanCommand {

    public ConnectCommand(Socket socket) throws IOException {
        super(socket, "CONNECTED");
    }
}
