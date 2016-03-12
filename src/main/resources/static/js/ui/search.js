/**
 * Handles search functionality.
 */
import $ from 'jquery';
import debounce from 'debounce';
import 'jquery-highlight';

var $logContent;
var $searchText;

/**
 * Updates the search results displayed in the log area.
 */
function updateSearch() {
    var searchText = $searchText.val();
    var $parent = $logContent.parent();

    $logContent.detach();

    $logContent.find(`:not(:contains(${searchText}))`).hide();
    $logContent.find(`:contains(${searchText})`).show();
    $logContent.unhighlight();
    $logContent.highlight(searchText);

    $parent.append($logContent);
    $logContent.scrollTop($logContent[0].scrollHeight);
}

$(document).ready(function() {
    $searchText = $('#searchText');
    $logContent = $('#logContent');

    $searchText.on('keyup click search', debounce(updateSearch, 250));
});