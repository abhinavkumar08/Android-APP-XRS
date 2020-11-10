package com.xpedite.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.login.SignInActivity;
import com.xpedite.activity.login.SignUpActivity;
import com.xpedite.activity.login.UserProfileActivity;
import com.xpedite.domain.Seller;
import com.xpedite.session.UserSession;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private JSONObject credentialJson;
    private String loginPhoneString;
    private String loginPassword;
    private UserSession session;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences pref = getSharedPreferences("Reg", 0);

        Long loginPhone = pref.getLong("phoneNumber", 0);

        loginPhoneString = loginPhone.toString();
        loginPassword = pref.getString("password", "");

        if (loginPhone != 0) {
            gotoUserProfilePage();
        }
    }

    public void gotoSignupPage(View view) {

        Intent signUpPage = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(signUpPage);
    }

    public void gotoSignInPage(View view) {
        Intent signInPage = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(signInPage);
    }


    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        session.logoutUser();
        startActivity(i);
    }

    private void prepareInputForSignIn(){

        credentialJson = new JSONObject();
        try {
            credentialJson.put("userName", loginPhoneString);
            credentialJson.put("password", loginPassword);

        } catch (JSONException e) {
            Log.e("Error Occurred while creating json for sign in", e.getMessage());
            displayErrorPage();
        }
    }


    private void gotoUserProfilePage() {

        prepareInputForSignIn();

        JSONObject sellerDetailJSONObj;
        session = new UserSession(getApplicationContext());
        try {
            String signInResponseBody = new BackgroundUtil(RestEndpointURLs.SIGN_IN, credentialJson, "POST").execute().get();
            if (signInResponseBody != null) {

                ObjectMapper mapper = new ObjectMapper();
                if (signInResponseBody.equals("authorized")) {

                    String sellerDetailsInJson = new BackgroundUtil(RestEndpointURLs.SELLER + "/" + String.valueOf(loginPhoneString), null, "GET").execute().get();

                    if (sellerDetailsInJson != null) {

                        sellerDetailJSONObj = new JSONObject(sellerDetailsInJson);
                        Object listOfAddresses = sellerDetailJSONObj.get("listOfAddresses");
                        //if listOfAddresses is not of type JSONArray
                        if (!(listOfAddresses instanceof JSONArray)) {
                            JSONArray jArray = new JSONArray();
                            jArray.put(listOfAddresses);
                            sellerDetailJSONObj.put("listOfAddresses", jArray);

                        }

                        Seller seller = mapper.readValue(sellerDetailJSONObj.toString(), Seller.class);
                        session.createUserLoginSession(Long.parseLong(loginPhoneString), loginPassword);

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

                } else {
                    // username / password doesn't match&
                    Toast.makeText(getApplicationContext(),
                            "Username/Password is incorrect",
                            Toast.LENGTH_LONG).show();

                }
            }else{
                Log.e("Error Occurred while trying to sign in", "No response from server");
                displayErrorPage();
            }

            }catch(InterruptedException e){
                Log.e("Background thread interrupted while attempting to sing in or get Seller information", e.getMessage());
                displayErrorPage();

            }catch(ExecutionException e){
                Log.e("Execution exception occurred while attempting to sing in or get Seller information", e.getMessage());
                displayErrorPage();
        }catch (JSONException e) {
                Log.e("Error occurred while creating listOfAddress json", e.getMessage());
                displayErrorPage();
            }catch(IOException e){
                Log.e("IOException occurred with Credentinal Paylod", e.getMessage());
                displayErrorPage();
            }


        }


}
