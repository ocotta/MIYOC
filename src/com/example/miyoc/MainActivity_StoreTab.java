package com.example.miyoc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity_StoreTab extends Activity {
	
	 LocalActivityManager mlam;
	   
	   
	 /** Called when the activity is first created. */
	 @SuppressWarnings("deprecation")
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.activity_main);

	 /** TabHost will have Tabs */
	 mlam = new LocalActivityManager(this, false);
	 mlam.dispatchCreate(savedInstanceState);
	 TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
	 tabHost.setup(mlam);
	 /** TabSpec used to create a new tab.
	 * By using TabSpec only we can able to setContent to the tab.
	 * By using TabSpec setIndicator() we can set name to tab. */
	 /** tid1 is firstTabSpec Id. Its used to access outside. */
	 /** TabSpec setIndicator() is used to set name for the tab. */
	 /** TabSpec setContent() is used to set content for a particular tab. */
	 /** Add tabSpec to the TabHost to display. */

	 
	 TabSpec firstTabSpec = tabHost.newTabSpec("Home");
	 firstTabSpec.setIndicator("Home");
	 firstTabSpec.setContent(new Intent(this,HomePage.class));
	 tabHost.addTab(firstTabSpec);

	 TabSpec secondTabSpec = tabHost.newTabSpec("Shop Online");
	 secondTabSpec.setIndicator("Shop Online");
	 secondTabSpec.setContent(new Intent(this,Product.class));
	 tabHost.addTab(secondTabSpec);

	 TabSpec thirdTabSpec = tabHost.newTabSpec("Redeem");
	 thirdTabSpec.setIndicator("Redeem");
	 thirdTabSpec.setContent(new Intent(this,Redemption.class));
	 tabHost.addTab(thirdTabSpec);

	 TabSpec forthTabSpec = tabHost.newTabSpec("Contact");
	 forthTabSpec.setIndicator("Contact");
	 forthTabSpec.setContent(new Intent(this,Customer_Service.class));
	 tabHost.addTab(forthTabSpec);
	 
	 TabSpec fifthTabSpec = tabHost.newTabSpec("Store");
	 fifthTabSpec.setIndicator("Store");
	 fifthTabSpec.setContent(new Intent(this,OurStore.class));
	 tabHost.addTab(fifthTabSpec);

	 tabHost.setCurrentTab(4);
	 tabHost.getTabWidget().getChildAt(4).setVisibility(View.GONE);

	 
	 // Removes away the icon on action bar
	 getActionBar().setDisplayShowHomeEnabled(false);
	 getActionBar().setDisplayShowTitleEnabled(false);
	 // Setting Title to the middle of the action bar

	 /* Show the custom action bar view and hide the normal Home icon and title */
	 final ActionBar actionBar = getActionBar();
	 actionBar.setCustomView(R.layout.action_bar);
	 actionBar.setDisplayShowTitleEnabled(false);
	 actionBar.setDisplayShowCustomEnabled(true);


}



public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	MenuInflater inflater = new MenuInflater(this);
	inflater.inflate(R.menu.main, menu);
	return true;
}


}

