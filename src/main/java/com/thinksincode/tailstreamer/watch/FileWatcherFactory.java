package com.thinksincode.tailstreamer.watch;

public class FileWatcherFactory {
    public static FileWatcher getFileWatcher() {
        return new WatchServiceFileWatcher();
    }
}
