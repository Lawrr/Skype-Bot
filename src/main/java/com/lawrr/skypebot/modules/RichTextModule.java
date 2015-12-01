package com.lawrr.skypebot.modules;

import com.lawrr.skypebot.MessageParser;
import in.kyle.ezskypeezlife.Chat;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RichTextModule implements Module {

    private List<String> commands = new ArrayList<>(
            Arrays.asList(
                    "!blink",
                    "!colour",
                    "!size"
            )
    );
    private String username;

    public RichTextModule(String username) {
        // Set properties
        this.username = username;
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
            String editedMessage = "";
            switch (command.get(0)) {
                // Blink
                case "!blink": {
                    if (command.size() > 1 && senderUsername.equals(username)) {
                        editedMessage = String.format("<blink>%s</blink>", message.substring("!blink ".length()));
                    }
                    break;
                }

                // Colour
                case "!colour": {
                    if (command.size() > 2 && senderUsername.equals(username)) {
                        String color = command.get(1);
                        // Check if possible colour
                        if (color.equals("rainbow")) {
                            // Rainbow colour
                            editedMessage = rainbowTag(message.substring("!colour rainbow ".length()));
                        } else if (color.length() == 6) {
                            // Solid colour
                            // Check if valid hex for colour
                            if (Integer.parseInt(color, 16) >= 0 && Integer.parseInt(color, 16) <= Integer.parseInt("ffffff", 16)) {
                                editedMessage = Chat.color(message.substring("!colour ffffff ".length()), "#" + color);
                            }
                        }
                    }
                    break;
                }

                // Size
                case "!size": {
                    if (command.size() > 2 && senderUsername.equals(username)) {
                        try {
                            // Check if valid size
                            int size = Integer.parseInt(command.get(1));
                            if (size > 0) {
                                editedMessage = Chat.size(message.substring(("!size " + command.get(1) + " ").length()), size);
                            }
                        } catch (NumberFormatException exception) {
                            // Invalid size
                        }
                    }
                    break;
                }

                // Html from message
                case "!html": {
                    if (senderUsername.equals(username)) {
                        editedMessage = message.substring("!html ".length());
                    }
                    break;
                }

                // Other
                default: {
                    break;
                }

            }

            if (!editedMessage.equals("")) {
                e.getMessage().edit(editedMessage);
            }
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
