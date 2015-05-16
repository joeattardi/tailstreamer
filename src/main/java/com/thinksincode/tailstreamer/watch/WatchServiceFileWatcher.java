package com.thinksincode.tailstreamer.watch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import name.pachler.nio.file.*;
import java.util.List;

public class WatchServiceFileWatcher extends AbstractFileWatcher implements FileWatcher {
    private Logger logger = LoggerFactory.getLogger(WatchServiceFileWatcher.class);

    /** Flag that indicates whether the watch service should continue. */
    private boolean watch = true;

    /**
     * Starts watching a file.
     */
    public void watchFile(final java.nio.file.Path watchedFile) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            // WatchService only watches directories, so watch the file's parent
            Path parent = Paths.get(watchedFile.getParent().toFile().getAbsolutePath());
            parent.register(watchService, StandardWatchEventKind.ENTRY_MODIFY);

            // Wait for changes
            for (boolean valid = true; watch && valid; ) {
                WatchKey key = watchService.take();
                List<WatchEvent<?>> events = key.pollEvents();
                for (WatchEvent<?> event : events) {
                    // overflow events can happen, we don't care about them
                    if (event.kind() == StandardWatchEventKind.OVERFLOW) {
                        continue;
                    }

                    // Events will fire for any files in the directory. Only
                    // respond to changes to the file being watched
                    Path changedFile = (Path) event.context();
                    if (changedFile.toString().equals(watchedFile.getFileName().toString())) {
                        notifyListeners(new FileUpdateEvent(this));
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
     * Signals the watcher to stop watching.
     */
    public void stop() {
        watch = false;
    }
}
