package com.gmail.jakekinsella.socketcommands.infocommands;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/29/16.
 */
public class StartTeleopCommand extends InfoCommand {

    public StartTeleopCommand(Socket socket) throws IOException {
        super(socket, "START_TELEOP");
    }
}
