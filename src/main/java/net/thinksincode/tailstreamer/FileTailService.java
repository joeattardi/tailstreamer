package net.thinksincode.tailstreamer;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("fileTailService")
public class FileTailService {
    
    @Async
    public void tailFile(final String filePath) {
        try {
            FileTail tail = new FileTail(filePath);
            tail.start();
        } catch (IOException ioe) {
            // TODO handle ioe
            ioe.printStackTrace();
        }
    }
}
