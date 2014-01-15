package com.example.miyoc;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.DatabaseHandler;
import com.example.library.UserFunction;

public class Redemption extends Activity {
    Button btnLogin;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    // Session manager class
    SessionManager session;
    Button register_but;
    private TextView loginErrorMsg;
    CheckBox CheckBox;
    public static final String PREFS_NAME = "LoginPrefs";  
    
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_page1);
        
        // Session Manager
        session = new SessionManager(getApplicationContext());           

        inputEmail = (EditText) findViewById(R.id.eLogin);
        inputPassword = (EditText) findViewById(R.id.ePass);
        register_but = (Button) findViewById(R.id.register_but);
        btnLogin = (Button) findViewById(R.id.login_but);
        passreset = (Button)findViewById(R.id.forgetpass);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        CheckBox = (CheckBox) findViewById(R.id.CheckBox);

        
        
        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        Intent myIntent = new Intent(view.getContext(), ResetPassword_TabHost.class);
        startActivityForResult(myIntent, 0);
        finish();
        }});

        register_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register_TabHost.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});
        
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String Email = settings.getString("Email", "");
		String Password = settings.getString("Password", "");
		inputEmail.setText(Email);
		inputPassword.setText(Password);
		Boolean isChecked = settings.getBoolean("CheckBox", false);
		CheckBox.setChecked(isChecked );

		
		
      
        /**
         * Login button click event
         * A Toast is set to alert when the Email and Password field is empty
         **/
                btnLogin.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                    	String strEmail = inputEmail.getText().toString();
        				String strPass = inputPassword.getText().toString();


                        if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                        {
                            
                        	if (CheckBox.isChecked()) {
                        		SharedPreferences settings = getSharedPreferences(
        								PREFS_NAME, Context.MODE_PRIVATE);
        						settings.edit().putString("Email", strEmail)
        								.putString("Password", strPass)
        								.putBoolean("CheckBox", true) 
        								.commit();
                        	}else{
                        		SharedPreferences settings = getSharedPreferences(
                						PREFS_NAME, Context.MODE_PRIVATE);
                				settings.edit().clear().commit();
                        	}
                        	NetAsync(view);
                        
                        }
                        else if ( ( !inputEmail.getText().toString().equals("")) )
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Password field empty", Toast.LENGTH_SHORT).show();
                        }
                        else if ( ( !inputPassword.getText().toString().equals("")) )
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Email field empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }



    /**
     * Async Task to check whether internet connection is working.
     **/

        private class NetCheck extends AsyncTask<String,String,Boolean>
        {
            private ProgressDialog nDialog;

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                nDialog = new ProgressDialog(Redemption.this);
                nDialog.setTitle("Checking Network");
                nDialog.setMessage("Loading..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
            }
            /**
             * Gets current device state and checks for working internet connection by trying Google.
            **/
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
                    new ProcessLogin().execute();
                }
                else{
                    nDialog.dismiss();
                    loginErrorMsg.setText("Error in Network Connection");
                }
            }
        }

        /**
         * Async Task to get and send data to My Sql database through JSON respone.
         **/
        
        private class ProcessLogin extends AsyncTask<String, String, JSONObject> {


            private ProgressDialog pDialog;

            String email,password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                inputEmail = (EditText) findViewById(R.id.eLogin);
                inputPassword = (EditText) findViewById(R.id.ePass);
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                pDialog = new ProgressDialog(Redemption.this);
                pDialog.setTitle("Contacting Servers");
                pDialog.setMessage("Logging in ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... args) {

                UserFunction userFunction = new UserFunction();
                JSONObject json = userFunction.loginUser(email, password);
                return json;
            }

            @Override
            protected void onPostExecute(JSONObject json) {
                try {
                   if (json.getString(KEY_SUCCESS) != null) {

                        String res = json.getString(KEY_SUCCESS);

                        if(Integer.parseInt(res) == 1){
                            pDialog.setMessage("Loading User Space");
                            pDialog.setTitle("Getting Data");
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                            /**
                             * Clear all previous data in SQlite database.
                             **/
                            UserFunction logout = new UserFunction();
                            logout.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                       	 
                            // Creating user login session
                            // For testing i am string name, email as follow
                            // Use user real data
                            session.createLoginSession("Android Hive", "anroidhive@gmail.com");
                            
                            /**
                            *If JSON array details are stored in SQlite it launches the User Panel.
                            **/
                            Intent upanel = new Intent(getApplicationContext(), Redemption1_TabHost.class);
                            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pDialog.dismiss();
                            startActivity(upanel);
                            /**
                             * Close Login Screen
                             **/
                            finish();
                        }else{

                            pDialog.dismiss();
                            loginErrorMsg.setText("Incorrect username/password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
           }
        }
        public void NetAsync(View view){
            new NetCheck().execute();
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