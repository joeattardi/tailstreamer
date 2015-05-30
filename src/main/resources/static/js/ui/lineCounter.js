/**
 * Shows a running total of the number of log messages received.
 */
'use strict';

var $ = require('jquery');
var socket = require('../socket');

var $counter;

$(document).ready(function () {
    $counter = $('#lineCount');
    socket.onLogMessage(incrementCounter);
});

function incrementCounter() {
    $counter.html(parseInt($counter.html()) + 1);
}