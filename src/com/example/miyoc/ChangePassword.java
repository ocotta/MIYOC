package com.example.miyoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.example.library.DatabaseHandler;
import com.example.library.UserFunction;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ChangePassword extends Activity {

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";


    EditText currentpass;
    EditText newpass;
    EditText confirmpass;
    TextView alert;
    Button changepass;
    Button cancel;
    TextView passworderror;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password);

        cancel = (Button) findViewById(R.id.btcancel);
        cancel.setOnClickListener(new View.OnClickListener(){
        public void onClick(View arg0){

                Intent login = new Intent(getApplicationContext(), MemberMain_TabHost.class);

                startActivity(login);
                finish();
            }

        });


        currentpass = (EditText) findViewById(R.id.currentpass);
        newpass = (EditText) findViewById(R.id.newpass);
        confirmpass = (EditText) findViewById(R.id.confirmpass);
        alert = (TextView) findViewById(R.id.alertpass);
        changepass = (Button) findViewById(R.id.btchangepass);

        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if ( ( !confirmpass.getText().toString().equals("")) && ( !newpass.getText().toString().equals("")) && ( !currentpass.getText().toString().equals("")) )
            	
            	 if ( newpass.getText().toString().length() > 5 ){ 
            	         if (newpass.getText().toString().equals(confirmpass.getText().toString() ))
	            	{  
	                NetAsync(view);
	            	} 
	            	else {
	            		Toast.makeText(getApplicationContext(),
	                            "Password does not match", Toast.LENGTH_SHORT).show();
	                	}
	            	}
            	 else{
                     Toast.makeText(getApplicationContext(),
                             "Password should be minimum 6 characters", Toast.LENGTH_SHORT).show();
                 }
             else if ( ( !newpass.getText().toString().equals("")) && ( !currentpass.getText().toString().equals("")) ) 
             {
                 Toast.makeText(getApplicationContext(),
                         "Confirm password field empty", Toast.LENGTH_SHORT).show();
             }
             else if ( ( !confirmpass.getText().toString().equals("")) && ( !currentpass.getText().toString().equals("")) )
             {
                 Toast.makeText(getApplicationContext(),
                         "New password field empty", Toast.LENGTH_SHORT).show();
             }
             else if ( ( !confirmpass.getText().toString().equals("")) && ( !newpass.getText().toString().equals("")) )
             {
                 Toast.makeText(getApplicationContext(),
                         "Current password field empty", Toast.LENGTH_SHORT).show();
             }                          
             else
             {
                 Toast.makeText(getApplicationContext(),
                         "One or more field are empty", Toast.LENGTH_SHORT).show();
             }
            	 
             
          }  
                
            
        });}

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(ChangePassword.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                alert.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String newpas,email, password;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();



            password = currentpass.getText().toString();
            newpas = newpass.getText().toString();
            email = user.get("email");

            currentpass = (EditText) findViewById(R.id.currentpass);	
            pDialog = new ProgressDialog(ChangePassword.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunction userFunction = new UserFunction();
            JSONObject json = userFunction.chgPass(newpas, email, password);
            Log.d("Button", "Register");
            return json;


        }


        @SuppressWarnings("deprecation")
		@Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                 // Show Dialog       
                AlertDialog alertDialog = new AlertDialog.Builder(
                ChangePassword.this).create();         
                // Setting Dialog Title
                alertDialog.setTitle("Alert Dialog");
         
                // Setting Dialog Message
                alertDialog.setMessage("Password Successfully Changed!");
                  
                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        // Start new activity and end current
                        	startActivity(new Intent(ChangePassword.this, MemberMain_TabHost.class));
                        	ChangePassword.this.finish();   
                        }
                });
         
                // Showing Alert Message
                alertDialog.show();


              
                    } else {
                        pDialog.dismiss();
                        alert.setText("Current password is wrong.");
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }}
    
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
    
    public void NetAsync(View view){
        new NetCheck().execute();
    }}
