package com.assit;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.example.library.ShareFunction;

public class ReflectTimeTask extends AsyncTask<String[], Void, JSONObject> {

	private String name;
	private String platform;
	
	ShareFunction service = new ShareFunction();
	public ReflectTimeTask(String name, String platform) {
		this.name = name;
		this.platform = platform;
	}
	
	@Override
	protected JSONObject doInBackground(String[]... params) {
		JSONObject json = service.relectDb(name, platform);
		return json;
	}


}
