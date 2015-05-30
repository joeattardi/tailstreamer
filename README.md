TailStreamer
============

TailStreamer is a browser-based log viewer. It's `tail -f` for the web. Built on top of the [Spring Framework](https://github.com/spring-projects/spring-framework), it uses [SockJS](https://github.com/sockjs/sockjs-client) to stream log updates in real-time to your browser.

# Requirements
To run TailStreamer, you need:
* a Java Runtime Environment - Java 7 or later.

To build TailStreamer, you need:
* a Java Development Kit - Java 7 or later.
* Node.js.

# Building
1. Download and install Node.js
2. Go to your TailStreamer directory
3. Run `npm install` to download the necessary JavaScript components
4. Start the build by running `./gradlew installApp`.

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
TailStreamer is configured using YAML. Upon startup, it will look for a file called `tailstreamer.yml` and read
configuration properties from there.

For a full reference of available configuration options, see the wiki page at https://github.com/joeattardi/tailstreamer/wiki/YAML-Configuration-Reference.

# Security
By default, TailStreamer is accessible by anyone. You can restrict access by requiring a username and password. 
Authentication is configured in `tailstreamer.yml`. For security purposes, user passwords are stored hashed. 

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
