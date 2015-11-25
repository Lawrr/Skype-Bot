package com.lawrr.skypebot.modules;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeModule {

    private YouTube youtube;

    public YoutubeModule(String apiKey) {
        // Initialise youtube api
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey)).setApplicationName("Skype Bot").build();
    }

    public void onMessage(SkypeMessageReceivedEvent e) {
        // Search message for youtube links
        String message = e.getMessage().getMessage();
        ArrayList<String> videoIds = getVideoIds(message);

        // Max 5 videos in a message
        if (videoIds.size() <= 5) {
            // Get videos data
            Joiner stringJoiner = Joiner.on(',');
            String videoIdsString = stringJoiner.join(videoIds);
            ArrayList<Video> videos = getVideos(videoIdsString);

            // Send message with info
            for(Video v : videos) {
                String title = v.getSnippet().getTitle();
                String duration = formatDuration(v.getContentDetails().getDuration());
                e.reply(String.format("Video: %s [%s]", title, duration));
            }
        }
    }

    private ArrayList<String> getVideoIds(String message) {
        // Get matches
        Pattern linkPattern = Pattern.compile("(https?://)?(www.)?(m.)?(youtube.com|youtu.be)/(.*?v=)?([\\w\\-_]{9,})([&?].*)?");
        Matcher matcher = linkPattern.matcher(message);

        // Add to list
        ArrayList<String> videoIds = new ArrayList<String>();
        while (matcher.find()) {
            // Add unique
            if (!videoIds.contains(matcher.group(6))) {
                videoIds.add(matcher.group(6));
            }
        }

        return videoIds;
    }

    private ArrayList<Video> getVideos(String videoIdsString) {
        ArrayList<Video> videos = new ArrayList<Video>();
        try {
            YouTube.Videos.List request = youtube.videos().list("snippet, contentDetails").setId(videoIdsString);
            VideoListResponse response = request.execute();
            if (response != null) {
                videos.addAll(response.getItems());
            }
        } catch (Exception e) {
            System.out.println("Could not find videos info");
        }

        return videos;
    }

    private String formatDuration(String durationString) {
        Duration duration = Duration.parse(durationString);
        int hours = (int) (duration.getSeconds() / (60 * 60));
        int minutes = (int) (duration.getSeconds() / 60) % 60;
        int seconds = (int) duration.getSeconds() % 60;

        String formattedDuration;
        if (hours > 0) {
            formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            formattedDuration = String.format("%02d:%02d", minutes, seconds);
        }

        return formattedDuration;
    }
}
