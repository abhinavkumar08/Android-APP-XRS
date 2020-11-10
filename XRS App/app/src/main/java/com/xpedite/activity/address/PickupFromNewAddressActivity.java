package com.xpedite.activity.address;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.login.SignUpActivity;
import com.xpedite.activity.request.PickupRequestActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;
import com.xpedite.support.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class PickupFromNewAddressActivity extends AppCompatActivity {

    private Seller seller;
    private TyreSize tyreDetails;
    private JSONObject inputBodyForSellerAltAddress;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_from_new_address);
        Intent intent = getIntent();
        seller = intent.getParcelableExtra("seller");
        tyreDetails = intent.getParcelableExtra("tyreCountDetails");
        amount = intent.getStringExtra("amount");

    }

    private boolean prepareInputJSON()
    {
        inputBodyForSellerAltAddress = new JSONObject();
        JSONObject inputBodyForUserRequest = new JSONObject();

        //Validate name(blank or not)
        String name = ((EditText)findViewById(R.id.edit_pickup_name)).getText().toString();
        if(ValidationUtil.isFieldBlank(name)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Name cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate phone(valid phone number)
        String phoneNumString = ((EditText)findViewById(R.id.edit_pickup_phone)).getText().toString();
        if(!ValidationUtil.isValidMobile(phoneNumString)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        long phoneNumber = Long.parseLong(phoneNumString);

        //Validate shop name(empty or not)
        String shopName = ((EditText)findViewById(R.id.edit_pickup_shop)).getText().toString();
        if(ValidationUtil.isFieldBlank(shopName)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Shop Name cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate street name(empty or not)
        String street = ((EditText)findViewById(R.id.edit_pickup_street)).getText().toString();
        if(ValidationUtil.isFieldBlank(street)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Street Name cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate area(blank or not)
        String area = ((EditText)findViewById(R.id.edit_pickup_area)).getText().toString();
        if(ValidationUtil.isFieldBlank(area)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Area cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate landmark(empty or not)
        String landmark = ((EditText)findViewById(R.id.edit_pickup_landmark)).getText().toString();
        if(ValidationUtil.isFieldBlank(landmark)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Landmark cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate city(blank or not)
        String city = ((EditText)findViewById(R.id.edit_pickup_city)).getText().toString();
        if(ValidationUtil.isFieldBlank(city)){
            Toast.makeText(PickupFromNewAddressActivity.this, "City cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate state(blank or not)
        String state = ((EditText)findViewById(R.id.edit_pickup_state)).getText().toString();
        if(ValidationUtil.isFieldBlank(state)){
            Toast.makeText(PickupFromNewAddressActivity.this, "State cannot be left blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validate pincode(blank or not)
        String pinCodeString = ((EditText)findViewById(R.id.new_pickup_pincode)).getText().toString();
        if(!ValidationUtil.isPinCodeValid(pinCodeString)){
            Toast.makeText(PickupFromNewAddressActivity.this, "Pincode is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        long pincode = Long.parseLong(pinCodeString);

        if(seller!=null && tyreDetails!=null){

            try {

                inputBodyForSellerAltAddress.put("primaryUserMobileNumber", seller.getPhoneNumber());
                inputBodyForSellerAltAddress.put("pickUpUser", name);
                inputBodyForSellerAltAddress.put("pickUpUsermobileNumber", phoneNumber);
                inputBodyForSellerAltAddress.put("plotNumber", shopName);
                inputBodyForSellerAltAddress.put("street", street);
                inputBodyForSellerAltAddress.put("area", area);
                inputBodyForSellerAltAddress.put("landmark", landmark);
                inputBodyForSellerAltAddress.put("city", city);
                inputBodyForSellerAltAddress.put("state", state);
                inputBodyForSellerAltAddress.put("pincode", pincode);


                inputBodyForUserRequest.put("pickUpUserMobileNumber", phoneNumber);
                inputBodyForUserRequest.put("smallTyreCount" , tyreDetails.getSmallTyreCount());
                inputBodyForUserRequest.put("mediumTyreCount" , tyreDetails.getMediumTyreCount());
                inputBodyForUserRequest.put("largeTyreCount" , tyreDetails.getLargeTyreCount());
                inputBodyForUserRequest.put("amountPaid" , -1);

            }

            catch (JSONException e) {
                Log.e("Error occurred when trying to form request payload for new Address", e.getMessage());
                displayErrorPage();
            }
        }

    return true;
    }


    public void registerUser(View view)
    {
       boolean isInputValid =  prepareInputJSON();
        if(isInputValid)
        {
            try{
                String responseBody= new BackgroundUtil(RestEndpointURLs.ADD_ADDRESS,inputBodyForSellerAltAddress, "POST").execute().get();

                if("addressRegistered".equals(responseBody)){

                    Toast.makeText(this, "Address Registered" , Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),PickupRequestActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("seller", seller);
                    i.putExtra("tyreCountDetails" , tyreDetails);
                    i.putExtra("amount", amount);
                    Bundle bundle = new Bundle();
                    i.putExtras(bundle);
                    // Add new Flag to start new Activity
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }else{
                    Toast.makeText(this, responseBody , Toast.LENGTH_SHORT).show();
                }


            } catch (InterruptedException e) {
                Log.e("Network connection iterrupted when trying to register new address" , e.getMessage());
                displayErrorPage();
            } catch (ExecutionException e) {
                Log.e("Execution exception occurred trying to register new address" , e.getMessage());
                displayErrorPage();
            }
        }

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
