package com.xpedite.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.OTPActivity;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Seller;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;
import com.xpedite.support.ValidationUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

    }

    public void sendOtpForPasswordReset(View view) {

        String phoneNumberString = ((EditText) findViewById(R.id.editText_forgot_password_phone)).getText().toString();

        if (!ValidationUtil.isValidMobile(phoneNumberString)) {
            Toast.makeText(ForgotPasswordActivity.this, "Enter Valid phone number", Toast.LENGTH_SHORT).show();
            return;

        }

        boolean isRegistered = isUserRegistered(phoneNumberString);

        if (isRegistered) {

            String otpSendUrl = RestEndpointURLs.OTP_API_ENDPOINT + "+91" + phoneNumberString + "/AUTOGEN";

            try {
                String otpAPIResponse = new BackgroundUtil(otpSendUrl, null, "GET").execute().get();

                if (otpAPIResponse != null) {

                    JSONObject otpAPIResponseJSON = new JSONObject(otpAPIResponse);
                    String status = otpAPIResponseJSON.getString("Status");
                    String sessionId = otpAPIResponseJSON.getString("Details");

                    if ("Success".equals(status)) {
                        Intent i = new Intent(getApplicationContext(), OTPActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("sessionId", sessionId);
                        i.putExtra("mobileNumber", phoneNumberString);
                        startActivity(i);
                        finish();
                    }
                }

            } catch (InterruptedException e) {
                Log.e("Network connection interrupted when trying to send otp for resetting password", e.getMessage());
                displayErrorPage();
            } catch (ExecutionException e) {
                Log.e("Execution exception occurred trying to delete address", e.getMessage());
                displayErrorPage();
            } catch (JSONException e) {
                Log.e("Error occurred while to trying to form json payload to send otp for password reset ", e.getMessage());
                displayErrorPage();
            }
        } else {
            Toast.makeText(ForgotPasswordActivity.this, "User with this mobile number is not registered", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserRegistered(String phoneNumberString) {

        try {
            JSONObject responseJSON = null;
            String signUpResponse = new BackgroundUtil(RestEndpointURLs.SELLER + "/" + phoneNumberString, null, "GET").execute().get();
            responseJSON = new JSONObject(signUpResponse);


            Object listOfAddressesFromJson = responseJSON.get("listOfAddresses");
            if (!(listOfAddressesFromJson instanceof JSONArray)) {
                JSONArray jArray = new JSONArray();
                jArray.put(listOfAddressesFromJson);
                responseJSON.put("listOfAddresses", jArray);
            }


            ObjectMapper mapper = new ObjectMapper();

            Seller seller = mapper.readValue(responseJSON.toString(), Seller.class);
            String phoneNumber = seller.getPhoneNumber();

            if ("0".equals(phoneNumber)) {

                return false;
            }


        } catch (InterruptedException e) {
            Log.e("Network connection interrupted when trying to check for user registration", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred trying to check for user registration", e.getMessage());
            displayErrorPage();
        } catch (IOException e) {
            Log.e("Error occurred while to trying to check for user registration", e.getMessage());
            displayErrorPage();
        } catch (JSONException e) {
            Log.e("Error occurred while to trying to check for user registration", e.getMessage());
            return false;
        }

        return true;
    }

    public void goBackToLoginPage(View view){
        super.onBackPressed();
    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
