var socket = new SockJS("/tail");
var stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
	console.log("Connected", frame);
	stompClient.subscribe("/topic/log", updateLog);
});

function updateLog(content) {
	var contentDiv = document.createElement("div");
	contentDiv.innerHTML = content.body;
	
	console.log(content);
	
	document.getElementById("logContent").appendChild(contentDiv);
}