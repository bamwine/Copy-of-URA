package com.ura.ug;

import helper.SQLiteHandler;
import helper.SessionManager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.Constants;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ura.ug.R;
import com.ura.ug.R.id;
import com.ura.ug.R.layout;
import com.ura.ug.R.menu;

public class Control_usr extends Activity implements OnClickListener {

	private EditText npalte,chasi,brnumb,cifnumb;
	
	private Button save,attach;

	private SQLiteHandler db;
	private SessionManager session;

	private RequestQueue requestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		npalte = (EditText) findViewById(R.id.npalte);
		chasi = (EditText) findViewById(R.id.chasi);
		brnumb = (EditText) findViewById(R.id.brnumb);
		cifnumb = (EditText) findViewById(R.id.cifnumb);
		requestQueue = Volley.newRequestQueue(getApplication());
		
		
		
		save = (Button) findViewById(R.id.save);
		attach = (Button) findViewById(R.id.attach);
		save.setOnClickListener(this);
		attach.setOnClickListener(this);
		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

	if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from sqlite
		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		String email = user.get("email");

		

		
	}

	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(Control_usr.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    
    
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.attach:			
			break;

		case R.id.save:	
			
			if (!npalte.getText().toString().isEmpty() && !chasi.getText().toString().isEmpty() && !brnumb.getText().toString().isEmpty()&& !cifnumb.getText().toString().isEmpty()) {
				upload ();
			} else {
				Toast.makeText(getApplicationContext(),
						"Please enter your details!", Toast.LENGTH_LONG)
						.show();
			}
			
			
			break;
		default:
			break;
		}
		
	}
	
	
	void upload (){
		
		StringRequest strReq = new StringRequest(Method.POST,
				Constants.UPLOAD, new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							Log.d("Message", "Register Response: " + response.toString());
							
							try {
								JSONObject jObj = new JSONObject(response);
								//boolean error = jObj.getBoolean("error");
								int suc=jObj.getInt("success");
								if (suc==1) {
									// User successfully stored in MySQL
									// Now store the user in sqlite
//									String uid = jObj.getString("uid");
//
//									JSONObject user = jObj.getJSONObject("user");
//									String name = user.getString("name");
//									String email = user.getString("email");
//									String created_at = user.getString("created_at");
									empy();
									
								} else {

									// Error occurred in registration. Get the error
									// message
									String errorMsg = jObj.getString("message");
									Toast.makeText(getApplicationContext(),
											errorMsg, Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Log.e("Erro", "Registration Error: " + error.getMessage());
							Toast.makeText(getApplicationContext(),
									error.getMessage(), Toast.LENGTH_LONG).show();
							
						}
					}) {

				@Override
				protected Map<String, String> getParams() {
					// Posting params to register url
					Map<String, String> params = new HashMap<String, String>();
					
					params.put("npalte", npalte.getText().toString());
					params.put("chasi", chasi.getText().toString());
					params.put("brnumb", brnumb.getText().toString());
					params.put("cifnumb", cifnumb.getText().toString());
					return params;
				}

			};

			// Adding request to request queue
			requestQueue.add(strReq);
		}

void empy(){
	
	npalte.setText("");chasi.setText("");brnumb.setText("");cifnumb.setText("");;
	
}

}
