/**
 * UI module
 */
import $ from 'jquery';
import { connect } from '../socket';

import './clock';
import './lineCounter';
import './unreadLineCount';
import './connectionStatusIndicator';
import './tooltips';
import './hotkeys';
import './messageHandler';
import './search';

var $clearButton;
var $filterButton;
var $highlightButton;
var $jumpToBottomButton;
var $logContent;

function initButtons() {
    $jumpToBottomButton.click(function(e) {
        $logContent.scrollTop($logContent[0].scrollHeight);
    });

    $logContent.on('scroll', function(e) {
        if ($logContent.scrollTop() + $logContent.innerHeight() !== $logContent[0].scrollHeight) {
            $jumpToBottomButton.fadeIn();
        } else {
            $jumpToBottomButton.fadeOut();
        }
    });

    $('#reconnectButton').hide().click(retryConnection);
}

/**
 * Recalculates the proper size of the log content area.
 */
function sizeLogContentArea() {
    $logContent.height($(window).height() - 112);
}

/**
 * Tries to connect.
 */
function retryConnection() {
    connect();
}

function bindEventListeners() {
    $(window).resize(sizeLogContentArea);
    $clearButton.click(clearLog);
}

function clearLog(e) {
    e.preventDefault();
    $logContent.empty();
}

$(document).ready(function() {
    $clearButton = $('#clearButton');
    $filterButton = $('#filterButton');
    $highlightButton = $('#highlightButton');
    $logContent = $('#logContent');
    $jumpToBottomButton = $('#jumpToBottomButton');

    sizeLogContentArea();
    initButtons();
    bindEventListeners();
});
