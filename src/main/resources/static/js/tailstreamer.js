/**
 * Entry point of the application
 */
"use strict";

var $ = require('jquery');
window.jQuery = $;

require('jquery-highlight');
require('qtip2');

require('./ui');
var socket = require('./socket');

var jQuery = $;

function init() {
    fixContains();
    socket.connect();
}

$(document).ready(init);

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

