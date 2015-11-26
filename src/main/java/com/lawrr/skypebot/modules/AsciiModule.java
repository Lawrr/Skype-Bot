package com.lawrr.skypebot.modules;

import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

public class AsciiModule {

    public void onMessage(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        String replyMessage = "";
        switch (message) {
            case "!shrug": {
                replyMessage = "¯\\_(ツ)_/¯";
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
