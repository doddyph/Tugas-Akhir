package com.example.tugasakhir;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText username, password;
	private CheckBox checkBoxShowPasswd;
	
	private static final String USERNAME = "hendri";
	private static final String PASSWORD = "tugas akhir";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		checkBoxShowPasswd = (CheckBox) findViewById(R.id.checkBox1);
	}
	
	public void doShowPassword(View v) {
		switch (v.getId()) {
		case R.id.checkBox1:
			if (checkBoxShowPasswd.isChecked()) {
				showPassword(true);
			}
			else {
				showPassword(false);
			}
			break;
		}
	}
	
	private void showPassword(boolean show) {
		if (show) {
			password.setTransformationMethod(null);
		}
		else {
			password.setTransformationMethod(new PasswordTransformationMethod());
		}
	}
	
	public void doLogin(View v) {
		switch (v.getId()) {
		case R.id.button1:
			login();
			break;
		}
	}
	
	private void login() {
		String txtUsername = username.getText().toString();
		String txtPassword = password.getText().toString();
		String txtToast = "";
		
		if (txtUsername.length() == 0) {
			txtToast = "Please insert username.";
		}
		else if (txtPassword.length() == 0) {
			txtToast = "Please insert password";
		}
		else if (!txtUsername.equals(USERNAME)) {
			txtToast = "Wrong username!";
		}
		else if (!txtPassword.equals(PASSWORD)) {
			txtToast = "Wrong password!";
		}
		
		if (txtToast.length() > 0) {
			Toast.makeText(getApplicationContext(), txtToast, Toast.LENGTH_SHORT).show();
			return;
		}
		
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
