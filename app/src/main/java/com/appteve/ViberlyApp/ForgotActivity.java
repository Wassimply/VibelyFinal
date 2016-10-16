package com.appteve.ViberlyApp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.constant.Constant;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotActivity extends AppCompatActivity implements Constant {

    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        email = (TextView) findViewById(R.id.emailTextForgot);
    }

    public void resetBtn (View v) {


        String url = BASE_BACKEND_URL + ENDPOINT_FORGOT_PASS + email.getText().toString().trim() + "&X-API-KEY="+ API_KEY;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            String resp =  response.getString("success");

                            new LovelyStandardDialog(ForgotActivity.this)
                                    .setTopColorRes(R.color.colorGreen)
                                    .setButtonsColorRes(R.color.colorBlack)
                                    .setIcon(R.drawable.ic_menu_manage)
                                    .setTitle("We send link" )
                                    .setMessage("Please check your e-mail.")
                                    .setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Intent registration = new Intent(ForgotActivity.this, LoginActivity.class);
                                            startActivity(registration);

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null)
                                    .show();



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ARRE", "Error: " + error.getMessage());
                // hide the progress dialog

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    public void backsBtn (View v){

        Intent registration = new Intent(ForgotActivity.this, LoginActivity.class);
        startActivity(registration);
    }
}
