package net.thinksincode.tailstreamer;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Watches a file for changes, and notifies observers when the file is updated.
 */
@Service("fileWatcher")
public class FileWatcher implements ApplicationEventPublisherAware {           
    private Logger logger = LoggerFactory.getLogger(FileWatcher.class);
    
    private ApplicationEventPublisher eventPublisher;
    
    /** Flag that indicates whether the watch service should continue. */
    private boolean watch = true;
    
    /**
     * Starts watching a file.
     */
    public void watchFile(final Path watchedFile) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            // WatchService only watches directories, so watch the file's parent
            watchedFile.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            
            // Wait for changes
            for (boolean valid = true; watch && valid; ) {
                WatchKey key = watchService.take();
                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> event : events) {
                    // overflow events can happen, we don't care about them
                    if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }
                    
                    // Events will fire for any files in the directory. Only 
                    // respond to changes to the file being watched
                    Path changedFile = (Path) event.context();
                    if (changedFile.getFileName().equals(watchedFile.getFileName())) {
                        fileChanged();
                    }
                }
                
                valid = key.reset();
            }
        } catch (IOException | ClosedWatchServiceException e) {
            logger.error("Error while watching file: " + e.getMessage(), e); 
        } catch (InterruptedException ie) {
            logger.warn("Watch service was interrupted", ie);
        }
    }

    /**
     * Called when changes have been detected to the file.
     */
    void fileChanged() {
        eventPublisher.publishEvent(new FileUpdateEvent(this));
    }
    
    /**
     * Signals the watcher to stop watching.
     */
    public void stop() {
        watch = false;
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
