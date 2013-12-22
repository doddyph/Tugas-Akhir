package com.example.tugasakhir;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	private TextView txtView1, txtView2, txtView3, txtView4;
	private ToggleButton btnToggel1, btnToggel2, btnToggel3, btnToggel4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		String text = txtView1.getText().toString();
		
		if (btnToggel1.isChecked()) {
			text += " ON";
		}
		else {
			text += " OFF";
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	private void doToggelButton2() {
		String text = txtView2.getText().toString();
		
		if (btnToggel2.isChecked()) {
			text += " ON";
		}
		else {
			text += " OFF";
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	private void doToggelButton3() {
		String text = txtView3.getText().toString();
		
		if (btnToggel3.isChecked()) {
			text += " ON";
		}
		else {
			text += " OFF";
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	private void doToggelButton4() {
		String text = txtView4.getText().toString();
		
		if (btnToggel4.isChecked()) {
			text += " ON";
		}
		else {
			text += " OFF";
		}
		
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

}
