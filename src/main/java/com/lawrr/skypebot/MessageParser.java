package com.lawrr.skypebot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageParser {
    public static List<String> toCommand(String message) {
        // Split into words
        String[] words = message.split(" ");
        List<String> command = new ArrayList<>();

        // Check if actually a command
        if (words.length > 0) {
            if (words[0].indexOf("!") == 0) {
                // Get command and params
                command = new ArrayList<>(Arrays.asList(words));
            }
        }

        return command;
    }
}
