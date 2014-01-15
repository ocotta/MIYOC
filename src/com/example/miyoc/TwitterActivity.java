package com.example.miyoc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.assit.Constants;
import com.assit.Constants.Extra;
import com.assit.TwitterEventObserver;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.twitter.TwitterFacade;

/**
 * Activity for sharing information with Twitter
 * 
 */
public class TwitterActivity extends Activity {

	private TextView messageView;

	private TwitterFacade twitter;
	private TwitterEventObserver twitterEventObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_twitter);

		Bundle bundle = getIntent().getExtras();
		final String message = bundle == null ? "" : bundle.getString(Extra.POST_MESSAGE);

		messageView = (TextView) findViewById(R.id.message);
		Button postButton = (Button) findViewById(R.id.button_post);

		messageView.setText(message);
		postButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (twitter.isAuthorized()) {
					twitter.publishMessage(messageView.getText().toString());
					finish();
				} else {
					// Start authentication dialog and publish message after successful authentication
					twitter.authorize(new AuthListener() {
						@Override
						public void onAuthSucceed() {
							twitter.publishMessage(messageView.getText().toString());
							finish();
						}

						@Override
						public void onAuthFail(String error) { // Do nothing
						}
					});
				}
			}
		});

		twitter = new TwitterFacade(this, Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET);
		twitterEventObserver = TwitterEventObserver.newInstance();
	}

	@Override
	protected void onStart() {
		super.onStart();
		twitterEventObserver.registerListeners(this);
		if (!twitter.isAuthorized()) {
			twitter.authorize();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		twitterEventObserver.unregisterListeners();
	}

}
