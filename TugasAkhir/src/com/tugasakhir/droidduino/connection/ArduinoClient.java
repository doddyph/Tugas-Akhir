package com.tugasakhir.droidduino.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.tugasakhir.droidduino.Settings;

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
	
	private MessageHandler mHandler;
	
	public ArduinoClient(MessageHandler handler) {
		mHandler = handler;
	}
	
	public void run() {
		try {
			mSocket = new Socket();
			mSocket.connect(new InetSocketAddress(Settings.IP_ADDRESS, PORT), TIMEOUT);
			
			if (mSocket.isConnected()) {
				setConnectionStatus(true);
				
				mIn = mSocket.getInputStream();
				mOut = mSocket.getOutputStream();
				mBuff = new BufferedReader(new InputStreamReader(mIn));
				
				while (mRun) {
					String msgFromServer = mBuff.readLine();
					byte[] msgBytes = msgFromServer.getBytes();
					receiveMessage(new String(msgBytes));
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "run() "+e.getMessage());
		} finally {
			close();
		}
	}
	
	private void setConnectionStatus(boolean connected) {
		if (mHandler != null) {
			mHandler.sendMessage(MessageHandler.MessageBuilder(
					connected,
					PayloadKey.CONNECTION_STATUS));
		}
	}
	
	private void receiveMessage(String message) {
		if (mHandler != null) {
			mHandler.sendMessage(MessageHandler.MessageBuilder(
					message,
					PayloadKey.RECEIVE_MESSAGE));
		}
	}
	
	public void sendMessage(String message) {
		if (mSocket.isConnected()) {
			try {
				mOut.write(message.getBytes());
			} catch (IOException e) {
				Log.e(TAG, "sendMessage() "+e.getMessage());
				return;
			}
			
			if (mHandler != null) {
				mHandler.sendMessage(MessageHandler.MessageBuilder(
						true,
						PayloadKey.SEND_MESSAGE));
			}
		}
	}
	
	public void close() {
		try {
			setConnectionStatus(false);
			mHandler = null;
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
		} catch (Exception e) {
			Log.e(TAG, "close() "+e.getMessage());
		}
	}
}
