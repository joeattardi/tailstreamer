package com.thinksincode.tailstreamer;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.Assertion;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.StandardErrorStreamLog;
import org.junit.contrib.java.lang.system.StandardOutputStreamLog;

public class TailStreamerTests {
    
    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();
    
    @Rule
    public final StandardErrorStreamLog stderr = new StandardErrorStreamLog();
    
    @Rule
    public final StandardOutputStreamLog stdout = new StandardOutputStreamLog();
    
    @Test
    public void testNoArgumentsExit() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(new Assertion() {
            @Override
            public void checkAssertion() throws Exception {
                Assert.assertEquals(ArgumentProcessor.MESSAGE_NO_FILE_SPECIFIED, stderr.getLog().trim());
            }
        });
        TailStreamer.main();
    }
    
    @Test
    public void testNonExistentFileExit() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(new Assertion() {
            @Override
            public void checkAssertion() throws Exception {
                Assert.assertEquals(String.format(ArgumentProcessor.MESSAGE_FILE_NOT_FOUND, "some_file.txt"), 
                        stderr.getLog().trim());
            }
        });
        TailStreamer.main("some_file.txt");  
    }
    
    @Test
    public void testMissingFileArgumentExit() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(new Assertion() {
            @Override
            public void checkAssertion() throws Exception {
                Assert.assertEquals(ArgumentProcessor.MESSAGE_NO_FILE_SPECIFIED, stderr.getLog().trim());
            }
        });
        TailStreamer.main("--server.port=8000");        
    }
    
    @Test
    public void testInvalidArgumentExit() {
        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(new Assertion() {
            @Override
            public void checkAssertion() throws Exception {
                Assert.assertEquals(String.format(ArgumentProcessor.MESSAGE_INVALID_OPTION, "z"), stderr.getLog().trim());
            }
        });
        TailStreamer.main("-z");   
    }
    
    @Test
    public void testHelpText() {
        TailStreamer.main("-h");
        Assert.assertEquals(TailStreamer.getHelpText(), stdout.getLog().trim());
    }
}
