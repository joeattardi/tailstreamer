package com.thinksincode.tailstreamer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.thinksincode.tailstreamer.watch.FileListener;
import com.thinksincode.tailstreamer.watch.FileUpdateEvent;
import com.thinksincode.tailstreamer.watch.WatchServiceFileWatcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class WatchServiceFileWatcherTests {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();    
    
    @Test
    public void testWatchFile() throws IOException, InterruptedException {
        File file = folder.newFile();
        
        FileWatcherThread thread = new FileWatcherThread(file);
        thread.start();
        
        Thread.sleep(1000);
        
        FileWriter writer = new FileWriter(file);
        writer.write("Hello world!\n");
        writer.close();
        
        thread.join();
        Assert.assertTrue(thread.updated());
    }
    
    class FileWatcherThread extends Thread {
        private File file;        
        
        private boolean updated;
        
        public FileWatcherThread(final File file) {
            this.file = file;
        }
        
        public void run() {
            final WatchServiceFileWatcher watcher = new WatchServiceFileWatcher();
            watcher.addFileListener(new FileListener() {
                @Override
                public void fileChanged(FileUpdateEvent event) {
                    synchronized (this) {
                        updated = true;
                        watcher.stop();
                    }
                }
            });
            watcher.watchFile(file.toPath());            
        }
        
        public synchronized boolean updated() {
            return updated;
        }
    }
}
