package com.thinksincode.tailstreamer.watch;

import java.nio.file.Path;

public interface FileWatcher {
    public void watchFile(Path file);
    public void removeFileListener(FileListener listener);
    public void addFileListener(FileListener listener);
}
