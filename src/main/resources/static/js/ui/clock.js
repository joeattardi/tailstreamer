'use strict';

var $ = require('jquery');
var socket = require('../socket');

var startTime = new Date().getTime();
var interval;
var $clock;

$(document).ready(initClock);

function initClock() {
    socket.onConnectionStateChange(setConnectionState);
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
        case socket.ConnectionState.DISCONNECTED:
        case socket.ConnectionState.FAILED:
            clearInterval(interval);
            break;
        case socket.ConnectionState.CONNECTED:
            startTime = new Date().getTime();
            interval = window.setInterval(updateClock, 1000);
            break;
    }
}