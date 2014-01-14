package net.thinksincode.tailstreamer;

import org.springframework.context.ApplicationEvent;

public class FileUpdateEvent extends ApplicationEvent {
    
    public FileUpdateEvent(final Object source) {
        super(source);
    }
}
