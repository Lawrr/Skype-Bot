package com.lawrr.skypebot;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandParser {

    public static ArrayList<String> parse(String message) {
        ArrayList<String> command = new ArrayList<String>();
        String[] words = message.split(" ");

        // Check length
        if (words.length > 0) {
            // Check if actually a command
            if (words[0].indexOf("!") == 0) {
                // Get command
                command = new ArrayList<String>(Arrays.asList(words));
            }
        }

        return command;
    }

}
