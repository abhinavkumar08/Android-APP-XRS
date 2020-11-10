package com.xpedite.activity.orders;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.UserRateDetail;
import com.xpedite.domain.UserRequest;
import com.xpedite.domain.UserRequests;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.CommonUtils;
import com.xpedite.support.RestEndpointURLs;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserPickupHistory extends AppCompatActivity {

    private static final int TEXT_VIEW = 1;
    private Seller seller = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pickup_history);
        renderRequestHistory();
    }

    private UserRequests getUserRequests(String mobileNumber) {

        UserRequests userRequests = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            String requestHistoryInJson = new BackgroundUtil(RestEndpointURLs.REQUEST_HISTORY + "/" + mobileNumber, null, "GET").execute().get();
            if(requestHistoryInJson.equals("null")){
                return null;
            }

            JSONObject responseJSON = new JSONObject(requestHistoryInJson);
            Object requestHistory = responseJSON.get("userRequest");

            if (!(requestHistory instanceof JSONArray)) {
                JSONArray jArray = new JSONArray();
                jArray.put(requestHistory);
                responseJSON.put("userRequest", jArray);

            }
            userRequests = mapper.readValue(responseJSON.toString(), UserRequests.class);

        } catch (InterruptedException e) {
            Log.e("Network connection iterrupted when trying to get address", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred trying to get address", e.getMessage());
            displayErrorPage();
        } catch (JSONException e) {
            Log.e("JSONException occurred while trying to get userRequest", e.getMessage());
            displayErrorPage();
        } catch (IOException e) {
            Log.e("Error while trying to convert json to  type request", e.getMessage());
            displayErrorPage();
        }

        return userRequests;
    }

    private void renderRequestHistory(){

        Intent intent = getIntent();
        seller = intent.getParcelableExtra("seller");
        final UserRateDetail userRateDetail = intent.getParcelableExtra("userRateDetail");
        String phoneNumber = seller.getPhoneNumber();

        UserRequests userRequests = getUserRequests(phoneNumber);

        if(userRequests==null){
            renderNoResultsPage();
            return;
        }

        List<UserRequest> listOfUserRequests = userRequests.getUserRequest();
        Collections.sort(listOfUserRequests, new Comparator<UserRequest>() {
            @Override
            public int compare(UserRequest userRequest, UserRequest userRequest2) {
                return (int)(userRequest2.getPickupRequestDate().getTime() -userRequest.getPickupRequestDate().getTime());
            }
        });

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout_pickup_first);

        final float scale = getResources().getDisplayMetrics().density;

        for(int i=0;i<listOfUserRequests.size();i++){

            final UserRequest userRequest = listOfUserRequests.get(i);

            LinearLayout secondLinearLayout = new LinearLayout(this);
            secondLinearLayout.setOrientation(LinearLayout.VERTICAL);
            secondLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            int minHeightForSeconLinearLayout = (int) (75 * scale);
            secondLinearLayout.setMinimumHeight(minHeightForSeconLinearLayout);
            secondLinearLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent ob = new Intent(UserPickupHistory.this, EachOrder.class);
                    ob.putExtra("seller", seller);
                    ob.putExtra("userRequest", userRequest);
                    ob.putExtra("userRateDetail", userRateDetail);
                    startActivity(ob);


                }
            });


            int textviewPadding = (int) (10 * scale);
            int textviewTopRight = (int) (20 * scale);
            TextView textView = new TextView(this);
            textView.setTextSize(15);
            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.parseColor("#000000"));
            String orderIdText = "Order Id : "+ userRequest.getRequestId();
            textView.setText(orderIdText);
            textView.setPadding(textviewPadding, textviewTopRight, textviewPadding, 0);
            textView.setGravity(Gravity.CENTER);
            textView.setMaxWidth(3000);

            TextView textView2 = new TextView(this);
            textView2.setTextSize(10);
            textView2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textView2.setTextColor(Color.GRAY);

            String orderDateString = "Order Date : "+ CommonUtils.getDate(userRequest.getPickupRequestDate());
            textView2.setText(orderDateString);
            textView2.setPadding(textviewPadding, 0, 0, 0);
            textView2.setGravity(Gravity.CENTER);
            textView2.setMaxWidth(3000);
            textView2.setId(TEXT_VIEW * 10 + i+1);


            String paymentStatus = userRequest.getPaymentStatus();
            TextView textView3 = new TextView(this);
            textView3.setTextSize(20);

            textView3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            if("CANCELLED".equalsIgnoreCase(paymentStatus)){
                textView3.setTextColor(Color.RED);
            }else if("PAID".equalsIgnoreCase(paymentStatus)){
                textView3.setTextColor(Color.GREEN);
            }else{
                textView3.setTextColor(Color.parseColor("#ffa500"));
            }

            textView3.setText(paymentStatus);
            textView3.setPadding(textviewPadding, 0, 0, 0);
            textView3.setGravity(Gravity.END);
            textView3.setMaxWidth(3000);

            LinearLayout thirdLinearLayout = new LinearLayout(this);
            thirdLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams thirdLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


            thirdLayoutParam.setMargins(0, 0, textviewTopRight, 0);
            thirdLinearLayout.setLayoutParams(thirdLayoutParam);
            thirdLinearLayout.addView(textView2);
            thirdLinearLayout.addView(textView3);


            secondLinearLayout.addView(textView);
            secondLinearLayout.addView(thirdLinearLayout);


            int heightForBorder = (int) (1 * scale);
            LinearLayout bottomBorderLinearLayout = new LinearLayout(this);
            bottomBorderLinearLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParamsForBottomBorder = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightForBorder);
            layoutParamsForBottomBorder.setMargins(0,10,0,0);
            bottomBorderLinearLayout.setLayoutParams(layoutParamsForBottomBorder);

            bottomBorderLinearLayout.setBackgroundColor(Color.GRAY);

            linearLayout.addView(secondLinearLayout);

            linearLayout.addView(bottomBorderLinearLayout);



        }
    }

    public void renderNoResultsPage(){

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout_pickup_first);

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.GRAY);
        String noRecordText = "No Records";
        textView.setText(noRecordText);
        textView.setPadding(20, 20, 20, 20);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setMaxWidth(3000);

        linearLayout.addView(textView);

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
