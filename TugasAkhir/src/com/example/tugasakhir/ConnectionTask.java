package com.example.tugasakhir;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConnectionTask extends AsyncTask<Void, byte[], Void> {
	
	private Handler mHandler;
	private Socket mSocket;
	private InputStream mIn;
	private OutputStream mOut;
	private BufferedReader mBufferedReader; // to store the incoming bytes from server
	private boolean mRun = true;
	
	private static final String TAG = "ConnectionTask";
	
	public ConnectionTask(Handler handler) {
		mHandler = handler;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Log.v(TAG, "doInBackground()");
		
		try {
			mSocket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(Setting.IP_ADDRESS, Setting.PORT);
			mSocket.connect(socketAddress, 5*1000);//5 second connection timeout
			
			if (mSocket.isConnected()) {
				setConnectionStatus(true);
				
				mIn = mSocket.getInputStream();
				mOut = mSocket.getOutputStream();
				mBufferedReader = new BufferedReader(new InputStreamReader(mIn));
				
				while (mRun) {
					String msgFromServer = mBufferedReader.readLine();
					byte[] msgBytes = msgFromServer.getBytes();
					publishProgress(msgBytes);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
		
		return null;
	}
	
	@Override
	protected void onCancelled() {
		Log.v(TAG, "onCancelled()");
		
		mRun = false;
		closeConnection();
		setConnectionStatus(false);
	}

	@Override
	protected void onProgressUpdate(byte[]... values) {
		Log.v(TAG, "onProgressUpdate()");
		
		if (values.length > 0) {
			String command = new String(values[0]);
			receiveCommand(command);
		}
	}
	
	@Override
	protected void onPostExecute(Void result) {
		Log.v(TAG, "onPostExecute()");
		setConnectionStatus(false);
	}
	
	private void setConnectionStatus(boolean connected) {
		Log.v(TAG, "setConnStatus() status: "+connected);
		
		if (mHandler != null) {
			Bundle b = new Bundle();
			b.putBoolean(BundleKey.CONNECTION_STATUS_KEY, connected);
			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
		}
	}
	
	public void sendCommand(String command) throws Exception {
		Log.v(TAG, "sendCommand() command: "+command);
		
		boolean sent = false;
		if (mSocket.isConnected()) {
			mOut.write(command.getBytes());
			sent = true;
		}
		
		if (mHandler != null) {
			Bundle b = new Bundle();
			b.putBoolean(BundleKey.SEND_COMMAND_KEY, sent);
			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
		}
	}
	
	private void receiveCommand(String command) {
		Log.v(TAG, "receiveCommand() command: "+command);
		
		if (mHandler != null) {
			Bundle b = new Bundle();
			b.putString(BundleKey.RECEIVE_COMMAND_KEY, command);
			Message msg = new Message();
			msg.setData(b);
			mHandler.sendMessage(msg);
		}
	}
	
	public void closeConnection() {
		Log.v(TAG, "closeConnection()");
		
		try {
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
			e.printStackTrace();
		}
	}
	
}
