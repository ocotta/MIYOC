package com.example.miyoc;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.assit.Constants;
import com.assit.Constants.Extra;
import com.assit.FacebookEventObserver;
import com.nostra13.socialsharing.common.AuthListener;
import com.nostra13.socialsharing.facebook.FacebookFacade;

/**
 * Activity for sharing information with Facebook
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class FacebookActivity extends Activity {

	private FacebookFacade facebook;
	private FacebookEventObserver facebookEventObserver;

	private TextView messageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_facebook);

		facebook = new FacebookFacade(this, Constants.FACEBOOK_APP_ID);
		facebookEventObserver = FacebookEventObserver.newInstance();

		messageView = (TextView) findViewById(R.id.message);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String message = bundle.getString(Extra.POST_MESSAGE);
			messageView.setText(message);
		}
		
		Button postImageButton = (Button) findViewById(R.id.button_post_image);

		postImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (facebook.isAuthorized()) {
					publishImage();
					finish();
				} else {
					// Start authentication dialog and publish image after successful authentication
					facebook.authorize(new AuthListener() {
						@Override
						public void onAuthSucceed() {
							publishImage();
							finish();
						}

						@Override
						public void onAuthFail(String error) { // Do noting
						}
					});
				}
			}
		});
	}

	private void publishImage() {
		Bitmap bmp = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher)).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();
		String capture = messageView.getText().toString();
		String msg = (capture == null ? Constants.FACEBOOK_SHARE_IMAGE_CAPTION:capture);
		facebook.publishImage(bitmapdata, msg);
	}

	@Override
	public void onStart() {
		super.onStart();
		facebookEventObserver.registerListeners(this);
		if (!facebook.isAuthorized()) {
			facebook.authorize();
		}
	}

	@Override
	public void onStop() {
		facebookEventObserver.unregisterListeners();
		super.onStop();
	}
}
