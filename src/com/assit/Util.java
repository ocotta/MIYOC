package com.assit;

import android.content.Context;

import com.example.miyoc.R;
import com.example.miyoc.SessionManager;

public class Util {
	public static void updateRecord(Context context, String platform) {
		String name = new SessionManager(context).getUserDetails().get("name");
		new ReflectTimeTask(name, platform).execute();
		
	}
}
