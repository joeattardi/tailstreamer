/**
 * Handles messages received from the WebSocket server.
 */
'use strict';

var $ = require('jquery');
var socket = require('../socket');

/** The maximum number of log entries to display before removing old entries. */
var MAX_LINES = 5000;

var $searchText;
var $logContent;

function addLogMessage(message) {
    var searchText = $searchText.val();

    // If we're scrolled down all the way, then automatically scroll to the bottom after appending
    // the new log entry. If not, that means the user scrolled up, so in that case we won't autoscroll.
    var autoscroll = ($logContent.scrollTop() + $logContent.innerHeight()) === $logContent[0].scrollHeight;

    var lines = $logContent.children();
    if (lines.length > MAX_LINES) {
        var diff = lines.length - MAX_LINES;
        lines.slice(0, diff).remove();
    }

    var $contentDiv = $('<div></div>').html(message);

    if (searchText.length > 0) {
        // If this doesn't match the search text, hide it
        if (message.toUpperCase().indexOf(searchText.toUpperCase()) < 0) {
            $contentDiv.hide();
        } else {
            $contentDiv.highlight(searchText);
        }
    }

    $logContent.append($contentDiv);

    if (autoscroll) {
        $logContent.scrollTop($logContent[0].scrollHeight);
    }
}

$(document).ready(function() {
    $logContent = $('#logContent');
    $searchText = $('#searchText');

    socket.onLogMessage(addLogMessage);
});