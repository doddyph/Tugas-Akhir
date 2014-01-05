package com.example.tugasakhir;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnConnect;
	private TextView txtView1, txtView2, txtView3, txtView4;
	private ToggleButton btnToggel1, btnToggel2, btnToggel3, btnToggel4;
	
	private ConnectionTask connTask;
	private boolean isConnected = false;
	
	private static final String CMD_1 = "cmd=1";
	private static final String CMD_2 = "cmd=2";
	private static final String CMD_3 = "cmd=3";
	private static final String CMD_4 = "cmd=4";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnConnect = (Button) findViewById(R.id.button_connect);
		btnConnect.setOnClickListener(this);
		
		txtView1 = (TextView) findViewById(R.id.textView1);
		txtView2 = (TextView) findViewById(R.id.textView2);
		txtView3 = (TextView) findViewById(R.id.textView3);
		txtView4 = (TextView) findViewById(R.id.textView4);
		
		btnToggel1 = (ToggleButton) findViewById(R.id.toggleButton1);
		btnToggel1.setOnClickListener(this);
		btnToggel2 = (ToggleButton) findViewById(R.id.toggleButton2);
		btnToggel2.setOnClickListener(this);
		btnToggel3 = (ToggleButton) findViewById(R.id.toggleButton3);
		btnToggel3.setOnClickListener(this);
		btnToggel4 = (ToggleButton) findViewById(R.id.toggleButton4);
		btnToggel4.setOnClickListener(this);
		
		disableAllToggleButton();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_connect:
			if (isConnected) {
				doDisconnect();
			}
			else {
				doConnect();
			}
			break;
		case R.id.toggleButton1:
			doToggelButton1();
			break;
		case R.id.toggleButton2:
			doToggelButton2();
			break;
		case R.id.toggleButton3:
			doToggelButton3();
			break;
		case R.id.toggleButton4:
			doToggelButton4();
			break;
		}
	}
	
	private void doConnect() {
		
	}
	
	private void doDisconnect() {
		
	}
	
	private void disableAllToggleButton() {
		btnToggel1.setEnabled(false);
		btnToggel2.setEnabled(false);
		btnToggel3.setEnabled(false);
		btnToggel4.setEnabled(false);
	}
	
	private void enableAllToggleButton() {
		btnToggel1.setEnabled(true);
		btnToggel2.setEnabled(true);
		btnToggel3.setEnabled(true);
		btnToggel4.setEnabled(true);
	}
	
	private void doToggelButton1() {
		String text = "Sent command to Turn";
		String result = sendCommand(CMD_1);
		
		if (result.startsWith("Succesfully")) {
			if (btnToggel1.isChecked()) {
				text += " ON ";
			}
			else {
				text += " OFF ";
			}
			text += txtView1.getText().toString();
		}
		else {
			text = "Failed sent command. "+result;
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	private void doToggelButton2() {
		String text = "Sent command to Turn";
		String result = sendCommand(CMD_2);
		
		if (result.startsWith("Succesfully")) {
			if (btnToggel2.isChecked()) {
				text += " ON ";
			}
			else {
				text += " OFF ";
			}
			text += txtView2.getText().toString();
		}
		else {
			text = "Failed sent command. "+result;
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	private void doToggelButton3() {
		String text = "Sent command to Turn";
		String result = sendCommand(CMD_3);
		
		if (result.startsWith("Succesfully")) {
			if (btnToggel3.isChecked()) {
				text += " ON ";
			}
			else {
				text += " OFF ";
			}
			text += txtView3.getText().toString();
		}
		else {
			text = "Failed sent command. "+result;
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	private void doToggelButton4() {
		String text = "Sent command to Turn";
		String result = sendCommand(CMD_4);
		
		if (result.startsWith("Succesfully")) {
			if (btnToggel4.isChecked()) {
				text += " ON ";
			}
			else {
				text += " OFF ";
			}
			text += txtView4.getText().toString();
		}
		else {
			text = "Failed sent command. "+result;
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	private String sendCommand(String command) {
		String result = "";
		
		try {
			result = connTask.sendCommand(command);
		} catch (Exception e) {
			e.printStackTrace();
			result = e.getMessage();
		}
		
		return result;
	}
	
	private void processReceiveCommand(String command) {
		// TODO
		if (command.equals(CMD_1)) {
			
		}
		else if (command.equals(CMD_2)) {
			
		}
		else if (command.equals(CMD_3)) {
			
		}
		else if (command.equals(CMD_4)) {
			
		}
	}

	private void changeConnectionStatus(boolean isConnected) {
		Log.v(getClass().getName(), "changeConnectionStatus() isConnected: "+isConnected);
		this.isConnected = isConnected;
		
		if (this.isConnected) {
			Toast.makeText(getApplicationContext(), 
					"Successfully connected to server", 
					Toast.LENGTH_SHORT).show();
			btnConnect.setText(R.string.label_disconnect);
			enableAllToggleButton();
		}
		else {
			Toast.makeText(getApplicationContext(), 
					"Disconnected from server", 
					Toast.LENGTH_SHORT).show();
			btnConnect.setText(R.string.label_connect);
			disableAllToggleButton();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (connTask != null) {
			connTask.cancel(true);
		}
	}
	
	class ConnectionTask extends AsyncTask<Void, byte[], Void> {
		
		private Socket mSocket;
		private InputStream mIn;
		private OutputStream mOut;
		private BufferedReader mBufferedReader; // to store the incoming bytes from server
		
		private String mIPAddress = "";
		private int mPort = 8888;
		private boolean goRun = true;
		
		@Override
		protected Void doInBackground(Void... params) {
			Log.v(getClass().getName(), "doInBackground()");
			
			try {
				mSocket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress(mIPAddress, mPort);
				mSocket.connect(socketAddress, 5*1000);//5 second connection timeout
				
				if (mSocket.isConnected()) {
					changeConnectionStatus(true);
					
					mIn = mSocket.getInputStream();
					mOut = mSocket.getOutputStream();
					mBufferedReader = new BufferedReader(new InputStreamReader(mIn));
					
					while (goRun) {
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
		protected void onProgressUpdate(byte[]... values) {
			
			if (values.length > 0) {
				String command = new String(values[0]);
				processReceiveCommand(command);
			}
		}
		
		@Override
		protected void onCancelled() {
			Log.v(getClass().getName(), "onCancelled()");
			goRun = false;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.v(getClass().getName(), "onPostExecute()");
			changeConnectionStatus(false);
		}
		
		public String sendCommand(String command) throws Exception {
			String result = "";
			if (mSocket.isConnected()) {
				mOut.write(command.getBytes());
				result = "Succesfully send command.";
			}
			else {
				result = "Cannot send command, connection is closed.";
			}
			return result;
		}
		
		public void closeConnection() {
			Log.v(getClass().getName(), "closeConnection()");
			
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
}
