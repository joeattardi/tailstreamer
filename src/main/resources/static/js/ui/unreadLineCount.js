/**
 * When the window is not focused, this module will count the number
 * of new, unread log messages and display the count in the page title.
 *
 * When the window regains focus, the count will be cleared.
 */
'use strict';

var $ = require('jquery');
var socket = require('../socket');

/** Whether or not the window has focus. */
var windowFocused = true;

/** The original window title, containing the file path. */
var originalTitle = document.title;

/** The number of new lines that came in since the window lost focus. */
var unreadLineCount = 0;

/**
 * Called when the window gains focus, clearing
 * the unread line count in the title.
 */
function handleFocus() {
    windowFocused = true;
    unreadLineCount = 0;
    document.title = originalTitle;
}

/**
 * Called when the window loses focus, starting
 * to count unread lines.
 */
function handleBlur() {
    windowFocused = false;
}

/**
 * Handles an incoming message, incrementing the unread
 * line count if the window doesn't have focus.
 * @param message the message that was received
 */
function onLogMessage(message) {
    if (!windowFocused) {
        document.title = '(' + (++unreadLineCount) + ') ' + originalTitle;
    }
}

$(document).ready(function() {
    socket.onLogMessage(onLogMessage);
    $(window)
        .on('focus', handleFocus)
        .on('blur', handleBlur);
});
