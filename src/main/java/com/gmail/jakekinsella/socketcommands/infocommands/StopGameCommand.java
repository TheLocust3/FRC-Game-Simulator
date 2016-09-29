package com.gmail.jakekinsella.socketcommands.infocommands;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/29/16.
 */
public class StopGameCommand extends InfoCommand {

    public StopGameCommand(Socket socket) throws IOException {
        super(socket, "STOP_GAME");
    }
}
