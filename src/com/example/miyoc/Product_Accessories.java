package com.example.miyoc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Product_Accessories extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* First Tab Content */
		TextView textView = new TextView(this);
		textView.setText("Accessories will be shown here");
		setContentView(textView);

	}

}