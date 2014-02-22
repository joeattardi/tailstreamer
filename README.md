TailStreamer
============

[![Build Status](https://travis-ci.org/joeattardi/tailstreamer.png?branch=master)](https://travis-ci.org/joeattardi/tailstreamer)

TailStreamer is a browser-based log viewer. It's `tail -f` for the web. Built on top of the [Spring Framework](https://github.com/spring-projects/spring-framework), it uses [SockJS](https://github.com/sockjs/sockjs-client) to stream log updates in real-time to your browser.

# Building
All you need to build TailStreamer is a Java Development Kit (JDK). The included Gradle wrapper will download Gradle if you don't already have it. Simply run:

    ./gradlew installApp
    
This will download Gradle, then build TailStreamer. The binaries will be under `build/install/tailstreamer/bin`.

# Usage
By default, TailStreamer runs on port 8080:

    tailstreamer /var/log/httpd-access.log
    
You can specify an alternate port with the `--server.port` argument:

    tailstreamer --server.port=8000 /var/log/httpd-access.log
    

