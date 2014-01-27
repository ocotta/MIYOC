package com.assit;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.example.miyoc.SessionManager;

public class Util {
	public static void updateRecord(Context context, String platform) {
		String name = new SessionManager(context).getUserDetails().get("name");
		String uid = new SessionManager(context).getUserDetails().get("uid");
		new ReflectTimeTask(name, platform, uid).execute();
	}

	public static void createLoginSession(JSONObject json_user,
			SessionManager session) {
		try {
			Log.e("Test...", json_user.getString("points"));
			session.createLoginSession(
					json_user.getString("id"),
					json_user.getString("fname") + ","
							+ json_user.getString("lname"),
					json_user.getString("email"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
}
