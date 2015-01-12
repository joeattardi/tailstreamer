var $ = require('jquery');
window.jQuery = $;

var SockJS = require('sockjs-client');
var Stomp = require('stompjs');

require('jquery-highlight');
require('qtip2');

$(function() {
    "use strict";

    var jQuery = $;

    var TailStreamer = (function($) {
        var NOTIFICATION_DURATION = 200;

        /** The maximum number of log entries to display before removing old entries. */
        var MAX_LINES = 5000;

        var ConnectionState = {
            DISCONNECTED: 0,
            FAILED: 1,
            CONNECTING: 2,
            CONNECTED: 3
        };

        var currentState = ConnectionState.DISCONNECTED;

        // Cached references to common DOM elements

        /** The element containing all log messages. */
        var logContent = $("#logContent");

        /** The search field. */
        var searchField = $("#searchText");

        /** The "clear contents" button. */
        var clearButton = $("#clearButton");

        /**
         * Changes the jQuery :contains selector so that it's
         * case-insensitive.
         */
        function fixContains() {
            $.expr[':'].contains = function(a, i, m) {
                return jQuery(a).text().toUpperCase()
                    .indexOf(m[3].toUpperCase()) >= 0;
            };
        }

        /**
         * Updates the search results displayed in the log area.
         */
        function updateSearch() {
            var searchText = $("#searchText").val();
            logContent.find(":not(:contains(" + searchText + "))").hide();
            logContent.find(":contains(" + searchText + ")").show();
            logContent.removeHighlight();
            logContent.highlight(searchText);
            logContent.scrollTop(logContent[0].scrollHeight);
        }

        /**
         * Recalculates the proper size of the log content area.
         */
        function sizeLogContentArea() {
            logContent.height($(window).height() - 112);
        }

        /**
         * Connects to the WebSocket endpoint and subscribes to log update
         * messages.
         */
        function connect() {
            var socket = new SockJS("/tail");
            var stompClient = Stomp.over(socket);

            stompClient.debug = false;

            setConnectionState(ConnectionState.CONNECTING);
            stompClient.connect({}, function(frame) {
                setConnectionState(ConnectionState.CONNECTED);
                stompClient.subscribe("/topic/log", updateLog);
            });

            // stomp.js has to perform cleanup on close, but we need to listen too
            var stompCleanup = socket.onclose;
            socket.onclose = function() {
                if (currentState === ConnectionState.CONNECTING) {
                    setConnectionState(ConnectionState.FAILED);
                } else {
                    setConnectionState(ConnectionState.DISCONNECTED);
                }
                stompCleanup();
            };
        }

        /**
         * Tries to connect.
         */
        function retryConnection() {
            hideConnectionError();
            connect();
        }

        /**
         * Updates the connection state indicator.
         * @param state the connection state
         */
        function setConnectionState(state) {
            var connectionStatus = $("#connectionStatus");
            var icon = $("#connectionStatus i");
            var message = $("#connectionMessage");
            var reconnectLink = $("#reconnectLink");
            icon.removeClass();

            switch (state) {
                case ConnectionState.DISCONNECTED:
                    icon.addClass("fa fa-exclamation-triangle");
                    message.html("Disconnected");
                    reconnectLink.show();
                    break;
                case ConnectionState.FAILED:
                    icon.addClass("fa fa-exclamation-triangle");
                    message.html("Failed to connect");
                    reconnectLink.show();
                    break;
                case ConnectionState.CONNECTING:
                    icon.addClass("fa fa-refresh");
                    message.html("Connecting");
                    reconnectLink.hide();
                    break;
                case ConnectionState.CONNECTED:
                    icon.addClass("fa fa-check-circle");
                    message.html("Connected");
                    reconnectLink.hide();
                    break;
            }

            currentState = state;
        }

        /**
         * Hides the connection error box
         */
        function hideConnectionError() {
            var messageBox = $("#connectionMessage");
            messageBox.animate({top: -messageBox.outerHeight()}, NOTIFICATION_DURATION);
        }

        /**
         * Handles a new log entry.
         * @param content The message received over the socket
         */
        function updateLog(content) {
            // If we're scrolled down all the way, then automatically scroll to the bottom after appending
            // the new log entry. If not, that means the user scrolled up, so in that case we won't autoscroll.
            var autoscroll = (logContent.scrollTop() + logContent.innerHeight()) === logContent[0].scrollHeight;

            JSON.parse(content.body).forEach(function(message) {
               addLogMessage(message);
            });

            if (autoscroll) {
                logContent.scrollTop(logContent[0].scrollHeight);
            }

            flashIndicator();
        }

        /**
         *
         * @param message
         */
        function addLogMessage(message) {
            var searchText = searchField.val();

            var lines = logContent.children();
            if (lines.length > MAX_LINES) {
                var diff = lines.length - MAX_LINES;
                lines.slice(0, diff).remove();
            }

            var contentDiv = $(document.createElement("div"));
            contentDiv.html(message);

            if (searchText.length > 0) {
                // If this doesn't match the search text, hide it
                if (message.toUpperCase().indexOf(searchText.toUpperCase()) < 0) {
                    contentDiv.hide();
                } else {
                    contentDiv.highlight(searchText);
                }
            }

            logContent.append(contentDiv);
        }

        function clearLog() {
            logContent.empty();
        }

        /**
         * Flashes the connection indicator to indicate data
         * was received.
         */
        function flashIndicator() {
            var indicator = $("#indicator");
            indicator.fadeOut(100, function() {
                indicator.fadeIn(100);
            });
        }

        function initTooltips() {
            $.fn.qtip.defaults.style.classes = "qtip-light";

            clearButton.qtip({content: "Clear contents <span class=\"shortcut\">Alt+C</span>"});
            $("#filterButton").qtip({content: "Configure filters"});
            $("#highlightButton").qtip({content: "Configure highlighting"});

            searchField.qtip({
                content: "Search <span class=\"shortcut\">Alt+S</span>",
                position: {
                    my: "top center",
                    at: "bottom center",
                    target: $("#searchText")
                }
            });
        }

        function initButtons() {
            var jumpToBottomButton = $("#jumpToBottomButton");

            jumpToBottomButton.click(function(e) {
                logContent.scrollTop(logContent[0].scrollHeight);
            });

            logContent.on("scroll", function(e) {
                if (logContent.scrollTop() + logContent.innerHeight() !== logContent[0].scrollHeight) {
                    jumpToBottomButton.fadeIn();
                } else {
                    jumpToBottomButton.fadeOut();
                }
            });
        }

        function bindEventListeners() {
            $(window).resize(sizeLogContentArea);
            clearButton.click(clearLog);
            searchField.on("keyup click", updateSearch);
            $("#reconnectLink").hide().click(retryConnection);
        }

        return {
            init: function() {
                initButtons();
                initTooltips();
                sizeLogContentArea();
                fixContains();
                bindEventListeners();
                connect(true);
            }
        };
    })(jQuery);

    TailStreamer.init();
});


