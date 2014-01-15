package com.example.miyoc;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {



    public MyWebViewClient(Product product) {
		// TODO Auto-generated constructor stub
	}

	public void onPageStarted(WebView view, String url, Bitmap favicon) {
       //Do something to the urls, views, etc.
    }
 }