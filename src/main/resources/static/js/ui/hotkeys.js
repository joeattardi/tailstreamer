'use strict';

var $ = require('jquery');
var hotkeys = require('hotkeys');
var is = require('is_js');

/** The hotkey dispatcher */
var dispatcher;

var $searchText;
var $clearButton;

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

$(document).ready(function() {
    $searchText = $('#searchText');
    $clearButton = $('#clearButton');
    bindHotkeys();
});