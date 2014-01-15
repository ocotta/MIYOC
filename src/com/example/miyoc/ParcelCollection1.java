package com.example.miyoc;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.library.DatabaseHandler;

public class ParcelCollection1 extends Activity {
	

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_parcel);

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
         HashMap<String,String> user = new HashMap<String, String>();
         user = db.getUserDetails();
         

                 final TextView orderid = (TextView) findViewById(R.id.order_id);
                 orderid.setText("OrderID:  "+user.get("orderid"));
                 final TextView lname = (TextView) findViewById(R.id.details);
                 lname.setText("Details:  "+user.get("details"));

		
}



    }
