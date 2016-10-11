package com.gmail.jakekinsella.socketcommands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakekinsella on 9/13/16.
 */
public class SocketCommand {

    public static final String COMMAND_RESPONSE = "COMMAND_RESPONSE";

    private Socket socket;
    private PrintWriter socketOutput;
    private BufferedReader socketReader;
    private JSONParser parser = new JSONParser();

    private static final Logger logger = LogManager.getLogger();

    public SocketCommand(Socket socket) throws IOException {
        this.socket = socket;
        this.socketOutput = new PrintWriter(this.socket.getOutputStream(), true);
        this.socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    protected void sendCommand(Command command) {
        String rawJSON = command.toJSON().toJSONString();

        logger.debug("Sending: " + rawJSON);
        writeln(rawJSON);
    }

    protected void sendCommand(String command, ArrayList<String> args) {
        Command newCommand = new Command(command, args);
        sendCommand(newCommand);
    }

    protected void sendCommand(String command, JSONObject args) {
        Command newCommand = new Command(command, args);
        sendCommand(newCommand);
    }

    protected void sendCommand(String command, String... args) {
        sendCommand(command, new ArrayList<>(Arrays.asList(args)));
    }

    protected Command getResponse() throws Exception {
        Command command = parseLine();

        if (command.getName().equals(COMMAND_RESPONSE)) {
            return command;
        }

        throw new Exception("Client failed to respond correctly");
    }

    protected Command parseLine() throws ParseException {
        String response = readln();

        Object obj = parser.parse(response);
        JSONObject jsonObject = (JSONObject) obj;
        Command command = new Command(jsonObject);

        return command;
    }

    private void writeln(String out) {
        this.socketOutput.println(out);
    }

    protected String readln() {
        try {
            while (!this.socketReader.ready()) {
                logger.debug("Waiting to receive response from client...");
            }

            return this.socketReader.readLine();
        } catch (IOException e) {
            logger.error("Error in reading line from socket", e);
        }

        return null;
    }
}
