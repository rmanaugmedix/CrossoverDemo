package com.crossover.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crossover.android.app.AppController;
import com.crossover.android.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rmaninfinity on 8/25/16.
 */
public class LoginActivity extends Activity {

    Button mBtnLogin;
    EditText mEmail, mPassword;
    ProgressDialog pDialog;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        pDialog = new ProgressDialog(this);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                pDialog.setMessage("Please wait");
                pDialog.show();
                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("email", email);
                    jobj.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        Constants.URL_AUTH, jobj,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("LOG", response.toString());
                                pDialog.hide();

                                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                                try {
                                    intent.putExtra("accessToken",response.getString("accessToken"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("LOG", "Error: " + error.getMessage());
                        // hide the progress dialog
                        pDialog.hide();
                    }
                });

// Adding request to request queue
                AppController.getInstance().addToRequestQueue(jsonObjReq, "jobj_req");
            }
        });
    }

    private void initViews(){
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
    }

}
