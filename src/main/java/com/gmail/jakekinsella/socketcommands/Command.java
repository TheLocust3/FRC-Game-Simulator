package com.gmail.jakekinsella.socketcommands;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/3/16.
 */
public class Command {

    private String name;
    private JSONArray args = new JSONArray();
    private JSONObject jsonArgs = new JSONObject();

    public Command(String name, ArrayList<String> args) {
        this.name = name;
        this.args.addAll(args);
    }

    public Command(String name, JSONObject args) {
        this.name = name;
        this.args.add(args);
    }

    public Command(JSONObject jsonObject) {
        this((String) jsonObject.get("command"), (ArrayList<String>) jsonObject.get("args"));
    }

    public String getName() {
        return this.name;
    }

    public JSONArray getArgs() {
        return args;
    }

    public Object getArg(int index) {
        return this.args.get(index);
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("command", this.name);

        obj.put("args", this.args);

        return obj;
    }
}
