/**
 * A clock that keeps track of the elapsed time connected
 * to the server.
 */
import $ from 'jquery';
import { ConnectionState, onConnectionStateChange } from '../socket';

var startTime = new Date().getTime();
var interval;
var $clock;

$(document).ready(initClock);

function initClock() {
    onConnectionStateChange(setConnectionState);
    $clock = $('#elapsed');
}

function updateClock() {
    var elapsed = new Date().getTime() - startTime;

    var seconds = Math.floor(elapsed / 1000);
    var minutes = Math.floor(seconds / 60);
    var hours = Math.floor(minutes / 60);
    minutes = minutes % 60;
    seconds = seconds % 60;

    $clock.html((hours > 0 ? zeroPad(hours) + ':' : '') + zeroPad(minutes) + ':' + zeroPad(seconds));
}

function zeroPad(value) {
    return value < 10 ? '0' + value : value;
}

function setConnectionState(state) {
    switch (state) {
        case ConnectionState.DISCONNECTED:
        case ConnectionState.FAILED:
            clearInterval(interval);
            break;
        case ConnectionState.CONNECTED:
            startTime = new Date().getTime();
            interval = window.setInterval(updateClock, 1000);
            break;
    }
}