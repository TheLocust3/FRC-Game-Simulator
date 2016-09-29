package com.gmail.jakekinsella.socketcommands.booleancommands;

import com.gmail.jakekinsella.socketcommands.booleancommands.BooleanCommand;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/28/16.
 */
public class StartAutoCommand extends BooleanCommand {

    public StartAutoCommand(Socket socket) throws IOException {
        super(socket, "START_AUTO");
    }
}
