package com.thinksincode.tailstreamer;

import com.thinksincode.tailstreamer.watch.FileListener;
import com.thinksincode.tailstreamer.watch.FileUpdateEvent;
import com.thinksincode.tailstreamer.watch.FileWatcher;
import com.thinksincode.tailstreamer.watch.FileWatcherFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

/**
 * Watches a file for changes, and notifies observers when the file is updated.
 */
@Service("fileWatcher")
public class FileWatcherService implements ApplicationEventPublisherAware, FileListener {
    private Logger logger = LoggerFactory.getLogger(FileWatcherService.class);
    
    private ApplicationEventPublisher eventPublisher;
    
    /** Flag that indicates whether the watch service should continue. */
    private boolean watch = true;
    
    /**
     * Starts watching a file.
     */
    public void watchFile(final Path watchedFile) {
        FileWatcher watcher = FileWatcherFactory.getFileWatcher();
        watcher.addFileListener(this);
        watcher.watchFile(watchedFile);
    }

    /**
     * Called when changes have been detected to the file.
     */
    @Override
    public void fileChanged(FileUpdateEvent event) {
        eventPublisher.publishEvent(event);
    }
    
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
