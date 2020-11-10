package com.xpedite.activity.count;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.UserPin;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;
import com.xpedite.domain.UserRateDetail;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ConfirmTyreCountAndAmountActivity extends AppCompatActivity {

    private Seller seller;
    private JSONObject inputBodyToUpdateFinalCount;
    private UserRateDetail userRateDetail;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_tyre_count_and_amount);

        populateAmountAndCountDetails();
    }

    public void populateAmountAndCountDetails(){

        Intent intent = getIntent();

        String requestId = intent.getStringExtra("requestId");
        seller = intent.getParcelableExtra("seller");
        TyreSize tyreSize = intent.getParcelableExtra("tyreSize");

        mobileNumber = seller.getPhoneNumber();
        int smallTyreCount = tyreSize.getSmallTyreCount();
        int medTyreCount = tyreSize.getMediumTyreCount();
        int largeTyreCount = tyreSize.getLargeTyreCount();

        String responseBodyUserRateConfig = null;
        try {
            responseBodyUserRateConfig = new BackgroundUtil(RestEndpointURLs.USER_RATE_CONFIG+"/"+mobileNumber, null, "GET").execute().get();

        } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to get user rate config", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to submit request", e.getMessage());
            displayErrorPage();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
             userRateDetail=  mapper.readValue(responseBodyUserRateConfig, UserRateDetail.class);
        } catch (IOException e) {
            Log.e("Error Occurred while while trying to map user rate detail to pojo", e.getMessage());
            displayErrorPage();
        }

        int smallTyrePrice = userRateDetail.getSmallTyrePrice();
        int mediumTyrePrice = userRateDetail.getMediumTyrePrice();
        int largeTyrePrice = userRateDetail.getLargeTyrePrice();

        double smallTyreAmount = smallTyreCount*smallTyrePrice;
        double medTyreAmount = medTyreCount*mediumTyrePrice;
        double largeTyreAmount = largeTyreCount*largeTyrePrice;

        double totalAmount = smallTyreAmount + medTyreAmount + largeTyreAmount;


        TextView textViewSmallTyreCount = (TextView) findViewById(R.id.confirm_small_tyre_count);
        textViewSmallTyreCount.setText(String.valueOf(smallTyreCount) );


        TextView textViewMediumTyreCount = (TextView) findViewById(R.id.confirm_medium_tyre_count);
        textViewMediumTyreCount.setText(String.valueOf(medTyreCount ));


        TextView textViewLargeTyreCount = (TextView) findViewById(R.id.confirm_large_tyre_count);
        textViewLargeTyreCount.setText(String.valueOf(largeTyreCount ));


        TextView textViewSmallTyrePrice = (TextView) findViewById(R.id.confirm_small_tyre_price);
        textViewSmallTyrePrice.setText(String.valueOf(smallTyrePrice ));


        TextView textViewMedTyrePrice = (TextView) findViewById(R.id.confirm_med_tyre_price);
        textViewMedTyrePrice.setText(String.valueOf(mediumTyrePrice ));


        TextView textViewLargeTyrePrice = (TextView) findViewById(R.id.confirm_large_tyre_price);
        textViewLargeTyrePrice.setText(String.valueOf(largeTyrePrice ));

        TextView textViewSmallTyreAmount = (TextView) findViewById(R.id.confirm_small_tyre_amount);
        textViewSmallTyreAmount.setText(String.valueOf(smallTyreAmount ));

        TextView textViewMediumTyreAmount = (TextView) findViewById(R.id.confirm_med_tyre_amount);
        textViewMediumTyreAmount.setText(String.valueOf(medTyreAmount ));

        TextView textViewLargeTyreAmount = (TextView) findViewById(R.id.confirm_large_tyre_amount);
        textViewLargeTyreAmount.setText(String.valueOf(largeTyreAmount ));

        TextView textViewTotalAmount = (TextView) findViewById(R.id.confirm_total_amount);
        textViewTotalAmount.setText(String.valueOf(totalAmount ));


        inputBodyToUpdateFinalCount = new JSONObject();
        try {
            inputBodyToUpdateFinalCount.put("requestId", requestId);
            inputBodyToUpdateFinalCount.put("smallTyreCount", smallTyreCount);
            inputBodyToUpdateFinalCount.put("mediumTyreCount" , medTyreCount);
            inputBodyToUpdateFinalCount.put("largeTyreCount", largeTyreCount);
            inputBodyToUpdateFinalCount.put("amountPaid", totalAmount);
        } catch (JSONException e) {
            Log.e("Error occurred while trying to form json paylod ", e.getMessage());
            displayErrorPage();
        }

    }


    public void submitFinalCount(View view) {


        try {
            String pickupAgentPhoneNumber = new BackgroundUtil(RestEndpointURLs.PICKUP_AGENT_PHONE_ENDPOINT+"/"+mobileNumber, null, "GET").execute().get();

            if(pickupAgentPhoneNumber!=null && !pickupAgentPhoneNumber.isEmpty()){

                String otpSendUrl = RestEndpointURLs.OTP_API_ENDPOINT+"+91"+pickupAgentPhoneNumber+"/AUTOGEN";

                try {
                    String otpAPIResponse = new BackgroundUtil(otpSendUrl, null, "GET").execute().get();

                    if(otpAPIResponse!=null){

                        JSONObject otpAPIResponseJSON = new JSONObject(otpAPIResponse);

                        String status = otpAPIResponseJSON.getString("Status");
                        String sessionId = otpAPIResponseJSON.getString("Details");

                        if("Success".equals(status)){
                            Intent i = new Intent(getApplicationContext(), UserPin.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("sessionId", sessionId);
                            i.putExtra("inputBodyToUpdateFinalCount", inputBodyToUpdateFinalCount.toString());
                            i.putExtra("seller", seller);
                            startActivity(i);
                            finish();
                        }
                    }

                } catch (InterruptedException e) {
                    Log.e("Background thread interrupted while attempting to submit request", e.getMessage());
                    displayErrorPage();
                } catch (ExecutionException e) {
                    Log.e("Execution exception occurred while attempting to submit request", e.getMessage());
                    displayErrorPage();
                } catch (JSONException e) {
                    Log.e("Error occurred while trying to get from JSON String ", e.getMessage());
                    displayErrorPage();
                }

            }else {
                displayErrorPage();
            }


        } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to submit request", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to submit request", e.getMessage());
            displayErrorPage();
        }


//        try {
//            String responseBodyUserReq = new BackgroundUtil(RestEndpointURLs.REQUEST_HISTORY, inputBodyToUpdateFinalCount, "PUT").execute().get();
//
//        } catch (InterruptedException e) {
//            Log.e("Background thread interrupted while attempting to submit request", e.getMessage());
//            displayErrorPage();
//        } catch (ExecutionException e) {
//            Log.e("Execution exception occurred while attempting to submit request", e.getMessage());
//            displayErrorPage();
//        }
//
//        Intent i = new Intent(getApplicationContext(), RequestCompletedActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.putExtra("seller", seller);
//        i.putExtra("lastRequest", "requestUpdated");
//        startActivity(i);


    }

    public void cancel(View view) {
        super.onBackPressed();
    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
