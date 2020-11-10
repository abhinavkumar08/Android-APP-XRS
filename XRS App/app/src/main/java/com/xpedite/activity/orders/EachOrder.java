package com.xpedite.activity.orders;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.count.TyreCountFinalDetail;
import com.xpedite.activity.request.CancelRequestPopUp;
import com.xpedite.domain.Seller;
import com.xpedite.domain.UserRateDetail;
import com.xpedite.domain.UserRequest;
import com.xpedite.support.CommonUtils;

import java.util.Date;

public class EachOrder extends AppCompatActivity {

    private UserRequest userRequest;
    private UserRateDetail userRateDetail;
    private Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_order);

        Intent intent = getIntent();
        userRequest = intent.getParcelableExtra("userRequest");
        userRateDetail = intent.getParcelableExtra("userRateDetail");
        seller = intent.getParcelableExtra("seller");

        populateInformationForOrder();

    }

    private void populateInformationForOrder(){

        TextView textViewName = ((TextView) findViewById(R.id.textView_each_orderId));
        String requestIDText = "Request Id :   "+userRequest.getRequestId();
        textViewName.setText(requestIDText);

        TextView textViewSmallTyreCount = ((TextView) findViewById(R.id.textView_each_order_small));
        int smallTyreCount = userRequest.getSmallTyreCount();
        textViewSmallTyreCount.setText(String.valueOf(smallTyreCount));

        TextView textViewMediumTyreCount = ((TextView) findViewById(R.id.textView_each_order_med));
        int mediumTyreCount = userRequest.getMediumTyreCount();
        textViewMediumTyreCount.setText(String.valueOf(mediumTyreCount));

        TextView textViewLargeTyreCount = ((TextView) findViewById(R.id.textView_each_order_large));
        int largeTyreCount = userRequest.getLargeTyreCount();
        textViewLargeTyreCount.setText(String.valueOf(largeTyreCount));

        TextView textViewPickupUser = ((TextView) findViewById(R.id.textView_each_order_pickup_user));
        String pickupUserTextString = "Name : "+userRequest.getPickupUser();
        textViewPickupUser.setText(pickupUserTextString);

        TextView textViewPhoneNumber = ((TextView) findViewById(R.id.textView_each_order_pickup_phone));
        textViewPhoneNumber.setText(String.valueOf("Phone Number :"+userRequest.getPickupUserMobileNumber()));

        TextView textViewAddress = ((TextView) findViewById(R.id.textView_each_order_address));
        String addressText = "Address :"+ userRequest.getAddress();
        textViewAddress.setText(addressText);

        int smallTyrePrice = userRateDetail.getSmallTyrePrice();
        int medTyrePrice = userRateDetail.getMediumTyrePrice();
        int largeTyrePrice = userRateDetail.getLargeTyrePrice();

        int amount = smallTyreCount*smallTyrePrice + mediumTyreCount*medTyrePrice + largeTyreCount*largeTyrePrice;
        TextView textViewAmount = ((TextView) findViewById(R.id.textView_request_completed_status_note));
        String amountText = "Amount :"+String.valueOf(amount);
        textViewAmount.setText(amountText);

        TextView textViewStatus = ((TextView) findViewById(R.id.textView_each_order_status));
        String statusText = "Status :    "+userRequest.getPaymentStatus();
        textViewStatus.setText(statusText);

        TextView textViewPickupReqDate = ((TextView) findViewById(R.id.textView_each_order_pickup_requested_date));
        textViewPickupReqDate.setText(CommonUtils.getDate(userRequest.getPickupRequestDate()));

        TextView textViewPickupCollectedDate = ((TextView) findViewById(R.id.textView_each_order_pickup_collected_date));
        Date pickUpCollectedDate = userRequest.getPickupCollectedDate();
        if(pickUpCollectedDate!=null){
            textViewPickupCollectedDate.setText(CommonUtils.getDate(pickUpCollectedDate));
        }else{
            textViewPickupCollectedDate.setText("");
        }

        Button cancelRequestButton = ((Button) findViewById(R.id.button_each_order_cancel_order));

        Date date = new Date();
        long timeInLong = date.getTime();
        long pickupReqDateInLong = userRequest.getPickupRequestDate().getTime();
        long diff = timeInLong - pickupReqDateInLong;
        long diffHours = diff / (60 * 60 * 1000);

        if(diffHours > 24){
            cancelRequestButton.setEnabled(false);
            cancelRequestButton.setBackgroundColor(Color.LTGRAY);
        }

        String status = userRequest.getPaymentStatus();
        if("PAID".equalsIgnoreCase(status) || "CANCELLED".equalsIgnoreCase(status)){
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_each_order_buttons_wrap);
            TextView textView = (TextView) findViewById(R.id.textView_each_order_cancel_note);

            linearLayout.removeAllViews();
            textView.setText("");
        }

    }

    public void goToConfirmTyreCount(View view){

        String reqId = userRequest.getRequestId();

        Intent ob = new Intent(EachOrder.this, TyreCountFinalDetail.class);
        ob.putExtra("seller", seller);
        ob.putExtra("requestId", String.valueOf(reqId));
        startActivity(ob);

    }

    public void cancelRequest(View view){

        CancelRequestPopUp cancelRequestPopUp = new CancelRequestPopUp();
        Bundle requestBundle = new Bundle();
        requestBundle.putString("lastRequest", "cancelled");
        requestBundle.putParcelable("seller", seller);
        requestBundle.putParcelable("userRequest", userRequest);
        cancelRequestPopUp.setArguments(requestBundle);
        cancelRequestPopUp.show(getFragmentManager(), "CancelRequestPopUp");

    }
}
