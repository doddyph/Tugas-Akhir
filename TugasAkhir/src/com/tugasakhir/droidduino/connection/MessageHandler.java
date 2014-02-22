package com.tugasakhir.droidduino.connection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MessageHandler extends Handler {

	private MessageListener messageListener;
	
	public MessageHandler(MessageListener messageListener) {
		this.messageListener = messageListener;
	}
	
	@Override
	public void handleMessage(Message msg) {
		
		if (messageListener != null) {
			Bundle data = msg.getData();
			
			if (data.containsKey(PayloadKey.CONNECTION_STATUS)) {
				boolean connected = data.getBoolean(PayloadKey.CONNECTION_STATUS);
				messageListener.onConnectionStatus(connected);
			}
			else if (data.containsKey(PayloadKey.SEND_MESSAGE)) {
				boolean sent = data.getBoolean(PayloadKey.SEND_MESSAGE);
				messageListener.onMessageSent(sent);
			}
			else if (data.containsKey(PayloadKey.RECEIVE_MESSAGE)) {
				String message = data.getString(PayloadKey.RECEIVE_MESSAGE);
				messageListener.onMessageReceived(message);
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
	
	public interface MessageListener {
		void onConnectionStatus(boolean connected);
		void onMessageSent(boolean sent);
		void onMessageReceived(String message);
	}
}
