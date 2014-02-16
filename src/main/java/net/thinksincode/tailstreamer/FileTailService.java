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
    
    private Path file;
    
    /**
     * Sets the file that will be tailed.
     * @param filePath The path of the file to tail.
     */
    public void setFile(final String filePath) {
        file = Paths.get(filePath).toAbsolutePath();
    }
    
    /**
     * Gets the path of the file being tailed.
     * @return the absolute path of the file
     */
    public String getFile() {
        return file.toString();
    }
    
    /**
     * Begins the file tail operation on a file.
     */
    @Async
    public void tailFile() {
        logger.info("Tailing " + file);
        reader.openFile(file);
        watcher.watchFile(file);
    }
}
