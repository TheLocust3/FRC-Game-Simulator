package com.gmail.jakekinsella.socketcommands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/3/16.
 */
public class Command {

    private String name;
    private ArrayList<String> args = new ArrayList<>();
    private JSONObject jsonArgs = new JSONObject();

    public Command(String name, ArrayList<String> args) {
        this.name = name;
        this.args = args;
    }

    public Command(String name, JSONObject args) {
        this.name = name;
        this.jsonArgs = args;
    }

    public Command(JSONObject jsonObject) {
        this((String) jsonObject.get("command"), (ArrayList<String>) jsonObject.get("args"));
    }

    public String getName() {
        return this.name;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("command", this.name);

        JSONArray array = new JSONArray();

        if (this.args.isEmpty() && !this.jsonArgs.isEmpty()) {
            array.add(this.jsonArgs);
        } else {
            array.addAll(this.args);
        }

        obj.put("args", array);

        return obj;
    }
}
