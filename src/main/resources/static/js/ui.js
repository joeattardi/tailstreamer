/**
 * UI module
 */
'use strict';

var $ = require('jquery');
var hotkeys = require('hotkeys');
require('qtip2');
var is = require('is_js');
var debounce = require('debounce');
var noty = require('noty');

var socket = require('./socket');
require('./clock');

/** The maximum number of log entries to display before removing old entries. */
var MAX_LINES = 5000;

/** The hotkey dispatcher */
var dispatcher;

var $clearButton;
var $filterButton;
var $highlightButton;
var $jumpToBottomButton;
var $logContent;
var $searchText;
var $reconnectLink;
var $filenameContainer;

function initTooltips() {
    $.fn.qtip.defaults.style.classes = 'qtip-light';

    var hotkeyModifier = is.mac() ? '&#8984;' : 'Alt';

    $clearButton.qtip({content: 'Clear contents <span class="shortcut">' + hotkeyModifier + '+X</span>'});
    $filterButton.qtip({content: 'Configure filters'});
    $highlightButton.qtip({content: 'Configure highlighting'});

    $searchText.qtip({
        content: 'Search <span class="shortcut">' + hotkeyModifier + '+S</span>',
        position: {
            my: 'top center',
            at: 'bottom center',
            target: $searchText
        }
    });

    $jumpToBottomButton.qtip({
        content: 'Jump to bottom',
        position: {
            my: 'right center',
            at: 'left center',
            target: $jumpToBottomButton
        }
    });

    $filenameContainer.qtip({
        content: 'File name',
        position: {
            my: 'right center',
            at: 'left center',
            target: $filenameContainer
        }
    });

}

function initButtons() {
    $jumpToBottomButton.click(function(e) {
        $logContent.scrollTop($logContent[0].scrollHeight);
    });

    $logContent.on('scroll', function(e) {
        if ($logContent.scrollTop() + $logContent.innerHeight() !== $logContent[0].scrollHeight) {
            $jumpToBottomButton.fadeIn();
        } else {
            $jumpToBottomButton.fadeOut();
        }
    });
}

/**
 * Recalculates the proper size of the log content area.
 */
function sizeLogContentArea() {
    $logContent.height($(window).height() - 112);
}

/**
 * Tries to connect.
 */
function retryConnection() {
    socket.connect();
}

function bindEventListeners() {
    $(window).resize(sizeLogContentArea);
    $clearButton.click(clearLog);
    $searchText.on('keyup click search', debounce(updateSearch, 250));
    $reconnectLink.hide().click(retryConnection);
}

function bindHotkeys() {
    dispatcher = new hotkeys.Dispatcher();
    var hotkeyModifier = is.mac() ? 'cmd' : 'alt';

    dispatcher.on(hotkeyModifier + ' s', function() {
        $searchText.focus();
    });

    dispatcher.on(hotkeyModifier + ' x', function() {
        $clearButton.click();
    });
}

/**
 * Updates the search results displayed in the log area.
 */
function updateSearch() {
    var searchText = $searchText.val();
    var $parent = $logContent.parent();

    $logContent.detach();

    $logContent.find(':not(:contains(' + searchText + '))').hide();
    $logContent.find(':contains(' + searchText + ')').show();
    $logContent.unhighlight();
    $logContent.highlight(searchText);

    $parent.append($logContent);
    $logContent.scrollTop($logContent[0].scrollHeight);
}

function clearLog() {
    $logContent.empty();
}

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

/**
 * Updates the connection state indicator.
 * @param state the connection state
 */
function setConnectionState(state) {
    var connectionStatus = $('#connectionStatus');
    var icon = $('#connectionStatus i');
    var reconnectLink = $('#reconnectLink');
    icon.removeClass();

    switch (state) {
        case socket.ConnectionState.DISCONNECTED:
            noty({
                text: 'Disconnected from server',
                timeout: 2000,
                theme: 'relax',
                type: 'warning',
                layout: 'topRight',
                animation: {
                    open: {opacity: 'toggle'},
                    close: {opacity: 'toggle'}
                }
            });
            icon.addClass('fa fa-lg fa-exclamation-triangle')
                .qtip({
                    content: 'Disconnected',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: icon
                    }
                });
            reconnectLink.show();
            break;
        case socket.ConnectionState.FAILED:
            noty({
                text: 'Failed to connect to the server',
                timeout: 2000,
                theme: 'relax',
                type: 'error',
                layout: 'topRight',
                animation: {
                    open: {opacity: 'toggle'},
                    close: {opacity: 'toggle'}
                }
            });
            icon.addClass('fa fa-lg fa-exclamation-triangle')
                .qtip({
                    content: 'Connection failed',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: icon
                    }
                });
            reconnectLink.show();
            break;
        case socket.ConnectionState.CONNECTING:
            icon.addClass('fa fa-lg fa-spin fa-refresh')
                .qtip({
                    content: 'Connecting',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: icon
                    }
                });
            reconnectLink.hide();
            break;
        case socket.ConnectionState.CONNECTED:
            noty({
                text: 'Connected to server',
                timeout: 1000,
                theme: 'relax',
                type: 'success',
                layout: 'topRight',
                animation: {
                    open: {opacity: 'toggle'},
                    close: {opacity: 'toggle'}
                }
            });
            icon.addClass('fa fa-lg fa-check-circle')
                .qtip({
                    content: 'Connected',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: icon
                    }
                });

            reconnectLink.hide();
            break;
    }
}

$(document).ready(function() {
    $clearButton = $('#clearButton');
    $filterButton = $('#filterButton');
    $highlightButton = $('#highlightButton');
    $logContent = $('#logContent');
    $jumpToBottomButton = $('#jumpToBottomButton');
    $searchText = $('#searchText');
    $reconnectLink = $('#reconnectLink');
    $filenameContainer = $('#filenameContainer');

    sizeLogContentArea();
    initButtons();
    initTooltips();
    bindEventListeners();
    bindHotkeys();

    socket.onConnectionStateChange(setConnectionState);
    socket.onLogMessage(addLogMessage);
});

exports.setConnectionState = setConnectionState;