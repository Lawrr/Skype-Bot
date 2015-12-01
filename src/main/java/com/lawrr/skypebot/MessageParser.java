package com.lawrr.skypebot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageParser {

    public static List<String> toCommand(String message) {
        // Strip tags
        String taglessMessage = message.replaceAll("<.+?>", "");

        // Split into words
        String[] words = taglessMessage.split(" ");
        List<String> command = new ArrayList<String>();

        // Check if actually a command
        if (words[0].indexOf("!") == 0) {
            // Get command and params
            command = new ArrayList<String>(Arrays.asList(words));
        }

        return command;
    }

}
