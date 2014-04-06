package com.tugasakhir.droidduino;

import com.example.tugasakhir.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private EditText editIpAddress;
	private String strIpAddress;
	private SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		sharedPreferences = getSharedPreferences(Settings.PREFERENCES, Context.MODE_PRIVATE);
		strIpAddress = sharedPreferences.getString(Settings.IP_ADDRESS_PREF, "");
		
		editIpAddress = (EditText) findViewById(R.id.editText3);
		editIpAddress.setText(strIpAddress.equals("")? Settings.IP_ADDRESS:strIpAddress);
	}
	
	public void save(View v) {
		strIpAddress = editIpAddress.getText().toString();
		
		if (strIpAddress.length() > 0) {
			Editor editor = sharedPreferences.edit();
			editor.putString(Settings.IP_ADDRESS_PREF, strIpAddress);
			editor.commit();
			
			Toast.makeText(getApplicationContext(), "Saved.", Toast.LENGTH_SHORT).show();
			finish();
		}
		else {
			Toast.makeText(getApplicationContext(), "Please insert Ip Address.", Toast.LENGTH_SHORT).show();
		}
	}
}
