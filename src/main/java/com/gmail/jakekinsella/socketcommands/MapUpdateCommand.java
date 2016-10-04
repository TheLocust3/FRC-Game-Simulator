package com.gmail.jakekinsella.socketcommands;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jakekinsella on 9/29/16.
 */
public class MapUpdateCommand extends SocketCommand {

    String command = "MAP_UPDATE";
    JSONObject map;

    public MapUpdateCommand(Socket socket, JSONObject map) throws IOException {
        super(socket);

        this.map = map;
    }

    public void run() {
        this.sendCommand(command, map);
    }
}
