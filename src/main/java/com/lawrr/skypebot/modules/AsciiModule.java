package com.lawrr.skypebot.modules;

import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

public class AsciiModule {

    public void onMessage(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        String replyMessage = "";
        switch (message) {
            default: {
                break;
            }
        }

        if (!replyMessage.equals("")) {
            e.reply(replyMessage);
        }
    }

}
