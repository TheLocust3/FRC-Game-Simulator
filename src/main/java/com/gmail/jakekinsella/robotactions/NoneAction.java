package com.gmail.jakekinsella.robotactions;

import com.gmail.jakekinsella.socketcommands.Command;

import java.util.ArrayList;

/**
 * Created by jakekinsella on 10/5/16.
 */
public class NoneAction extends Action {

    public NoneAction() {
        super(new Command("NONE", new ArrayList<>()), null);
    }
}
