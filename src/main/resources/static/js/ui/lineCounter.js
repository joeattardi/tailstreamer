/**
 * Shows a running total of the number of log messages received.
 */
import $ from 'jquery';
import { onLogMessage } from '../socket';

var $counter;

$(document).ready(function () {
    $counter = $('#lineCount');
    onLogMessage(incrementCounter);
});

function incrementCounter() {
    $counter.html(parseInt($counter.html()) + 1);
}