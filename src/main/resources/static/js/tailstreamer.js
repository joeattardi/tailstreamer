$(function() {
	connect();
});

/**
 * Connects to the WebSocket endpoint and subscribes to log update
 * messages.
 */
function connect() {
	var socket = new SockJS("/tail");
	var stompClient = Stomp.over(socket);	
	
	stompClient.connect({}, function(frame) {
		stompClient.subscribe("/topic/log", updateLog);
	});	
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
