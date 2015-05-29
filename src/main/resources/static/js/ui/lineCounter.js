'use strict';

var $ = require('jquery');
var socket = require('../socket');

var $counter;

$(document).ready(function () {
    $.fn.qtip.defaults.style.classes = 'qtip-light';
    $counter = $('#lineCount');
    $counter.parent().qtip({
        content: 'Lines received',
        position: {
            my: 'right center',
            at: 'left center',
            target: $counter.parent()
        }
    });

    socket.onLogMessage(incrementCounter);
});

function incrementCounter() {
    $counter.html(parseInt($counter.html()) + 1);
}