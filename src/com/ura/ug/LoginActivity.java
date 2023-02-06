
package com.ura.ug;


import helper.SessionManager;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.Constants;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ura.ug.R;
import com.ura.ug.R.id;
import com.ura.ug.R.layout;

public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText tin_no;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private RequestQueue requestQueue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		requestQueue = Volley.newRequestQueue(getApplication());
		tin_no = (EditText) findViewById(R.id.tin);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LoginActivity.this, Control_usr.class);
			startActivity(intent);
			finish();
		}

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String tin = tin_no.getText().toString();
				String password = inputPassword.getText().toString();

				// Check for empty data in the form
				if (tin.trim().length() > 0 && password.trim().length() > 0) {
					// login user
					checkLogin(tin, password);
				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please enter the credentials!", Toast.LENGTH_LONG)
							.show();
				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin( final String tin, final String password) {
		// Tag used to cancel the request
		//String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				Constants.LOGIN, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Login Response: " + response.toString());
						hideDialog();

						try {
							JSONObject jObj = new JSONObject(response);
//							boolean error = jObj.getBoolean("error");
							int suc=jObj.getInt("success");
							// Check for error node in json
							if (suc==1) {
								// user successfully logged in
								// Create login session
								session.setLogin(true);

								// Launch main activity
								Intent intent = new Intent(LoginActivity.this,
										Control_usr.class);
								startActivity(intent);
								finish();
							} else {
								// Error in login. Get the error message
								String errorMsg = jObj.getString("message");
								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Login Error: " + error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_LONG).show();
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "login");
				params.put("tin", tin);
				params.put("password", password);

				return params;
			}

		};

		// Adding request to request queue
		requestQueue.add(strReq);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
