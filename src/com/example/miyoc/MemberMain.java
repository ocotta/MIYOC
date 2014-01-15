package com.example.miyoc;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.assit.Constants;
import com.assit.Constants.Extra;
import com.assit.FacebookEventObserver;
import com.assit.TwitterEventObserver;
import com.example.library.DatabaseHandler;
import com.example.library.UserFunction;

public class MemberMain extends Activity {
	Button btnLogout;
	Button changepas;
	Button shareToFb;
	Button shareToTwitter;

	SessionManager session;
	
	private FacebookEventObserver facebookEventObserver;
	private TwitterEventObserver twitterEventObserver;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_main);

		// Session class instance
		session = new SessionManager(getApplicationContext());

		facebookEventObserver = FacebookEventObserver.newInstance();
		twitterEventObserver = TwitterEventObserver.newInstance();
		
		changepas = (Button) findViewById(R.id.btchangepass);
		btnLogout = (Button) findViewById(R.id.logout);
		shareToFb = (Button) findViewById(R.id.shareTofb);
		shareToTwitter = (Button)findViewById(R.id.shareToTwitter);

		shareToFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MemberMain.this, FacebookActivity.class);
				intent.putExtra(Extra.POST_MESSAGE, Constants.FACEBOOK_SHARE_MESSAGE);
				startActivity(intent);
			}
			
		});
		
		shareToTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MemberMain.this, TwitterActivity.class);
				intent.putExtra(Extra.POST_MESSAGE, Constants.TWITTER_SHARE_MESSAGE);
				startActivity(intent);
			}
			
		});
		
		/**
		 * Call this function whenever you want to check user login This will
		 * redirect user to LoginActivity is he is not logged in
		 * */
		session.checkLogin();

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		/**
		 * Hashmap to load data from the Sqlite database
		 **/
		HashMap<String, String> user = new HashMap<String, String>();
		user = db.getUserDetails();

		/**
		 * Change Password Activity Started
		 **/
		changepas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				Intent chgpass = new Intent(getApplicationContext(),
						ChangePassword_TabHost.class);

				startActivity(chgpass);
				finish();
			}

		});

		/**
		 * Logout from the User Panel which clears the data in Sqlite database
		 **/
		btnLogout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				UserFunction logout = new UserFunction();
				session.logoutUser();
				finish();

			}
		});
		/**
		 * Sets user first name and last name in text view.
		 **/
		final TextView login = (TextView) findViewById(R.id.textwelcome);
		login.setText("Welcome  " + user.get("fname"));
		final TextView lname = (TextView) findViewById(R.id.lname);
		lname.setText(user.get("lname"));

	}

	@Override
	public void onStart() {
		super.onStart();
		facebookEventObserver.registerListeners(this);
		twitterEventObserver.registerListeners(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		facebookEventObserver.unregisterListeners();
		twitterEventObserver.unregisterListeners();
	}
	
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								android.os.Process
										.killProcess(android.os.Process.myPid());
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}
}