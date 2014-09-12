package com.tugasakhir.droidduino.connection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ConnectionMessageHandler extends Handler {

	private ConnectionListener connListener;
	
	public ConnectionMessageHandler(ConnectionListener connListener) {
		this.connListener = connListener;
	}
	
	@Override
	public void handleMessage(Message msg) {
		if (connListener != null) {
			Bundle data = msg.getData();
			
			if (data.containsKey(PayloadKey.CONNECTION_STATUS)) {
				boolean connected = data.getBoolean(PayloadKey.CONNECTION_STATUS);
				connListener.onConnectionStatus(connected);
			}
			else if (data.containsKey(PayloadKey.SEND_MESSAGE)) {
				boolean sent = data.getBoolean(PayloadKey.SEND_MESSAGE);
				connListener.onMessageSent(sent);
			}
			else if (data.containsKey(PayloadKey.RECEIVE_MESSAGE)) {
				String message = data.getString(PayloadKey.RECEIVE_MESSAGE);
				connListener.onMessageReceived(message);
			}
		}
	}
	
	public static Message MessageBuilder(String message, String payloadKey) {
		Bundle b = new Bundle();
		b.putString(payloadKey, message);
		Message msg = new Message();
		msg.setData(b);
		return msg;
	}
	
	public static Message MessageBuilder(boolean value, String payloadKey) {
		Bundle b = new Bundle();
		b.putBoolean(payloadKey, value);
		Message msg = new Message();
		msg.setData(b);
		return msg;
	}
	
	public interface ConnectionListener {
		void onConnectionStatus(boolean connected);
		void onMessageSent(boolean sent);
		void onMessageReceived(String message);
	}
}
