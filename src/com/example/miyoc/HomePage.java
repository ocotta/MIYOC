package com.example.miyoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class HomePage extends Activity {

/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main_page);

}

public void onBackPressed() {

   AlertDialog.Builder builder = new AlertDialog.Builder(this);
   builder.setMessage("Are you sure you want to exit?")
          .setCancelable(false)
          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                 android.os.Process.killProcess(android.os.Process.myPid());
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