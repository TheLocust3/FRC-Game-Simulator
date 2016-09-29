package com.gmail.jakekinsella.socketcommands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public final String COMMAND_PREFIX = "COMMAND";
    public final String COMMAND_RESPONSE_PREFIX = "COMMAND_RESPONSE";
    public final String BOOLEAN_TRUE = "TRUE";
    public final String BOOLEAN_FALSE = "FALSE";

    private Socket socket;
    private PrintWriter socketOutput;
    private BufferedReader socketReader;

    private static final Logger logger = LogManager.getLogger();

    public SocketCommand(Socket socket) throws IOException {
        this.socket = socket;
        this.socketOutput = new PrintWriter(this.socket.getOutputStream(), true);
        this.socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    protected void sendCommand(String command, String... args) {
        ArrayList<String> argsList = new ArrayList<>(Arrays.asList(args));

        String fullCommand = String.format("%s %s(%s)", this.COMMAND_PREFIX, command, String.join(",", argsList));

        logger.debug("Sending: " + fullCommand);
        writeln(fullCommand);
    }

    protected String getResponse() throws Exception {
        String response = readln();

        if (response.contains(this.COMMAND_RESPONSE_PREFIX + " ")) {
            String finalResponse = response.replace(this.COMMAND_RESPONSE_PREFIX + " ", "");

            logger.debug("Received: " + finalResponse);
            return finalResponse;
        }

        throw new Exception("Client failed to respond correctly");
    }

    private void writeln(String out) {
        this.socketOutput.println(out);
    }

    private String readln() {
        try {
            while (!this.socketReader.ready()) {
                logger.debug("Waiting to receive response from client...");
            }

            return this.socketReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
