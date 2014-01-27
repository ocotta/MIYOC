package com.assit;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.library.ShareFunction;

public class ReflectTimeTask extends AsyncTask<String[], Void, JSONObject> {

	private String name;
	private String platform;
	private String uid;
	
	ShareFunction service = new ShareFunction();
	public ReflectTimeTask(String name, String platform, String uid) {
		this.name = name;
		this.platform = platform;
		this.uid = uid;
	}
	
	@Override
	protected JSONObject doInBackground(String[]... params) {
		JSONObject json = service.relectDb(name, platform, uid);
		return json;
	}


}
