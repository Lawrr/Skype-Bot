package com.lawrr.skypebot.modules;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeModule implements Module {

    public List<String> commands = new ArrayList<>(
            Arrays.asList(
            )
    );

    private YouTube youtube;

    public YoutubeModule(String apiKey) {
        // Initialise youtube api
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {
        }).setYouTubeRequestInitializer(new YouTubeRequestInitializer(apiKey)).setApplicationName("Skype Bot").build();
    }

    public List<String> getCommands() {
        return commands;
    }

    public void displayYoutubeInfo(SkypeMessageReceivedEvent e) {
        // Search message for youtube links
        String message = e.getMessage().getMessage();
        List<String> videoIds = getVideoIds(message);

        // Set max video ids index so that maximum of 5 videos data are queried
        int maxIndex = videoIds.size() <= 5 ? videoIds.size() : 5;
        // Get videos data
        String videoIdsString = Joiner.on(',').join(videoIds.subList(0, maxIndex));
        List<Video> videos = getVideos(videoIdsString);

        // Format videos data
        List<String> videoDetails = new ArrayList<String>();
        for(Video v : videos) {
            String title = v.getSnippet().getTitle();
            String duration = formatDuration(v.getContentDetails().getDuration());
            videoDetails.add(String.format("Video: %s [%s]", title, duration));
        }

        // Send message with info
        if (videoDetails.size() > 0) {
            String replyMessage = Joiner.on('\n').join(videoDetails);
            e.reply(replyMessage);
        }
    }

    private List<String> getVideoIds(String message) {
        // Get matches
        Pattern linkPattern = Pattern.compile("(https?://)?(www.)?(m.)?(youtube.com|youtu.be)/(.*?v=)?([\\w\\-_]+)([&?].*)?");
        Matcher matcher = linkPattern.matcher(message);

        // Add to list
        List<String> videoIds = new ArrayList<String>();
        while (matcher.find()) {
            // Add unique
            if (!videoIds.contains(matcher.group(6))) {
                videoIds.add(matcher.group(6));
            }
        }

        return videoIds;
    }

    private List<Video> getVideos(String videoIdsString) {
        List<Video> videos = new ArrayList<Video>();
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
