/**
 * Entry point of the application
 */
const $ = require('jquery');
window.jQuery = $;

require('./ui/ui');
const socket = require('./socket');

const jQuery = $;

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

