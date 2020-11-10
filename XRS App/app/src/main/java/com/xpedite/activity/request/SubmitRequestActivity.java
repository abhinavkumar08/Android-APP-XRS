package com.xpedite.activity.request;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Seller;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class SubmitRequestActivity extends AppCompatActivity {

    private JSONObject inputBodyForUserRequest;
    private Seller seller;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_request);
        Intent intent = getIntent();

        String inputBodyForUserRequestString = intent.getStringExtra("inputBodyForUserRequest");
        seller = intent.getParcelableExtra("seller");
        address = intent.getStringExtra("address");



        try {
            inputBodyForUserRequest = new JSONObject(inputBodyForUserRequestString);
            inputBodyForUserRequest.put("primaryUserMobileNumber", seller.getPhoneNumber());
            inputBodyForUserRequest.put("address", address);
            fillDetails(inputBodyForUserRequest.getString("amountPaid"));
        } catch (JSONException e) {
            Log.e("Error occurred while trying to prepare payload for submit request", e.getMessage());
            displayErrorPage();
        }


    }

    private void fillDetails(String amount) {

        String smallTyreCount = null;
        String medTyreCount = null;
        String largeTyreCount = null;

        try {
            smallTyreCount = inputBodyForUserRequest.getString("smallTyreCount");
            medTyreCount = inputBodyForUserRequest.getString("mediumTyreCount");
            largeTyreCount = inputBodyForUserRequest.getString("largeTyreCount");


        } catch (JSONException e) {
            Log.e("Error occurred while trying to fetch tyre count details from submit request", e.getMessage());
            displayErrorPage();
        }

        TextView textViewName = ((TextView) findViewById(R.id.textView_submit_name));
        textViewName.setTextColor(Color.BLACK);
        textViewName.setTextSize(20);

        TextView textViewPhone = ((TextView) findViewById(R.id.textView_submit_phone));
        textViewPhone.setTextColor(Color.BLACK);
        textViewPhone.setTextSize(20);

        TextView textViewAddress = ((TextView) findViewById(R.id.textView_submit_address));
        textViewAddress.setTextColor(Color.BLACK);
        textViewAddress.setTextSize(20);

        TextView textViewSmallTyreCount = ((TextView) findViewById(R.id.textView_submit_small_tyre));
        textViewSmallTyreCount.setTextColor(Color.BLACK);
        textViewSmallTyreCount.setTextSize(20);

        TextView textViewMedTyreCount = ((TextView) findViewById(R.id.textView_submit_med_tyre));
        textViewMedTyreCount.setTextColor(Color.BLACK);
        textViewMedTyreCount.setTextSize(20);

        TextView textViewLargeTyreCount = ((TextView) findViewById(R.id.textView_submit_large));
        textViewLargeTyreCount.setTextColor(Color.BLACK);
        textViewLargeTyreCount.setTextSize(20);

        TextView textViewSubmitCash = ((TextView) findViewById(R.id.textView_submit_cash));
        textViewSubmitCash.setTextColor(Color.BLACK);
        textViewSubmitCash.setTextSize(20);

        if(address!=null){

            String addressDetailArray[] = address.split(",");
            textViewName.setText(addressDetailArray[0]);
            textViewPhone.setText(addressDetailArray[1]);
            textViewAddress.setText(prepareAddressStringFromText(addressDetailArray));
        }


        textViewSmallTyreCount.setText(smallTyreCount);
        textViewMedTyreCount.setText(medTyreCount);
        textViewLargeTyreCount.setText(largeTyreCount);
        textViewSubmitCash.setText("Rs. "+amount);

    }

    public void submit(View view) {
        try {
            Button cancelRequestButton = ((Button) findViewById(R.id.button_submit));
            cancelRequestButton.setEnabled(false);
            cancelRequestButton.setText("Processing...");

            String responseBodyUserReq = new BackgroundUtil(RestEndpointURLs.USER_REQUEST, inputBodyForUserRequest, "POST").execute().get();
            //cancelRequestButton.setEnabled(true);

        } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to submit request", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to submit request", e.getMessage());
            displayErrorPage();
        }

        Intent i = new Intent(getApplicationContext(), RequestCompletedActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("seller", seller);
        startActivity(i);
    }

    private String prepareAddressStringFromText(String[] addressDetailArray) {

        int arrayLength = addressDetailArray.length;
        if(arrayLength<5){
            return "";
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 2; i < addressDetailArray.length; i++) {
            builder.append(addressDetailArray[i]);
            if(i!=addressDetailArray.length-1)
            builder.append(", ");
        }

        return builder.toString();

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
