package com.xpedite.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.login.ResetPasswordActivity;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class OTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

    }

    public void updatePassword(View view){

        String otpEntered = ((EditText) findViewById(R.id.editText_otp_entered)).getText().toString();
        if(!otpEntered.isEmpty()){

            Intent intent = getIntent();
            String sessionId = intent.getStringExtra("sessionId");
            String phoneNumberString = intent.getStringExtra("mobileNumber");
            String otpSubmitUrl = RestEndpointURLs.OTP_API_ENDPOINT+"VERIFY/"+sessionId+"/"+otpEntered;

            try {
                String otpSubmitResponse = new BackgroundUtil(otpSubmitUrl, null, "GET").execute().get();
                JSONObject otpSubmitResponseJSON;

                if(otpSubmitResponse!=null){

                    otpSubmitResponseJSON = new JSONObject(otpSubmitResponse);

                    String status = otpSubmitResponseJSON.getString("Status");

                    if("Success".equals(status)){
                        Intent i = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("sessionId", sessionId);
                        i.putExtra("mobileNumber", phoneNumberString);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(OTPActivity.this, "Entered OTP is incorrect, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (InterruptedException e) {
                Log.e("Background thread interrupted while attempting to request OTP", e.getMessage());
                displayErrorPage();

            } catch (ExecutionException e) {
                Log.e("Execution exception occurred while attempting to request OTP", e.getMessage());
                displayErrorPage();
            } catch (JSONException e) {
                Log.e("Error occurred while creating json for OTP", e.getMessage());
                displayErrorPage();
            }

        }else{
            Toast.makeText(OTPActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
