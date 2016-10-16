package com.appteve.ViberlyApp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements Constant {

     EditText emailText;
     EditText passwordText;
     Button loginBtn;
     Button registerBtn;
     Button forgotBtn;
     SharedPreferences userPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        loginBtn = (Button) findViewById(R.id.buttonLogin);
        registerBtn = (Button) findViewById(R.id.buttonRegister);
        forgotBtn = (Button) findViewById(R.id.buttonForgot);

    }

    public void login_click(View v){

        loginUser();
    }

    public void registration_click(View v){

        Intent registration = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(registration);
    }

    public void  loginUser() {


        final String password = passwordText.getText().toString().trim();
        final String email = emailText.getText().toString().trim();

        if (password == null || email == null) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Please fill in all required fields.");

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });



            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else {

            String request = BASE_BACKEND_URL + ENDPOINT_USER_LOGIN;

            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
                params.put("password", password);
                params.put("X-API-KEY", API_KEY);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    request, params,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {


                            try {

                                int errors = response.getInt("error");
                                System.out.println("ERRORS - " + errors);

                                if (errors == 0) {

                                    JSONObject loginObg = response.getJSONObject("respon");


                                    String username = loginObg.getString("username");
                                    String photo = loginObg.getString("profile_photo");
                                    String user_id = loginObg.getString("id");

                                    userPreference = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = userPreference.edit();
                                    editor.putString(APP_PREFERENCES_NAME, username);
                                    editor.putString(APP_PREFERENCES_PHOTO, photo);
                                    editor.putString(APP_PREFERENCES_ID, user_id);
                                    editor.apply();

                                    Intent searchIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(searchIntent);

                                } else {

                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Invalid email or password!", Toast.LENGTH_SHORT);
                                    toast.show();
                                }


                            } catch (JSONException e) {
                                //some exception handler code.
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("RESP", "Error: " + error.getMessage());
                    // pDialog.hide();
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };


            AppController.getInstance().addToRequestQueue(jsonObjReq);

        }
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    public void forgotBtnClick(View v){

        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);


    }
}
