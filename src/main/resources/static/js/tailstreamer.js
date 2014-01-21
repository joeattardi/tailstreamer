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

function retryConnection() {
	connect(false);
}

function setConnectionState(state) {
	var connectionStatus = $("#connectionStatus");
	
	switch (state) {
		case SockJS.CLOSED:
			connectionStatus.html('<i class="disconnected fa fa-exclamation-triangle"></i> Disconnected');
			break;
		case SockJS.CONNECTING: 
			connectionStatus.html('<img style="vertical-align: middle; margin-bottom: 2px;" src="connecting.gif"> Connecting');
			break;
		case SockJS.CONNECTED:
			connectionStatus.html('<i class="connected fa fa-check-square"></i> Connected');
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
