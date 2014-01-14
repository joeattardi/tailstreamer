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

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Watches a file for changes, and notifies observers when the file is updated.
 */
@Service("fileWatcher")
public class FileWatcher implements ApplicationEventPublisherAware {           
    
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * Starts watching a file.
     */
    public void watchFile(final Path watchedFile) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            
            // WatchService only watches directories, so watch the file's parent
            watchedFile.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            
            // Wait for changes
            for (boolean valid = true; valid; ) {
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
                        eventPublisher.publishEvent(new FileUpdateEvent(this));
                    }
                }
                
                valid = key.reset();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // TODO handle exception
        } catch (ClosedWatchServiceException cwse) {
            cwse.printStackTrace();
            // TODO handle exception
        } catch (InterruptedException ie) {
            ie.printStackTrace();
            // TODO handle exception
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
