$(function() {
	connect(true);
	sizeLogContentArea();
	$(window).resize(sizeLogContentArea);
});

/**
 * Recalculates the proper size of the log content area.
 */
function sizeLogContentArea() {
	$("#logContent").height($(window).height() - 85);
}

/**
 * Connects to the WebSocket endpoint and subscribes to log update
 * messages.
 */
function connect(reconnect) {
	var socket = new SockJS("/tail");
	var stompClient = Stomp.over(socket);	
	
	setConnectionState(SockJS.CONNECTING);
	stompClient.connect({}, function(frame) {
		setConnectionState(SockJS.CONNECTED);
		stompClient.subscribe("/topic/log", updateLog);
	});	
	
	// stomp.js has to perform cleanup on close, but we need to listen too
	var stompCleanup = socket.onclose;
	socket.onclose = function() {
		setConnectionState(SockJS.CLOSED);
		stompCleanup();
		if (reconnect) {
			retryConnection();
		}
	}
}

/**
 * Tries to connect. If the connection attempt fails, it will not attempt a reconnect.
 */
function retryConnection() {
	connect(false);
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
		case SockJS.CLOSED:
			indicator.addClass("disconnected");			
			connectionLabel.html("Disconnected");
			break;
		case SockJS.CONNECTING:
			indicator.addClass("connecting");
			connectionLabel.html("Connecting");
			break;
		case SockJS.CONNECTED:
			indicator.addClass("connected");
			connectionLabel.html("Connected");
			break;
	}
}

/**
 * Handles a new log entry.
 * @param content The message received over the socket
 */
function updateLog(content) {
	var contentDiv = document.createElement("div");
	$(contentDiv).html(content.body);
	$("#logContent").append(contentDiv);
	window.scrollTo(0, document.body.scrollHeight);
}
