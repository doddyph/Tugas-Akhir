package com.tugasakhir.droidduino.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.tugasakhir.droidduino.Settings;
import com.tugasakhir.droidduino.connection.ConnectionMessageHandler.ConnectionListener;

import android.os.AsyncTask;
import android.util.Log;

public class ArduinoClient {

	private static final int PORT = 8888;
	private static final int TIMEOUT = 5*1000;// 5 seconds connection timeout
	private static final String TAG = "ArduinoClient";
	
	private Socket mSocket;
	private InputStream mIn;
	private OutputStream mOut;
	private BufferedReader mBuff; // to store the incoming bytes from server
	private boolean mRun = true;
	
	private ConnectionMessageHandler connMsgHandler;
	
	public ArduinoClient(ConnectionListener listener) {
		connMsgHandler = new ConnectionMessageHandler(listener);
	}
	
	public void connect() {
		new ConnectionTask().execute();
	}
	
	public void connectAndWait() {
		try {
			mSocket = new Socket();
			mSocket.connect(new InetSocketAddress(Settings.IP_ADDRESS, PORT), TIMEOUT);
			
			if (mSocket.isConnected()) {
				informConnectionStatus(true);
				
				mIn = mSocket.getInputStream();
				mOut = mSocket.getOutputStream();
				mBuff = new BufferedReader(new InputStreamReader(mIn));
				
				while (mRun) {
					String msgFromServer = mBuff.readLine();
					byte[] msgBytes = msgFromServer.getBytes();
					doIncomingMessageProcess(new String(msgBytes));
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "connect() "+e.getMessage());
		} finally {
			disconnect();
		}
	}
	
	private void doIncomingMessageProcess(String message) {
		if (connMsgHandler != null) {
			connMsgHandler.sendMessage(ConnectionMessageHandler.MessageBuilder(
					message,
					PayloadKey.RECEIVE_MESSAGE));
		}
	}
	
	private void informConnectionStatus(boolean connected) {
		if (connMsgHandler != null) {
			connMsgHandler.sendMessage(ConnectionMessageHandler.MessageBuilder(
					connected,
					PayloadKey.CONNECTION_STATUS));
		}
	}
	
	public void sendCommand(String message) {
		if (mSocket.isConnected()) {
			try {
				mOut.write(message.getBytes());
			} catch (IOException e) {
				Log.e(TAG, "sendCommand() "+e.getMessage());
				return;
			}
			
			if (connMsgHandler != null) {
				connMsgHandler.sendMessage(ConnectionMessageHandler.MessageBuilder(
						true,
						PayloadKey.SEND_MESSAGE));
			}
		}
	}
	
	public void disconnect() {
		try {
			connMsgHandler = null;
			mRun = false;
			
			if (mOut != null) {
				mOut.close();
			}
			if (mIn != null) {
				mIn.close();
			}
			if (mSocket != null) {
				mSocket.close();
			}
			
			informConnectionStatus(false);
		} catch (Exception e) {
			Log.e(TAG, "disconnect() "+e.getMessage());
		}
	}
	
	private class ConnectionTask extends AsyncTask<Void, String, Void> {
		
		@Override
		protected Void doInBackground(Void... arg0) {
			connectAndWait();
			return null;
		}
		
		@Override
		protected void onCancelled() {
			disconnect();
		}
	}
}
