package net.thinksincode.tailstreamer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("fileTailService")
public class FileTailService {
    
    @Autowired
    private SimpMessagingTemplate template;
    
    @Async
    public void tailFile(final String filePath) {
        try {
            FileTail tail = new FileTail(filePath) {
                @Override
                public void onNewContent(final String content) {
                    template.convertAndSend("/topic/log", content);
                }
            };
            tail.start();
        } catch (IOException ioe) {
            // TODO handle ioe
            ioe.printStackTrace();
        }
    }
}
