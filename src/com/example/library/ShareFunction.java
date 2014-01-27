package com.example.library;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

public class ShareFunction {

	private String relectdbUrl = "http://mp18.bit-mp.biz/reflect_api/";
	private JSONParser jsonParser;
	
	
	public ShareFunction() {
		// constructor
	    jsonParser = new JSONParser();
	}
	
	public JSONObject relectDb(String  name, String platform, String uid) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("platform", platform));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("updatetime", new Date().toString()));
        JSONObject json = jsonParser.getJSONFromUrl(relectdbUrl, params);
        return json;
	}
}
