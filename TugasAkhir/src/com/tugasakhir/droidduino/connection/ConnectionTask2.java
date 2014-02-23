package com.tugasakhir.droidduino.connection;

import android.os.AsyncTask;

public class ConnectionTask2 extends AsyncTask<Void, String, Void> {
	
	private ArduinoClient mClient;
	
	public ConnectionTask2(ArduinoClient client) {
		mClient = client;
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (mClient != null) {
			mClient.run();
		}
		return null;
	}
	
	@Override
	protected void onCancelled() {
		if (mClient != null) {
			mClient.close();
		}
	}
	
}
