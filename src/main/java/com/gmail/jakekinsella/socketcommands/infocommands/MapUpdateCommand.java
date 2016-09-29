package com.gmail.jakekinsella.socketcommands.infocommands;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/29/16.
 */
public class MapUpdateCommand extends InfoCommand {

    public MapUpdateCommand(Socket socket, String map) throws IOException {
        super(socket, "MAP_UPDATE", map);
    }


}
