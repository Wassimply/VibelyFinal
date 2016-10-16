package com.appteve.ViberlyApp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appteve.ViberlyApp.Helpers.AppHelper;
import com.appteve.ViberlyApp.Helpers.VolleyMultipartRequest;
import com.appteve.ViberlyApp.Helpers.VolleySingleton;
import com.appteve.ViberlyApp.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements Constant{


    ImageView avatarImage;
    String encodedString;
    String fileName;

    EditText usernameFields;
    EditText passswordFields;
    EditText emailFields;

    SharedPreferences userPreference;


    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        avatarImage = (ImageView) findViewById(R.id.imageAvatar);

        usernameFields = (EditText) findViewById(R.id.editUsername);
        passswordFields = (EditText) findViewById(R.id.editPassword);
        emailFields = (EditText) findViewById(R.id.editEmail);
    }

    public void addphoto_click (View v){

        chooseImage();

    }

    public void chooseImage(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                avatarImage.setImageBitmap(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] byte_arr = stream.toByteArray();
                encodedString = Base64.encodeToString(byte_arr, 0);
                uploadImage();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){


        String url = BASE_BACKEND_URL + ENDPOINT_IMAGE_UPLOAD;

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);



                try {
                   JSONObject result = new JSONObject(resultResponse);

                    fileName = result.getString("image");

                    System.out.println("RESPONSE ++++++++ " + fileName);

               } catch (JSONException e) {
                    e.printStackTrace();
               }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("X-API-KEY", API_KEY);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("file", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), avatarImage.getDrawable()), "image/jpeg"));


                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);


    }

    public  void reg_click (View v){

        registrationUser();


    }

    public  void registrationUser() {


        final String password = passswordFields.getText().toString().trim();
        final String email = emailFields.getText().toString().trim();
        final String username = usernameFields.getText().toString().trim();

        System.out.println("REG -----  " + password + email + username);

        if (password == null || email == null || username == null || fileName == null) {


            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Please add an image and fill in all required fields.");

            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                   // Toast.makeText(RegistrationActivity.this,"",Toast.LENGTH_LONG).show();
                }
            });



            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        } else {

            String request = BASE_BACKEND_URL + ENDPOINT_USER_REGISTER;

            System.out.println("TEDDD - : " + password);
            System.out.println("TEDDDzzz - : " + email);

            System.out.println(request);

            JSONObject params = new JSONObject();
            try {
                params.put("email", email);
                params.put("password", password);
                params.put("username", username);
                params.put("profile_photo", fileName);
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

                                String user_ids = response.getString("message");

                                System.out.println("ERRORS MMM - " + user_ids);


                                if (user_ids == "0") {


                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Invalid email !", Toast.LENGTH_SHORT);
                                    toast.show();


                                } else {

                                    System.out.println("ERRORS MMM GOGOGOGOG");

                                    userPreference = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor = userPreference.edit();
                                    editor.putString(APP_PREFERENCES_NAME, username);
                                    editor.putString(APP_PREFERENCES_PHOTO, fileName);
                                    editor.putString(APP_PREFERENCES_ID, user_ids);
                                    editor.apply();

                                    Intent searchIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                                    // searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(searchIntent);

                                   // System.out.println("OBJR - " + loginObg);
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





}
