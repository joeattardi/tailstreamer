package net.thinksincode.tailstreamer;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("fileTailService")
public class FileTailService {
    
    @Autowired
    private FileWatcher watcher;
    
    @Autowired
    private FileContentReader reader;
    
    @Async
    public void tailFile(final String filePath) {
        Path file = Paths.get(filePath);
        reader.openFile(file);
        watcher.watchFile(file);
    }
}
