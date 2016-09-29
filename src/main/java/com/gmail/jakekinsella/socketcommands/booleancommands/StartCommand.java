package com.gmail.jakekinsella.socketcommands.booleancommands;

import com.gmail.jakekinsella.socketcommands.booleancommands.BooleanCommand;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/28/16.
 */
public class StartCommand extends BooleanCommand {

    public StartCommand(Socket socket) throws IOException {
        super(socket, "GAME_START");
    }
}
