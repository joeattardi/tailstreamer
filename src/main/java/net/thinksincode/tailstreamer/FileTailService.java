package net.thinksincode.tailstreamer;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * A service that coordinates the file tail operation. The service runs asynchronously, and 
 * maintains a FileWatcher and FileContentReader for the specified file. 
 */
@Service("fileTailService")
public class FileTailService {
    
    private Logger logger = LoggerFactory.getLogger(FileTailService.class);
    
    @Autowired
    private FileWatcher watcher;
    
    @Autowired
    private FileContentReader reader;
    
    /**
     * Begins the file tail operation on a file.
     * @param filePath The path of the file to tail.
     */
    @Async
    public void tailFile(final String filePath) {
        logger.info("Tailing " + filePath);
        
        Path file = Paths.get(filePath);
        reader.openFile(file);
        watcher.watchFile(file);
    }
}
