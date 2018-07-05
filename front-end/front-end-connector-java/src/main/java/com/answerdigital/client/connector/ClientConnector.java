package com.answerdigital.client.connector;

import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import com.answerdigital.pcaap.dto.Patient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ClientConnector extends WebSocketClient {
	ObjectMapper mapper = new ObjectMapper();

	public ClientConnector( URI serverUri , Draft draft ) {
		super( serverUri, draft );
	}

	public ClientConnector( URI serverURI ) {
		super( serverURI );
	}

	public ClientConnector( URI serverUri, Map<String, String> httpHeaders ) {
		super(serverUri, httpHeaders);
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		System.out.println( "opened connection" );
	}

	@Override
	public void onMessage( String message ) {
		System.out.println( "received: " + message );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}
	
	public void sendPatientContext( Patient patient ) {
		try {
			send("{\"event\":\"patient-context:changed\",\"data\":" + mapper.writeValueAsString(patient) + "}");
		} catch (NotYetConnectedException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPatientEnded() {
		send("{\"event\":\"patient-context:ended\"}");
	}

}