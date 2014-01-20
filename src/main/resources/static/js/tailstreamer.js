$(function() {
	connect();
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
function connect() {
	var socket = new SockJS("/tail");
	var stompClient = Stomp.over(socket);	
	
	$("#connectionStatus").html("Connecting");
	stompClient.connect({}, function(frame) {
		$("#connectionStatus").html("Connected");
		stompClient.subscribe("/topic/log", updateLog);
	});	
	
	// stomp.js has to perform cleanup on close, but we need to listen too
	var stompCleanup = socket.onclose;
	socket.onclose = function() {
		$("#connectionStatus").html("Disconnected");
		stompCleanup();
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
