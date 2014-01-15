package com.example.miyoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Customer_Service extends Activity {
/** Called when the activity is first created. */

Button btnSubmit;
EditText inputSubject;
EditText inputMessage;
EditText inputEmail	;



private static String KEY_Subject = "Subject";
private static String KEY_Message = "Message"; 
private static String KEY_Email   = "To";


/* First Tab Content */

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.customer_service); 

inputEmail = (EditText) findViewById(R.id.eTextto);
inputSubject = (EditText) findViewById(R.id.eSubject);
inputMessage = (EditText) findViewById(R.id.eMessage);
btnSubmit= (Button) findViewById(R.id.Submit_but); 

	

Button Submit = (Button) findViewById(R.id.Submit_but);
Submit.setOnClickListener(new View.OnClickListener() {
    
	@Override
	public void onClick(View v) {

	  String to = inputEmail.getText().toString();
	  String subject = inputSubject.getText().toString();
	  String message = inputMessage.getText().toString();

	  Intent email = new Intent(Intent.ACTION_SEND);
	  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
	   //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
	  //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
	  email.putExtra(Intent.EXTRA_SUBJECT, subject);
	  email.putExtra(Intent.EXTRA_TEXT, message);
	  //need this to prompts email client only
	  email.setType("message/rfc822");
	  

	  startActivity(Intent.createChooser(email, "Choose an Email client :"));

}

});

}



public boolean onCreateOptionsMenu(Menu menu) {
   // Inflate the menu; this adds items to the action bar if it is present.
   MenuInflater inflater = new MenuInflater(this);
   inflater.inflate(R.menu.main, menu);
   return true;


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

