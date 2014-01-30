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
	$(window).resize(sizeLogContentArea);
});

/**
 * Recalculates the proper size of the log content area.
 */
function sizeLogContentArea() {
	$("#logContent").height($(window).height() - 90);
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
			showConnectionError('Failed to connect! <button id="reconnect">Reconnect</button>');
			$("#reconnect").click(retryConnection);
			setConnectionState(ConnectionState.FAILED);
		} else {
			showConnectionError('Lost connection! <button id="reconnect">Reconnect</button>');
			$("#reconnect").click(retryConnection);
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
	var indicator = $("#indicator");
	var connectionLabel = $("#connectionLabel");
	
	indicator.removeClass();
	
	switch (state) {
		case ConnectionState.DISCONNECTED:
		case ConnectionState.FAILED:
			indicator.addClass("disconnected");			
			connectionLabel.html("Disconnected");
			break;
		case ConnectionState.CONNECTING:
			indicator.addClass("connecting");
			connectionLabel.html("Connecting");
			break;
		case ConnectionState.CONNECTED:
			indicator.addClass("connected");
			connectionLabel.html("Connected");
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
	
	for (var i = 0; i < messages.length; i++) {
		var contentDiv = $(document.createElement("div"));	
		contentDiv.html(messages[i]);
		logContent.append(contentDiv.hide().fadeIn(200));
	}

	logContent[0].scrollTop = logContent[0].scrollHeight;
	
	flashIndicator();
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
