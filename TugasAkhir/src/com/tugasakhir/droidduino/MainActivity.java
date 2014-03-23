package com.tugasakhir.droidduino;

import com.example.tugasakhir.R;
import com.tugasakhir.droidduino.connection.ArduinoClient;
import com.tugasakhir.droidduino.connection.CmdMessage;
import com.tugasakhir.droidduino.connection.ConnectionTask2;
import com.tugasakhir.droidduino.connection.MessageHandler;
import com.tugasakhir.droidduino.connection.MessageHandler.MessageListener;
import com.tugasakhir.droidduino.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener, MessageListener {

	private Button btnConnect, btnDisconnect;
	private TextView txtView1, txtView2, txtView3, txtView4;
	private ImageView imgStatus1, imgStatus2, imgStatus3, imgStatus4;
	private ToggleButton btnToggel1, btnToggel2, btnToggel3, btnToggel4;
	private Drawable lampOn, lampOff;
//	private MenuItem mMenuSettings;
	
	private ArduinoClient mClient;
	private ConnectionTask2 mConnectionTask;
	private String mCurrentMessage = CmdMessage.CMD_1_ON;
	
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
		
		imgStatus1 = (ImageView) findViewById(R.id.imageview_lamp1);
		imgStatus2 = (ImageView) findViewById(R.id.imageview_lamp2);
		imgStatus3 = (ImageView) findViewById(R.id.imageview_lamp3);
		imgStatus4 = (ImageView) findViewById(R.id.imageview_lamp4);
		
		btnToggel1 = (ToggleButton) findViewById(R.id.toggleButton1);
		btnToggel1.setOnClickListener(this);
		btnToggel2 = (ToggleButton) findViewById(R.id.toggleButton2);
		btnToggel2.setOnClickListener(this);
		btnToggel3 = (ToggleButton) findViewById(R.id.toggleButton3);
		btnToggel3.setOnClickListener(this);
		btnToggel4 = (ToggleButton) findViewById(R.id.toggleButton4);
		btnToggel4.setOnClickListener(this);
		
		disableAllToggleButton();
		
		Resources res = getResources();
		lampOn = res.getDrawable(R.drawable.lamp_on_48);
		lampOff = res.getDrawable(R.drawable.lamp_off_48);
	}
	
	public void connect(View v) {
		if (Util.isConnectingToInternet(this)) {
			mClient = new ArduinoClient(new MessageHandler(this));
			mConnectionTask = (ConnectionTask2) new ConnectionTask2(mClient).execute();
//			showProgressBar();
		}
		else {
			showAlertDialog(getString(R.string.no_connection_title),
					getString(R.string.no_connection_message));
		}
	}
	
	public void disconnect(View v) {
		if (mClient != null) {
			mClient.close();
			mClient = null;
		}
		
		if (mConnectionTask != null) {
			mConnectionTask.cancel(true);
			mConnectionTask = null;
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
	
	private void turnOffAllLamp() {
		btnToggel1.setChecked(false);
		imgStatus1.setImageDrawable(lampOff);
		btnToggel2.setChecked(false);
		imgStatus2.setImageDrawable(lampOff);
		btnToggel3.setChecked(false);
		imgStatus3.setImageDrawable(lampOff);
		btnToggel4.setChecked(false);
		imgStatus4.setImageDrawable(lampOff);
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
	
	private void doToggelButton1() {
		mCurrentMessage = CmdMessage.CMD_1_OFF;
		if (btnToggel1.isChecked()) {
			mCurrentMessage = CmdMessage.CMD_1_ON;
		}
		sendMessage();
	}
	
	private void doToggelButton2() {
		mCurrentMessage = CmdMessage.CMD_2_OFF;
		if (btnToggel2.isChecked()) {
			mCurrentMessage = CmdMessage.CMD_2_ON;
		}
		sendMessage();
	}

	private void doToggelButton3() {
		mCurrentMessage = CmdMessage.CMD_3_OFF;
		if (btnToggel3.isChecked()) {
			mCurrentMessage = CmdMessage.CMD_3_ON;
		}
		sendMessage();
	}

	private void doToggelButton4() {
		mCurrentMessage = CmdMessage.CMD_4_OFF;
		if (btnToggel4.isChecked()) {
			mCurrentMessage = CmdMessage.CMD_4_ON;
		}
		sendMessage();
	}
	
	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
	private void showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.fail);
		alertDialog.show();
	}
	
	private void sendMessage() {
		try {
			mClient.sendMessage(mCurrentMessage + "\n");
		} catch (Exception e) {
			showAlertDialog("Send Message", e.getMessage());
		}
	}

	@Override
	public void onConnectionStatus(boolean connected) {
//		hideProgressBar();
		
		if (connected) {
			enableAllToggleButton();
			showToast(getString(R.string.status_connected));
			
			btnConnect.setVisibility(View.GONE);
			btnDisconnect.setVisibility(View.VISIBLE);
			
			mCurrentMessage = CmdMessage.CMD_GET_STATUS;
			sendMessage();
		}
		else {
			turnOffAllLamp();
			disableAllToggleButton();
			showToast(getString(R.string.status_disconnected));
			
			btnConnect.setVisibility(View.VISIBLE);
			btnDisconnect.setVisibility(View.GONE);
			mConnectionTask = null;
		}
	}

	@Override
	public void onMessageSent(boolean sent) {
		if (sent) {
			StringBuilder text = new StringBuilder();
			
			if (mCurrentMessage.equals(CmdMessage.CMD_GET_STATUS)) {
				text.append("Get All LEDs State");
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_1_ON)) {
				text.append("Turn ON ");
				text.append(txtView1.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_1_OFF)) {
				text.append("Turn OFF ");
				text.append(txtView1.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_2_ON)) {
				text.append("Turn ON ");
				text.append(txtView2.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_2_OFF)) {
				text.append("Turn OFF ");
				text.append(txtView2.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_3_ON)) {
				text.append("Turn ON ");
				text.append(txtView3.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_3_OFF)) {
				text.append("Turn OFF ");
				text.append(txtView3.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_4_ON)) {
				text.append("Turn ON ");
				text.append(txtView4.getText().toString());
			}
			else if (mCurrentMessage.equals(CmdMessage.CMD_4_OFF)) {
				text.append("Turn OFF ");
				text.append(txtView4.getText().toString());
			}
			
			showToast(text.toString());
		}
		/*
		StringBuilder text = new StringBuilder();
		
		if (mCurrentMessage.equals(CmdMessage.CMD_GET_STATUS)) {
			text.append("Get Status").append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_1_ON)) {
			text.append("Turn ON ");
			text.append(txtView1.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_1_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView1.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_2_ON)) {
			text.append("Turn ON ");
			text.append(txtView2.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_2_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView2.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_3_ON)) {
			text.append("Turn ON ");
			text.append(txtView3.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_3_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView3.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_4_ON)) {
			text.append("Turn ON ");
			text.append(txtView4.getText().toString()).append(' ');
		}
		else if (mCurrentMessage.equals(CmdMessage.CMD_4_OFF)) {
			text.append("Turn OFF ");
			text.append(txtView4.getText().toString()).append(' ');
		}
		
		if (sent) {
			text.append("Sent.");
		}
		else {
			text.append("Failed.");
		}
		
		showToast(text.toString());
		*/
	}

	@Override
	public void onMessageReceived(String message) {
		if (message.equals(CmdMessage.CMD_1_ON)) {
			imgStatus1.setImageDrawable(lampOn);
			btnToggel1.setChecked(true);
		}
		else if (message.equals(CmdMessage.CMD_1_OFF)) {
			imgStatus1.setImageDrawable(lampOff);
			btnToggel1.setChecked(false);
		}
		else if (message.equals(CmdMessage.CMD_2_ON)) {
			imgStatus2.setImageDrawable(lampOn);
			btnToggel2.setChecked(true);
		}
		else if (message.equals(CmdMessage.CMD_2_OFF)) {
			imgStatus2.setImageDrawable(lampOff);
			btnToggel2.setChecked(false);
		}
		else if (message.equals(CmdMessage.CMD_3_ON)) {
			imgStatus3.setImageDrawable(lampOn);
			btnToggel3.setChecked(true);
		}
		else if (message.equals(CmdMessage.CMD_3_OFF)) {
			imgStatus3.setImageDrawable(lampOff);
			btnToggel3.setChecked(false);
		}
		else if (message.equals(CmdMessage.CMD_4_ON)) {
			imgStatus4.setImageDrawable(lampOn);
			btnToggel4.setChecked(true);
		}
		else if (message.equals(CmdMessage.CMD_4_OFF)) {
			imgStatus4.setImageDrawable(lampOff);
			btnToggel4.setChecked(false);
		}
		else {// if failed send the command 
			showAlertDialog("Failed", message);
		}
	}
	
	/*private void showProgressBar() {
		if (mMenuSettings != null) {
			mMenuSettings.setActionView(R.layout.progressbar);
			mMenuSettings.expandActionView();
		}
	}*/
	
	/*private void hideProgressBar() {
		if (mMenuSettings != null) {
			mMenuSettings.collapseActionView();
			mMenuSettings.setActionView(null);
		}
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
//		mMenuSettings = menu.findItem(R.id.action_settings);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy()");
		if (mConnectionTask != null) {
			mConnectionTask.cancel(true);
			mConnectionTask = null;
		}
	}
}
