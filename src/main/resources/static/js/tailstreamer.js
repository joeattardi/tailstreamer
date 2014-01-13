var socket = new SockJS("/tail");
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
	console.log("Connected", frame);
	stompClient.subscribe("/topic/log", function(content) {
		console.log(content.body);
	});
});