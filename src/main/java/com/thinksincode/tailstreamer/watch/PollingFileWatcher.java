package com.thinksincode.tailstreamer.watch;

import java.nio.file.Path;

/**
 * A {@link FileWatcher} that polls at regular intervals to see if a file has changed.
 */
public class PollingFileWatcher extends AbstractFileWatcher implements FileWatcher {
    @Override
    public void watchFile(Path file) {

    }
}
