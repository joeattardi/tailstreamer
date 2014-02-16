ConnectionState = {
    DISCONNECTED: 0,
    FAILED: 1,
    CONNECTING: 2,
    CONNECTED: 3
} 

NOTIFICATION_DURATION = 200;

currentState = ConnectionState.DISCONNECTED;

$(function() {
    connect(true);
    sizeLogContentArea();

    fixContains();
    $(window).resize(sizeLogContentArea);

    $("#clearButton").click(clearLog);
    $("#searchText").on("keyup click", updateSearch);
    $("#reconnectLink").hide().click(retryConnection);
});

/**
 * Changes the jQuery :contains selector so that it's
 * case-insensitive.
 */
function fixContains() {
    jQuery.expr[':'].contains = function(a, i, m) {
          return jQuery(a).text().toUpperCase()
              .indexOf(m[3].toUpperCase()) >= 0;
        };  
}

function updateSearch() {
    var searchText = $("#searchText").val();
    $("#logContent :not(:contains(" + searchText + "))").hide();
    $("#logContent :contains(" + searchText + ")").show();
    
    var logContent = $("#logContent");
    logContent.removeHighlight();
    logContent.highlight(searchText);
    logContent.scrollTop(logContent[0].scrollHeight);
}

/**
 * Recalculates the proper size of the log content area.
 */
function sizeLogContentArea() {
    $("#logContent").height($(window).height() - 115);
}

/**
 * Connects to the WebSocket endpoint and subscribes to log update
 * messages.
 */
function connect() {
    var socket = new SockJS("/tail");
    var stompClient = Stomp.over(socket);   
    
    stompClient.debug = false;
    
    setConnectionState(ConnectionState.CONNECTING);
    stompClient.connect({}, function(frame) {
        setConnectionState(ConnectionState.CONNECTED);
        stompClient.subscribe("/topic/log", updateLog);
    }); 
    
    // stomp.js has to perform cleanup on close, but we need to listen too
    var stompCleanup = socket.onclose;
    socket.onclose = function() {
        if (currentState === ConnectionState.CONNECTING) {
//            showConnectionError('Failed to connect! <button id="reconnect">Reconnect</button>');
//            $("#reconnect").click(retryConnection);
            setConnectionState(ConnectionState.FAILED);
        } else {
//            showConnectionError('Lost connection! <button id="reconnect">Reconnect</button>');
//            $("#reconnect").click(retryConnection);
            setConnectionState(ConnectionState.DISCONNECTED);
        }
        stompCleanup();
    }
}


/**
 * Tries to connect. 
 */
function retryConnection() {
    hideConnectionError();
    connect();
}

/**
 * Updates the connection state indicator.
 * @param state the connection state
 */
function setConnectionState(state) {
    var connectionStatus = $("#connectionStatus");
    var icon = $("#connectionStatus i");
    var message = $("#connectionMessage");    
    var reconnectLink = $("#reconnectLink");
    icon.removeClass();
    
    switch (state) {
        case ConnectionState.DISCONNECTED:
            icon.addClass("fa fa-exclamation-triangle");
            message.html("Disconnected");
            reconnectLink.show();
            break;
        case ConnectionState.FAILED:
            icon.addClass("fa fa-exclamation-triangle");
            message.html("Failed to connect");
            reconnectLink.show();
            break;
        case ConnectionState.CONNECTING:
            icon.addClass("fa fa-refresh");
            message.html("Connecting");
            reconnectLink.hide();
            break;
        case ConnectionState.CONNECTED:
            icon.addClass("fa fa-check-circle");
            message.html("Connected");
            reconnectLink.hide();
            break;                       
    }
    
    currentState = state;
}

/**
 * Shows a message in the connection error notification box.
 * @param message The message to show
 */
function showConnectionError(message) { 
    var messageBox = $("#connectionMessage");
    messageBox.html(message);
    messageBox.animate({top: 0}, NOTIFICATION_DURATION);    
}

/**
 * Hides the connection error box
 */
function hideConnectionError() {
    var messageBox = $("#connectionMessage");
    messageBox.animate({top: -messageBox.outerHeight()}, NOTIFICATION_DURATION);
}

/**
 * Handles a new log entry.
 * @param content The message received over the socket
 */
function updateLog(content) {
    var logContent = $("#logContent");

    var messages = JSON.parse(content.body);
    
    // If we're scrolled down all the way, then automatically scroll to the bottom after appending
    // the new log entry. If not, that means the user scrolled up, so in that case we won't autoscroll.
    var autoscroll = logContent.scrollTop() + logContent.innerHeight() == logContent[0].scrollHeight;
    
    for (var i = 0; i < messages.length; i++) {
        addLogMessage(messages[i]);
    }

    if (autoscroll) {
        logContent.scrollTop(logContent[0].scrollHeight);
    }
    
    flashIndicator();
}

function addLogMessage(message) {
    var searchText = $("#searchText").val();

    var logContent = $("#logContent");
    var contentDiv = $(document.createElement("div"));
    contentDiv.html(message);
    
    if (searchText.length > 0) {
        // If this doesn't match the search text, hide it
        if (message.toUpperCase().indexOf(searchText.toUpperCase()) < 0) {
            contentDiv.hide();
        } else {
            contentDiv.highlight(searchText);
        }
    }
        
    logContent.append(contentDiv);
}

function clearLog() {
    $("#logContent").empty();
}

/**
 * Flashes the connection indicator to indicate data
 * was received.
 */
function flashIndicator() {
    var indicator = $("#indicator");
    indicator.fadeOut(100, function() {
        indicator.fadeIn(100);
    });
}
