package com.lawrr.skypebot.modules;

import com.lawrr.skypebot.MessageParser;
import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RichTextModule implements Module {

    public static String RAINBOW_COLOR = "rainbow_color";

    private List<String> commands = new ArrayList<>(
            Arrays.asList(
                    "!blink",
                    "!colour",
                    "!size"
            )
    );
    private String username;
    private boolean blinkEnabled;
    private boolean colorEnabled;
    private boolean sizeEnabled;
    private String color;
    private int size;

    public RichTextModule(String username) {
        // Set properties
        this.username = username;
        blinkEnabled = false;
        colorEnabled = false;
        sizeEnabled = false;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void handleCommands(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        String senderUsername = e.getMessage().getSender().getUsername();
        List<String> command = MessageParser.toCommand(message);

        // Check if possible command
        if (command.size() > 0) {
            String replyMessage = "";
            switch (command.get(0)) {
                // Blink
                case "!blink": {
                    if (command.size() == 2 && senderUsername.equals(username)) {
                        String state = command.get(1);
                        switch (state) {
                            case "on": {
                                // Check if already on
                                if (!blinkEnabled) {
                                    replyMessage = "Blink enabled";
                                    blinkEnabled = true;
                                }
                                break;
                            }
                            case "off": {
                                // Check if already off
                                if (blinkEnabled) {
                                    replyMessage = "Blink disabled";
                                    blinkEnabled = false;
                                }
                                break;
                            }
                        }
                    }
                    break;
                }

                // Colour
                case "!colour": {
                    if (command.size() == 2 && senderUsername.equals(username)) {
                        String requestedColor = command.get(1);
                        switch (requestedColor) {
                            case "off": {
                                // Check if already off
                                if (colorEnabled) {
                                    replyMessage = "Colour disabled";
                                    colorEnabled = false;
                                }
                                break;
                            }
                            default: {
                                // Check if possible color
                                boolean isColor = false;
                                if (requestedColor.equals("rainbow")) {
                                    isColor = true;
                                    color = RAINBOW_COLOR;
                                } else if (requestedColor.length() == 6) {
                                    // Check if valid colour
                                    if (Integer.parseInt(requestedColor, 16) >= 0 && Integer.parseInt(requestedColor, 16) <= Integer.parseInt("ffffff", 16)) {
                                        isColor = true;
                                        color = requestedColor;
                                    }
                                }
                                if (isColor) {
                                    replyMessage = "Colour enabled";
                                    colorEnabled = true;
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
                // Size
                case "!size": {
                    if (command.size() == 2 && senderUsername.equals(username)) {
                        String requestedSize = command.get(1);
                        switch (requestedSize) {
                            case "off": {
                                // Check if already off
                                if (sizeEnabled) {
                                    replyMessage = "Size disabled";
                                    sizeEnabled = false;
                                }
                                break;
                            }
                            default: {
                                // Check if valid size
                                try {
                                    int requestedSizeInt = Integer.parseInt(requestedSize);
                                    if (requestedSizeInt > 0) {
                                        size = requestedSizeInt;
                                        replyMessage = "Size enabled";
                                        sizeEnabled = true;
                                    }
                                } catch (NumberFormatException exception) {
                                }
                                break;
                            }
                        }
                    }
                    break;
                }

                // Other
                default: {
                    break;
                }

            }

            if (!replyMessage.equals("")) {
                e.reply(MessageParser.encode(replyMessage));
            }
        }
    }

    public void editTags(SkypeMessageReceivedEvent e) {
        String message = e.getMessage().getMessage();
        String senderUsername = e.getMessage().getSender().getUsername();
        String editedMessage = message;

        // Check for color text
        if (colorEnabled && senderUsername.equals(username)) {
            if (color.equals(RAINBOW_COLOR)) {
                // Rainbow color
                editedMessage = rainbowTag(message);
            } else {
                // Normal color
                editedMessage = Chat.color(editedMessage, "#" + color);
            }
        }

        // Check for size text
        if (sizeEnabled && senderUsername.equals(username)) {
            editedMessage = Chat.size(editedMessage, size);
        }

        // Check for blink text
        if (blinkEnabled && senderUsername.equals(username)) {
            editedMessage = String.format("<blink>%s</blink>", editedMessage);
        }

        // Edit original message if different
        if (!message.equals(editedMessage)) {
            e.getMessage().edit(MessageParser.encode(editedMessage));
        }
    }

    private String rainbowTag(String message) {
        String rainbowMessage = "";
        int currHexColor[] = {255, 0, 0};
        int colNum;
        int currCharIndex = 0;
        int incrementValue = (int) Math.ceil((255 * 6) / message.length());
        rainbowLoop:
        for (int cycle = 0; cycle < 1; cycle++) {
            for (int color = 1; color <= 6; color++) {
                for (int value = 0; value < 256; value += incrementValue) {
                    // Exit if end of message
                    if (currCharIndex == message.length()) {
                        break rainbowLoop;
                    }

                    // Increment color
                    if ((double) color / 2 == Math.floor(color / 2)) {
                        colNum = (int) Math.floor(color / 2);
                        currHexColor[colNum - 1] -= incrementValue;
                    } else {
                        colNum = (int) Math.ceil(color / 2) + 1;
                        if (colNum >= 3) {
                            colNum -= 3;
                        }
                        currHexColor[colNum] += incrementValue;
                    }

                    // Set max values for hex
                    for (int i = 0; i < currHexColor.length; i++) {
                        if (currHexColor[i] > 255) {
                            currHexColor[i] = 255;
                        } else if (currHexColor[i] < 0) {
                            currHexColor[i] = 0;
                        }
                    }

                    // Apply tag
                    String rString = StringUtils.leftPad(Integer.toString(currHexColor[0], 16), 2, '0');
                    String gString = StringUtils.leftPad(Integer.toString(currHexColor[1], 16), 2, '0');
                    String bString = StringUtils.leftPad(Integer.toString(currHexColor[2], 16), 2, '0');
                    String hexColor = "#" + rString + gString + bString;
                    rainbowMessage += Chat.color(Character.toString(message.charAt(currCharIndex)), hexColor);
                    currCharIndex++;
                }
            }
        }
        return rainbowMessage;
    }

}
