package net.thinksincode.tailstreamer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Observable;

/**
 * Watches a file for changes, and notifies observers when the file is updated.
 */
public class FileWatcher extends Observable {
    /** The file being watched. */
    private Path watchedFile;
    
    /**
     * Creates a FileWatcher for a specified file.
     * @param filePath The full path and filename
     * @throws FileNotFoundException if the specified file does not exist
     */
    public FileWatcher(final Path watchedFile) {
        this.watchedFile = watchedFile;
    }
    
    /**
     * Starts watching a file.
     */
    public void watchFile() {
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
                        setChanged();
                        notifyObservers();
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
}
