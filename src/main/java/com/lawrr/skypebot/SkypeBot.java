package com.lawrr.skypebot;

import com.lawrr.skypebot.modules.YoutubeModule;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;

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
        SkypeBotCredentials credentials;
        try {
            // Get login credentials
            credentials = EzSkype.GSON.fromJson(new FileReader(new File("credentials.json")), SkypeBotCredentials.class);

            // Create EzSkype instance
            ezSkype = new EzSkype(new SkypeCredentials(credentials.getUsername(), credentials.getPassword()));
            ezSkype.login();

            // Create modules
            YoutubeModule youtubeModule = new YoutubeModule(credentials.getYoutubeApiKey());

            // Register events
            ezSkype.getEventManager().registerEvents(youtubeModule);

            // Start
            System.out.println("Bot started");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error occurred");
            System.exit(1);
        }
    }
}
