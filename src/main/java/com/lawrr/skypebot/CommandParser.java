package com.lawrr.skypebot;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {

    public static ArrayList<String> parse(String message) {
        String[] words = message.split(" ");
        ArrayList<String> command = new ArrayList<String>();

        // Check if actually a command
        if (words[0].indexOf("!") == 0) {
            // Get command and params
            command = new ArrayList<String>(Arrays.asList(words));
        }

        return command;
    }

}
