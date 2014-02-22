package com.tugasakhir.droidduino.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
			
			if (networkInfos != null) {
				for (int i = 0; i < networkInfos.length; i++) {
					if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
}
