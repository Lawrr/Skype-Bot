package com.lawrr.skypebot.modules;

import com.lawrr.skypebot.CommandParser;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.util.ArrayList;

public class RichTextModule {

    private String username;
    private boolean blinkEnabled;
    private boolean colorEnabled;
    private String color;

    public RichTextModule(String username) {
        // Set properties
        this.username = username;
        blinkEnabled = false;
        colorEnabled = false;
    }

    public void handleCommands(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        String senderUsername = e.getMessage().getSender().getUsername();
        ArrayList<String> command = CommandParser.parse(message);

        // Check if possible command
        if (command.size() > 0) {
            String replyMessage = "";
            switch (command.get(0)) {

                // Blink
                case "!blink": {
                    if (command.size() == 2 && senderUsername.equals(username)) {
                        String state = command.get(1);
                        switch (state) {
                            case "on": {
                                // Check if already on
                                if (!blinkEnabled) {
                                    replyMessage = "Blink enabled";
                                    blinkEnabled = true;
                                }
                                break;
                            }
                            case "off": {
                                // Check if already off
                                if (blinkEnabled) {
                                    replyMessage = "Blink disabled";
                                    blinkEnabled = false;
                                }
                                break;
                            }
                        }
                    }
                    break;
                }

                // Colour
                case "!colour": {
                    if (command.size() == 2 && senderUsername.equals(username)) {
                        String newColor = command.get(1);
                        switch (newColor) {
                            case "off": {
                                // Check if already off
                                if (colorEnabled) {
                                    replyMessage = "Colour disabled";
                                    colorEnabled = false;
                                }
                                break;
                            }
                            default: {
                                // Check if possible color
                                if (newColor.length() == 6) {
                                    // Check if valid colour
                                    int r = Integer.parseInt(newColor.substring(0, 2), 16);
                                    int g = Integer.parseInt(newColor.substring(2, 4), 16);
                                    int b = Integer.parseInt(newColor.substring(4, 6), 16);
                                    if (r <= 255 && r >= 0 && g <= 255 && g >= 0 && b <= 255 && b >= 0) {
                                        color = newColor;
                                        replyMessage = "Colour enabled";
                                        colorEnabled = true;
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                }

                // Other
                default: {
                    break;
                }

            }

            if (!replyMessage.equals("")) {
                e.reply(replyMessage);
            }
        }
    }

    public void editTags(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        String senderUsername = e.getMessage().getSender().getUsername();
        String editedMessage = message;

        // Check for blink text
        if (blinkEnabled && senderUsername.equals(username)) {
            editedMessage = String.format("<blink>%s</blink>", editedMessage);
        }

        // Check for color text
        if (colorEnabled && senderUsername.equals(username)) {
            editedMessage = String.format("<font color=\"#%s\">%s</font>", color, editedMessage);
        }

        // Edit original message if different
        if (!message.equals(editedMessage)) {
            e.getMessage().edit(editedMessage);
        }
    }

}
