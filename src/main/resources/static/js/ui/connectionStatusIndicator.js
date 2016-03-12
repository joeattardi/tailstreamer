/**
 * Displays the connection status in the main window.
 */
import $ from 'jquery';
import noty from 'noty';
import { ConnectionState, onConnectionStateChange } from '../socket';

var $notificationContainer;
var $connectionStatus;
var $reconnectButton;
var $icon;

function setDefaults() {
    $.noty.defaults.theme = 'relax';
    $.noty.defaults.layout = 'topRight';
    $.noty.defaults.animation = {
        open: { opacity: 'toggle' },
        close: { opacity: 'toggle' }
    };
}

/**
 * Updates the connection state indicator.
 * @param state the connection state
 */
function setConnectionState(state) {
    $icon.removeClass();

    switch (state) {
        case ConnectionState.DISCONNECTED:
            $notificationContainer.noty({
                text: 'Disconnected from server',
                timeout: 2000,
                type: 'warning'
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
            $reconnectButton.show();
            break;
        case ConnectionState.FAILED:
            $notificationContainer.noty({
                text: 'Failed to connect to the server',
                timeout: 2000,
                type: 'error'
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
            $reconnectButton.show();
            break;
        case ConnectionState.CONNECTING:
            $icon.addClass('fa fa-lg fa-spin fa-refresh')
                .qtip({
                    content: 'Connecting',
                    position: {
                        my: 'right center',
                        at: 'left center',
                        target: $icon
                    }
                });
            $reconnectButton.hide();
            break;
        case ConnectionState.CONNECTED:
            $notificationContainer.noty({
                text: 'Connected to server',
                timeout: 1000,
                type: 'success'
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

            $reconnectButton.hide();
            break;
    }
}

$(document).ready(function() {
    $notificationContainer = $('#notificationContainer');
    $connectionStatus = $('#connectionStatus');
    $icon = $('#connectionStatusIcon');
    $reconnectButton = $('#reconnectButton');

    setDefaults();
    onConnectionStateChange(setConnectionState);
});