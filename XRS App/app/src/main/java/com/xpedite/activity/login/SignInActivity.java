package com.xpedite.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Seller;
import com.xpedite.session.UserSession;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;
import com.xpedite.support.ValidationUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SignInActivity extends AppCompatActivity {

    private long phoneNumberUserName;
    private String password;
    private UserSession session;                // User Session Manager Class
    private JSONObject credentialJson;


    private boolean prepareInputJSON() {

        String phoneNumberAsString = ((EditText) findViewById(R.id.signIn_phn)).getText().toString();
        if(!ValidationUtil.isValidMobile(phoneNumberAsString)) {
            Toast.makeText(SignInActivity.this, "Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        phoneNumberUserName = Long.parseLong(phoneNumberAsString);
        password = ((EditText) findViewById(R.id.editText_signIn_pwd)).getText().toString();

        if(ValidationUtil.isFieldBlank(password)) {
            Toast.makeText(SignInActivity.this, "Password Cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            credentialJson = new JSONObject();
            credentialJson.put("userName", phoneNumberAsString);
            credentialJson.put("password", password);

        } catch (JSONException e) {
            Log.e("Error Occurred while creating json for sign in", e.getMessage());
            displayErrorPage();
        }

        return true;

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        session.logoutUser();
        startActivity(i);
    }

    public void signInUser(View view) {

        boolean isInputJsonValid = prepareInputJSON();
        session = new UserSession(getApplicationContext());

        if (isInputJsonValid) {
            try {
                String signInResponseBody = new BackgroundUtil(RestEndpointURLs.SIGN_IN, credentialJson, "POST").execute().get();
                if(signInResponseBody!=null) {

                    ObjectMapper mapper = new ObjectMapper();

                    if (signInResponseBody.equals("authorized")) {

                        String sellerDetailsInJson = new BackgroundUtil(RestEndpointURLs.SELLER + "/" + String.valueOf(phoneNumberUserName), null, "GET").execute().get();

                        if (sellerDetailsInJson != null) {

                            JSONObject sellerDetailJSONObj = new JSONObject(sellerDetailsInJson);
                            Object listOfAddresses = sellerDetailJSONObj.get("listOfAddresses");
                            if (!(listOfAddresses instanceof JSONArray)) {
                                JSONArray jArray = new JSONArray();
                                jArray.put(listOfAddresses);
                                sellerDetailJSONObj.put("listOfAddresses", jArray);

                            }

                            Seller seller = mapper.readValue(sellerDetailJSONObj.toString(), Seller.class);
                            session.createUserLoginSession(phoneNumberUserName, password);

                            // Starting UserProfile Activity
                            Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("seller", seller);
                            Bundle bundle = new Bundle();
                            i.putExtras(bundle);
                            // Add new Flag to start new Activity
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();

                        }else{
                            Log.e("Error Occurred while trying to sign in", "No response from server");
                            displayErrorPage();
                        }

                    } else if("INACTIVE".equals(signInResponseBody)) {
                        // username / password doesn't match&
                        Toast.makeText(getApplicationContext(),
                                "User is InActive, Please contact Admin to Activate your profile.",
                                Toast.LENGTH_LONG).show();
                    }else{
                        // username / password doesn't match&
                        Toast.makeText(getApplicationContext(),
                                signInResponseBody,
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Log.e("Error Occurred while trying to sign in", "No response from server");
                    displayErrorPage();
                }
            } catch (InterruptedException e) {
                Log.e("Background thread interrupted while attempting to sing in or get Seller information", e.getMessage());
                displayErrorPage();
            } catch (ExecutionException e) {
                Log.e("Execution exception occurred while attempting to sing in or get Seller information", e.getMessage());
                displayErrorPage();
            } catch (IOException e) {
                Log.e("IOException occurred with Credentinal Paylod", e.getMessage());
                displayErrorPage();
            } catch (JSONException e) {
                Log.e("Error occurred while creating listOfAddress json", e.getMessage());
                displayErrorPage();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    }

    public void gotoForgotPasswordPage(View view){

        Intent forgotPasswordPage = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(forgotPasswordPage);

    }

}
