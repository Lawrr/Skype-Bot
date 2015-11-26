package com.lawrr.skypebot.modules;

import com.lawrr.skypebot.CommandParser;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.util.ArrayList;

public class AsciiModule {

    public AsciiModule() {

    }

    public void handleCommands(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        ArrayList<String> command = CommandParser.parse(message);

        // Check if possible command
        if (command.size() > 0) {
            String replyMessage = "";
            switch (command.get(0)) {

                // Shrug
                case "!shrug": {
                    replyMessage += "¯\\_(ツ)_/¯";
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

}
