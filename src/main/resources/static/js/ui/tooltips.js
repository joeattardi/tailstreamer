/**
 * Sets all tooltips in the application.
 */
'use strict';

var $ = require('jquery');
var is = require('is_js');
require('qtip2');

var $clearButton;
var $filterButton;
var $highlightButton;
var $searchText;
var $jumpToBottomButton;
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
        content: $('#filename').data('filepath'),
        position: {
            my: 'right center',
            at: 'left center',
            target: $filenameContainer
        }
    });

    var $clock = $('#elapsed');
    $clock.parent().qtip({
        content: 'Elapsed time',
        position: {
            my: 'right center',
            at: 'left center',
            target: $clock.parent()
        }
    });

    var $counter = $('#lineCount');
    $counter.parent().qtip({
        content: 'Lines received',
        position: {
            my: 'right center',
            at: 'left center',
            target: $counter.parent()
        }
    });
}

$(document).ready(function() {
    $clearButton = $('#clearButton');
    $filterButton = $('#filterButton');
    $highlightButton = $('#highlightButton');
    $jumpToBottomButton = $('#jumpToBottomButton');
    $searchText = $('#searchText');
    $filenameContainer = $('#filenameContainer');

    initTooltips();
});