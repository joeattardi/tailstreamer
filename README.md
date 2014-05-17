TailStreamer
============

[![Build Status](https://travis-ci.org/joeattardi/tailstreamer.png?branch=master)](https://travis-ci.org/joeattardi/tailstreamer)

TailStreamer is a browser-based log viewer. It's `tail -f` for the web. Built on top of the [Spring Framework](https://github.com/spring-projects/spring-framework), it uses [SockJS](https://github.com/sockjs/sockjs-client) to stream log updates in real-time to your browser.

# Requirements
TailStreamer requires Java 7 or later.

# Building
All you need to build TailStreamer is a Java Development Kit (JDK). The included Gradle wrapper will download Gradle if you don't already have it. Simply run:

    ./gradlew installApp
    
This will download Gradle, then build TailStreamer. The binaries will be under `build/install/tailstreamer/bin`.

# Usage
By default, TailStreamer runs on port 8080:

    tailstreamer /var/log/httpd-access.log
    
You can specify an alternate port with the `--server.port` argument:

    tailstreamer --server.port=8000 /var/log/httpd-access.log

You can also specify the server port in `application.yml`:

    server:
        port: 9000

# Configuration
TailStreamer is configured using YAML. Upon startup, it will look for a file called `application.yml` and read
configuration properties from there.

# Security
By default, TailStreamer is accessible by anyone. You can restrict access by requiring a username and password. 
Authentication is configured in `application.yml`. For security purposes, user passwords are stored hashed. 

The first step is getting the hashed password. Run TailStreamer with the `--encryptPassword` option to generate
the hashed password:

    tailstreamer --encryptPassword myPassword
    
The hashed password will be displayed:

Copy this password to your clipboard, then edit `application.yml` and add it under `auth`: `users`:

    auth:
        users:
            - username: joe
              password: $2a$10$mAlhv2g41/NObkGOSVLvO.ayoFdN7kqnV61Km8PSJ8qjcYnK5q2ke
        
Now, when starting TailStreamer, you will be prompted to log in before viewing the log.

# Screenshots
![Screenshot](https://raw.github.com/joeattardi/tailstreamer/gh-pages/screenshot.png)
![Screenshot](https://raw.github.com/joeattardi/tailstreamer/gh-pages/screenshot_search.png)
