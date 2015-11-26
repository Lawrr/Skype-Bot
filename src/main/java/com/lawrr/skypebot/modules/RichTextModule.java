package com.lawrr.skypebot.modules;

import com.lawrr.skypebot.CommandParser;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.util.ArrayList;

public class RichTextModule {

    private String username;
    private boolean blinkEnabled;

    public RichTextModule(String username) {
        // Set properties
        this.username = username;
        blinkEnabled = false;
    }

    public void handleCommands(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        ArrayList<String> command = CommandParser.parse(message);

        // Check if possible command
        if (command.size() > 0) {
            String replyMessage = "";
            switch (command.get(0)) {
                case "!blink": {
                    if (command.size() == 2) {
                        switch (command.get(1)) {
                            case "on": {
                                replyMessage = "Blink enabled";
                                blinkEnabled = true;
                                break;
                            }
                            case "off": {
                                replyMessage = "Blink disabled";
                                blinkEnabled = false;
                                break;
                            }
                        }
                    }
                    break;
                }

                default: {
                    break;
                }
            }

            if (!replyMessage.equals("")) {
                e.reply(replyMessage);
            }
        }
    }

    public void editBlinkText(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();

        // Check for blink text
        if (blinkEnabled && e.getMessage().getSender().getUsername().equals(username)) {
            e.getMessage().edit(String.format("<blink>%s</blink>", message));
        }
    }

}
