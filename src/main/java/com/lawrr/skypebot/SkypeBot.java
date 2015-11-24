package com.lawrr.skypebot;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SkypeBot {

    private EzSkype ezSkype;

    public static void main(String[] args) {
        new SkypeBot();
    }

    public SkypeBot() {
        init();
    }

    private void init() {
        LoginCredentials loginCredentials;
        try {

            // Get login credentials
            loginCredentials = EzSkype.GSON.fromJson(new FileReader(new File("credentials.json")), LoginCredentials.class);

            // Create EzSkype instance
            ezSkype = new EzSkype(new SkypeCredentials(loginCredentials.getUsername(), loginCredentials.getPassword()));
            ezSkype.login();

            // Register events
            ezSkype.getEventManager().registerEvents(this);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error occurred");
            System.exit(1);
        }

    }

    public void onMessage(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        if (message.equals("ping")) {
            e.reply("pong!");
        }
    }

}
