package com.thinksincode.tailstreamer.watch;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract base {@link FileWatcher} class that handles event
 * notifications.
 */
public abstract class AbstractFileWatcher implements FileWatcher {
    private List<FileListener> listeners = new ArrayList<>();

    /**
     * Adds a {@link FileListener} to the list of listeners.
     * @param listener the {@link FileListener} to add
     */
    @Override
    public void addFileListener(FileListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a {@link FileListener} from the list of listeners.
     * @param listener the {@link FileListener} to remove
     */
    @Override
    public void removeFileListener(FileListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered {@link FileListener}s that a {@link FileUpdateEvent}
     * has occurred.
     * @param event the {@link FileUpdateEvent} that occurred
     */
    protected void notifyListeners(FileUpdateEvent event) {
        for (FileListener listener : listeners) {
            listener.fileChanged(event);
        }
    }
}
