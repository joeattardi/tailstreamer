'use strict';

var $ = require('jquery');
var socket = require('../socket');
var noty = require('noty');

var $connectionStatus;
var $reconnectLink;
var $icon;

/**
 * Updates the connection state indicator.
 * @param state the connection state
 */
function setConnectionState(state) {
    $icon.removeClass();

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
            $icon.addClass('fa fa-lg fa-exclamation-triangle')
                .qtip({
                    content: 'Disconnected',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: $icon
                    }
                });
            $reconnectLink.show();
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
            $icon.addClass('fa fa-lg fa-exclamation-triangle')
                .qtip({
                    content: 'Connection failed',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: $icon
                    }
                });
            $reconnectLink.show();
            break;
        case socket.ConnectionState.CONNECTING:
            $icon.addClass('fa fa-lg fa-spin fa-refresh')
                .qtip({
                    content: 'Connecting',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: $icon
                    }
                });
            $reconnectLink.hide();
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
            $icon.addClass('fa fa-lg fa-check-circle')
                .qtip({
                    content: 'Connected',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: $icon
                    }
                });

            $reconnectLink.hide();
            break;
    }
}

$(document).ready(function() {
    $connectionStatus = $('#connectionStatus');
    $icon = $('#connectionStatus i');
    $reconnectLink = $('#reconnectLink');

    socket.onConnectionStateChange(setConnectionState);
});