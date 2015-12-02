package com.lawrr.skypebot.modules;

import com.lawrr.skypebot.MessageParser;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsciiModule implements Module {

    private List<String> commands = new ArrayList<>(
            Arrays.asList(
                    "!shrug"
            )
    );

    public AsciiModule() {

    }

    public List<String> getCommands() {
        return commands;
    }

    public void handleCommands(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        List<String> command = MessageParser.toCommand(message);

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
