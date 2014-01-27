package net.thinksincode.tailstreamer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Handles the broadcasting of messages to connected SockJS clients.
 */
@Component
public class FileContentBroadcaster {
    @Autowired
    private SimpMessagingTemplate template;
    
    /**
     * Broadcasts new content to any subscribed clients.
     * @param lines The lines to broadcast
     */
    public void sendContent(String...lines) {
        for (String line : lines) {
            template.convertAndSend("/topic/log", line);
        }    
    }
}
