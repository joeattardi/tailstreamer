/**
 * WebSocket communications layer module
 */
'use strict';

var SockJS = require('sockjs-client');
var Stomp = require('stompjs');
var ee = require('event-emitter');

var EVENT_CONNECTION_STATE_CHANGE = 'connectionstatechange';
var EVENT_LOG_MESSAGE_RECEIVE = 'logmessagereceive';

var SOCKJS_ENDPOINT = '/tail';
var STOMP_TOPIC = '/topic/log';

var ConnectionState = {
    DISCONNECTED: 0,
    FAILED: 1,
    CONNECTING: 2,
    CONNECTED: 3
};

/** The currently active SockJS socket. */
var socket;

/** The currently active STOMP client. */
var stompClient;

/** The current connection state. */
var currentState = ConnectionState.DISCONNECTED;

var emitter = ee({});

/**
 * Initiates the STOMP over SockJS connection.
 */
function connect() {
    socket = new SockJS(SOCKJS_ENDPOINT);
    stompClient = Stomp.over(socket);
    stompClient.debug = false;

    emitter.emit(EVENT_CONNECTION_STATE_CHANGE, ConnectionState.CONNECTING);
    stompClient.connect({}, function(frame) {
        emitter.emit(EVENT_CONNECTION_STATE_CHANGE, ConnectionState.CONNECTED);
        stompClient.subscribe(STOMP_TOPIC, onMessage);
    });

    // stomp.js has to perform cleanup on close, but we need to listen too
    var onclose = socket.onclose;
    socket.onclose = function() {
        closeConnection(onclose);
    };
}

/**
 * Handles an incoming STOMP message.
 * @param content the message content as a string
 */
function onMessage(content) {
    JSON.parse(content.body).forEach(function(message) {
        emitter.emit(EVENT_LOG_MESSAGE_RECEIVE, message);
    });
}

/**
 * Updates the connection status, then calls the SockJS cleanup code.
 * @param stompCleanup the original socket.onclose function.
 */
function closeConnection(onclose) {
    if (currentState === ConnectionState.CONNECTING) {
        emitter.emit(EVENT_CONNECTION_STATE_CHANGE, ConnectionState.FAILED);
    } else {
        emitter.emit(EVENT_CONNECTION_STATE_CHANGE, ConnectionState.DISCONNECTED);
    }
    onclose();
}

/**
 * Adds a listener for the EVENT_CONNECTION_STATE_CHANGE event.
 * @param listener the listener function
 */
function onConnectionStateChange(listener) {
    emitter.on(EVENT_CONNECTION_STATE_CHANGE, listener);
}

/**
 * Adds a listener for the EVENT_LOG_MESSAGE_RECEIVE event.
 * @param listener the listener function
 */
function onLogMessage(listener) {
    emitter.on(EVENT_LOG_MESSAGE_RECEIVE, listener);
}

exports.connect = connect;
exports.onConnectionStateChange = onConnectionStateChange;
exports.onLogMessage = onLogMessage;
exports.ConnectionState = ConnectionState;