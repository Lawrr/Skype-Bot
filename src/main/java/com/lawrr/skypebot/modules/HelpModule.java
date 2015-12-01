package com.lawrr.skypebot.modules;

import com.google.api.client.util.Joiner;
import com.lawrr.skypebot.MessageParser;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpModule implements Module {

    private List<String> commands = new ArrayList<>(
            Arrays.asList(
                    "!help"
            )
    );
    private List<Module> modules;

    public HelpModule() {
        modules = new ArrayList<Module>();
    }

    public HelpModule(List<Module> modules) {
        this.modules = modules;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void handleCommands(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        List<String> command = MessageParser.toCommand(message);

        // Check if possible command
        if (command.size() > 0) {
            String replyMessage = "";
            switch (command.get(0)) {
                // Help
                case "!help": {
                    List<String> allCommands = new ArrayList<String>();
                    for (Module m : modules) {
                        for (String s : m.getCommands()) {
                            allCommands.add(s);
                        }
                    }
                    replyMessage = Joiner.on('\n').join(allCommands);
                    break;
                }

                // Other
                default: {
                    break;
                }

            }

            if (!replyMessage.equals("")) {
                e.reply(StringEscapeUtils.escapeHtml4(replyMessage));
            }
        }
    }

}
