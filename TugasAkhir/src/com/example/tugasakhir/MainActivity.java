package com.example.tugasakhir;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	private Button btnConnect, btnDisconnect;
	private TextView txtView1, txtView2, txtView3, txtView4;
	private TextView txtStatus1, txtStatus2, txtStatus3, txtStatus4;
	private ToggleButton btnToggel1, btnToggel2, btnToggel3, btnToggel4;
	
	private ConnectionTask connTask;
	private boolean isConnected = false;
	private String currentCommand = Command.CMD_1_ON;
	
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnConnect = (Button) findViewById(R.id.button_connect);
		btnDisconnect = (Button) findViewById(R.id.button_disconnect);
		
		txtView1 = (TextView) findViewById(R.id.textView1);
		txtView2 = (TextView) findViewById(R.id.textView2);
		txtView3 = (TextView) findViewById(R.id.textView3);
		txtView4 = (TextView) findViewById(R.id.textView4);
		
		txtStatus1 = (TextView) findViewById(R.id.textStatus1);
		txtStatus2 = (TextView) findViewById(R.id.textStatus2);
		txtStatus3 = (TextView) findViewById(R.id.textStatus3);
		txtStatus4 = (TextView) findViewById(R.id.textStatus4);
		
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
	
	public void connect(View v) {
		connTask = (ConnectionTask) new ConnectionTask(mHandler).execute();
	}
	
	public void disconnect(View v) {
		if (connTask != null) {
			connTask.cancel(true);
			connTask = null;
		}
		disableAllToggleButton();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		if (btnToggel1.isChecked()) {
			currentCommand = Command.CMD_1_OFF;
		}
		else {
			currentCommand = Command.CMD_1_ON;
		}
		sendCommand();
	}
	
	private void doToggelButton2() {
		if (btnToggel2.isChecked()) {
			currentCommand = Command.CMD_2_OFF;
		}
		else {
			currentCommand = Command.CMD_2_ON;
		}
		sendCommand();
	}

	private void doToggelButton3() {
		if (btnToggel3.isChecked()) {
			currentCommand = Command.CMD_3_OFF;
		}
		else {
			currentCommand = Command.CMD_3_ON;
		}
		sendCommand();
	}

	private void doToggelButton4() {
		if (btnToggel4.isChecked()) {
			currentCommand = Command.CMD_4_OFF;
		}
		else {
			currentCommand = Command.CMD_4_ON;
		}
		sendCommand();
	}
	
	private void sendCommand() {
		try {
			connTask.sendCommand(currentCommand);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle b = msg.getData();
			
			if (b.containsKey(BundleKey.CONNECTION_STATUS_KEY)) {
				boolean connected = b.getBoolean(BundleKey.CONNECTION_STATUS_KEY);
				setConnectionStatus(connected);
			}
			else if (b.containsKey(BundleKey.SEND_COMMAND_KEY)) {
				boolean sent = b.getBoolean(BundleKey.SEND_COMMAND_KEY);
				sendCommandStatus(sent);
			}
			else if (b.containsKey(BundleKey.RECEIVE_COMMAND_KEY)) {
				String command = b.getString(BundleKey.RECEIVE_COMMAND_KEY);
				receiveCommand(command);
			}
		};
	};
	
	private void setConnectionStatus(boolean connected) {
		Log.v(TAG, "setConnectionStatus() status: "+connected);
		isConnected = connected;
		
		if (isConnected) {
			Toast.makeText(this, R.string.status_connected, Toast.LENGTH_SHORT).show();
			enableAllToggleButton();
			
			btnConnect.setVisibility(View.GONE);
			btnDisconnect.setVisibility(View.VISIBLE);
		}
		else {
			Toast.makeText(this, R.string.status_disconnected, Toast.LENGTH_SHORT).show();
			disableAllToggleButton();
			
			btnConnect.setVisibility(View.VISIBLE);
			btnDisconnect.setVisibility(View.GONE);
			connTask = null;
		}
	}
	
	private void sendCommandStatus(boolean sent) {
		Log.v(TAG, "sendCommandStatus() status: "+sent);
		StringBuilder text = new StringBuilder();
		
		if (currentCommand.equals(Command.CMD_1_ON)) {
			text.append("Turn ON ");
			text.append(txtView1.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_1_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView1.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_2_ON)) {
			text.append("Turn ON ");
			text.append(txtView2.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_2_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView2.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_3_ON)) {
			text.append("Turn ON ");
			text.append(txtView3.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_3_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView3.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_4_ON)) {
			text.append("Turn ON ");
			text.append(txtView4.getText().toString()).append(' ');
		}
		else if (currentCommand.equals(Command.CMD_4_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView4.getText().toString()).append(' ');
		}
		
		if (sent) {
			text.append("Sent.");
		}
		else {
			text.append("Failed.");
		}
		
		Toast.makeText(this, text.toString(), Toast.LENGTH_SHORT).show();
	}
	
	private void receiveCommand(String command) {
		Log.v(TAG, "processReceiveCommand() command: "+command);
		
		if (command.equals(Command.CMD_1_ON)) {
			txtStatus1.setText(R.string.label_on);
			txtStatus1.setTextColor(getResources().getColor(R.color.text_color_green));
		}
		else if (command.equals(Command.CMD_1_OFF)) {
			txtStatus1.setText(R.string.label_off);
			txtStatus1.setTextColor(getResources().getColor(R.color.text_color_red));
		}
		else if (command.equals(Command.CMD_2_ON)) {
			txtStatus2.setText(R.string.label_on);
			txtStatus2.setTextColor(getResources().getColor(R.color.text_color_green));
		}
		else if (command.equals(Command.CMD_2_OFF)) {
			txtStatus2.setText(R.string.label_off);
			txtStatus2.setTextColor(getResources().getColor(R.color.text_color_red));
		}
		else if (command.equals(Command.CMD_3_ON)) {
			txtStatus3.setText(R.string.label_on);
			txtStatus3.setTextColor(getResources().getColor(R.color.text_color_green));
		}
		else if (command.equals(Command.CMD_3_OFF)) {
			txtStatus3.setText(R.string.label_off);
			txtStatus3.setTextColor(getResources().getColor(R.color.text_color_red));
		}
		else if (command.equals(Command.CMD_4_ON)) {
			txtStatus4.setText(R.string.label_on);
			txtStatus4.setTextColor(getResources().getColor(R.color.text_color_green));
		}
		else if (command.equals(Command.CMD_4_OFF)) {
			txtStatus4.setText(R.string.label_off);
			txtStatus4.setTextColor(getResources().getColor(R.color.text_color_red));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
		if (connTask != null) {
			connTask.cancel(true);
			connTask = null;
		}
	}
	
}
