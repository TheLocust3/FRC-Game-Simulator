package com.gmail.jakekinsella;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by jakekinsella on 9/23/16.
 */
public class JSONFileReader {

    private JSONObject jsonObject;

    private static final Logger logger = LogManager.getLogger();

    public JSONFileReader(String filePath) {
        this.parse(filePath);
    }

    public JSONObject getJSONObject() {
        return this.jsonObject;
    }

    private void parse(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String text = "";
            String line = br.readLine();
            while (line != null) {
                text += line;
                line = br.readLine();
            }

            br.close();

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(text);
            this.jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            logger.error("Error in parsing JSON", e);
        }
    }
}
