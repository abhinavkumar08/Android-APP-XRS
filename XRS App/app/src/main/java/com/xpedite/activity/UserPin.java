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
import com.xpedite.activity.request.RequestCompletedActivity;
import com.xpedite.domain.Seller;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class UserPin extends AppCompatActivity {

    private Seller seller;
    private JSONObject inputBodyToUpdateFinalCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pin);

        String inputBodyToUpdateFinalCountString = getIntent().getStringExtra("inputBodyToUpdateFinalCount");
        try {
            inputBodyToUpdateFinalCount = new JSONObject(inputBodyToUpdateFinalCountString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        seller = getIntent().getParcelableExtra("seller");
    }


    public void submitRequest(View view){

        String otpEntered = ((EditText) findViewById(R.id.editText_user_pin_passcode)).getText().toString();
        if(!otpEntered.isEmpty()){

            Intent intent = getIntent();
            String sessionId = intent.getStringExtra("sessionId");
            String otpSubmitUrl = RestEndpointURLs.OTP_API_ENDPOINT+"VERIFY/"+sessionId+"/"+otpEntered;

            try {
                String otpSubmitResponse = new BackgroundUtil(otpSubmitUrl, null, "GET").execute().get();
                JSONObject otpSubmitResponseJSON;

                if(otpSubmitResponse!=null){

                    otpSubmitResponseJSON = new JSONObject(otpSubmitResponse);

                    String status = otpSubmitResponseJSON.getString("Status");

                    if("Success".equals(status)){
                        try {
                            String responseBodyUserReq = new BackgroundUtil(RestEndpointURLs.REQUEST_HISTORY, inputBodyToUpdateFinalCount, "PUT").execute().get();

                        } catch (InterruptedException e) {
                            Log.e("Background thread interrupted while attempting to submit request", e.getMessage());
                        } catch (ExecutionException e) {
                            Log.e("Execution exception occurred while attempting to submit request", e.getMessage());
                        }

                        Intent i = new Intent(getApplicationContext(), RequestCompletedActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("seller", seller);
                        i.putExtra("lastRequest", "requestUpdated");
                        startActivity(i);
                    }else{
                        Toast.makeText(UserPin.this, "Entered OTP is incorrect, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (InterruptedException e) {
                Log.e("Background thread interrupted while attempting to request PIN", e.getMessage());
                displayErrorPage();
            } catch (ExecutionException e) {
                Log.e("Execution exception occurred while attempting to request PIN", e.getMessage());
                displayErrorPage();
            } catch (JSONException e) {
                Log.e("Error occurred while creating json for PIN", e.getMessage());
                displayErrorPage();
            }
        }else{
            Toast.makeText(UserPin.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
