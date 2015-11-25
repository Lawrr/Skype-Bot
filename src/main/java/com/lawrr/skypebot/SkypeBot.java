package com.lawrr.skypebot;

import com.lawrr.skypebot.modules.AsciiModule;
import com.lawrr.skypebot.modules.YoutubeModule;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class SkypeBot {

    private EzSkype ezSkype;

    public static void main(String[] args) {
        new SkypeBot();
    }

    public SkypeBot() {
        init();
    }

    private void init() {
        try {
            // Get credentials
            SkypeBotCredentials credentials = EzSkype.GSON.fromJson(new FileReader(new File("credentials.json")), SkypeBotCredentials.class);

            // Create EzSkype instance
            ezSkype = new EzSkype(new SkypeCredentials(credentials.getUsername(), credentials.getPassword()));
            ezSkype.login();

            // Create modules
            YoutubeModule youtubeModule = new YoutubeModule(credentials.getYoutubeApiKey());
            AsciiModule asciiModule = new AsciiModule();

            // Register events
            ezSkype.getEventManager().registerEvents(this);
            ezSkype.getEventManager().registerEvents(youtubeModule);
            ezSkype.getEventManager().registerEvents(asciiModule);

            // Start
            System.out.println("Bot started");
        } catch (FileNotFoundException e) {
            System.out.println("Credentials not found");
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Error occurred");
            System.exit(1);
        }
    }

    // Called when a captcha needs to be solved
    // Returns the solution to the captcha
    @Override
    public String solve(SkypeCaptcha skypeCaptcha) {
        System.out.println("Enter the solution to " + skypeCaptcha.getUrl() + " then click enter");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
