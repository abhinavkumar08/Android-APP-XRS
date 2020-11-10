package com.xpedite.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.count.TyreCountDetailsActivity;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.orders.UserPickupHistory;
import com.xpedite.domain.Seller;
import com.xpedite.domain.UserRateDetail;
import com.xpedite.session.UserSession;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class UserProfileActivity extends AppCompatActivity {

    private Seller seller;
    private UserSession session;                // User Session Manager Class
    private UserRateDetail userRateDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        session = new UserSession(getApplicationContext());

        Intent intent = getIntent();
        seller = intent.getParcelableExtra("seller");
        TextView textView = (TextView)findViewById(R.id.user_profile_name);
        textView.setTextSize(20);
        String nameOfSeller = seller.getName();
        String greetingText = "Hello "+nameOfSeller;
        textView.setText(greetingText);
        textView.setTextColor(Color.BLACK);
    }

    public void requestPickup(View view) {

        String responseBodyUserRateConfig = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            responseBodyUserRateConfig = new BackgroundUtil(RestEndpointURLs.USER_RATE_CONFIG+"/"+seller.getPhoneNumber(), null, "GET").execute().get();
            userRateDetail=  mapper.readValue(responseBodyUserRateConfig, UserRateDetail.class);

        } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to sing up user", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to sing up user", e.getMessage());
            displayErrorPage();
        } catch (IOException e) {
            Log.e("Error occurred while trying to register user", e.getMessage());
            displayErrorPage();
        }

        Intent ob = new Intent(UserProfileActivity.this, TyreCountDetailsActivity.class);
        ob.putExtra("seller" , seller);
        ob.putExtra("userRateDetail", userRateDetail);
        startActivity(ob);
    }

    public void logout(View view) {
        session.logoutUser();
    }


    public void contactUs(View view) {

        String customerSupportPhoneNumberResponseBody = null;
        try {
            customerSupportPhoneNumberResponseBody = new BackgroundUtil(RestEndpointURLs.CUSTOMER_SUPPORT, null, "GET").execute().get();
        } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to get customer support details.", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to get customer support details.", e.getMessage());
            displayErrorPage();
        }

        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", customerSupportPhoneNumberResponseBody, null));
        startActivity(phoneIntent);
    }


    public void history(View view) {

        String responseBodyUserRateConfig = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            responseBodyUserRateConfig = new BackgroundUtil(RestEndpointURLs.USER_RATE_CONFIG+"/"+seller.getPhoneNumber(), null, "GET").execute().get();
            userRateDetail=  mapper.readValue(responseBodyUserRateConfig, UserRateDetail.class);

        } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to sing up user", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to sing up user", e.getMessage());
            displayErrorPage();
        } catch (IOException e) {
            Log.e("Error occurred while trying to register user", e.getMessage());
            displayErrorPage();
        }

        Intent ob = new Intent(UserProfileActivity.this, UserPickupHistory.class);
        ob.putExtra("seller" , seller);
        ob.putExtra("userRateDetail", userRateDetail);
        startActivity(ob);
    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}
