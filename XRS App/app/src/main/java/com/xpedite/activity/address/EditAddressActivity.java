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
import com.xpedite.activity.request.PickupRequestActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;
import com.xpedite.support.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class EditAddressActivity extends AppCompatActivity {

    private JSONObject inputBody;
    private String name;
    private String pickupUserMobileNumber;
    private String amount;

    private Seller seller;
    private TyreSize tyreDetails;

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        Intent intent = getIntent();
        if(intent!=null){

            seller = intent.getParcelableExtra("seller");
            tyreDetails = intent.getParcelableExtra("tyreCountDetails");
            amount = intent.getStringExtra("amount");

            String addressInfo = intent.getStringExtra("textInfo");
            if(addressInfo!=null){
                String address = addressInfo.trim().replaceAll(" ", "").replaceAll("\n", "");
                setDefaultAddressFiled(address);
            }
        }

    }

    private void setDefaultAddressFiled(String addressInfo) {

        String id = getIntent().getStringExtra("id");

        String addressInfoArray[] = addressInfo.trim().split(",");
        String pickUpUserName = null;
        String pickUpUserPhoneNumber = null;
        String shopName = null;
        String area = null;
        String city = null;
        String state = null;
        String pincode = null;
        String street = null;
        String landmark = null;

        if (addressInfoArray.length == 9) {
            pickUpUserName = addressInfoArray[0];
            pickUpUserPhoneNumber = addressInfoArray[1];
            shopName = addressInfoArray[2];
            street = addressInfoArray[3];
            area = addressInfoArray[4];
            landmark = addressInfoArray[5];
            city = addressInfoArray[6];
            state = addressInfoArray[7];
            pincode = addressInfoArray[8];
            pickupUserMobileNumber = pickUpUserPhoneNumber;

        }



        EditText editTextPickupUserName = (EditText) findViewById(R.id.edit_pickup_name);
        editTextPickupUserName.setText(pickUpUserName);

        EditText editTextPickupUserPhoneNumber = (EditText) findViewById(R.id.edit_pickup_phone);
        editTextPickupUserPhoneNumber.setText(pickUpUserPhoneNumber);

        EditText editTextShop = (EditText) findViewById(R.id.edit_pickup_shop);
        editTextShop.setText(shopName);

        EditText editTextStreet = (EditText) findViewById(R.id.editText_edit_address_street);
        editTextStreet.setText(street);

        EditText editTextArea = (EditText) findViewById(R.id.edit_pickup_area);
        editTextArea.setText(area);

        EditText editTextLandmark = (EditText) findViewById(R.id.editText_edit_address_landmark);
        editTextLandmark.setText(landmark);

        EditText editTextCity = (EditText) findViewById(R.id.edit_pickup_city);
        editTextCity.setText(city);

        EditText editTextState = (EditText) findViewById(R.id.edit_pickup_state);
        editTextState.setText(state);

        EditText editTextPincode = (EditText) findViewById(R.id.edit_pinCode);
        editTextPincode.setText(pincode);

        if("10".equals(id)){
            editTextPickupUserName.setEnabled(false);
            editTextPickupUserPhoneNumber.setEnabled(false);
        }

    }

    private void prepareInputJSON() {

        inputBody = new JSONObject();
        name = ((EditText) findViewById(R.id.edit_pickup_name)).getText().toString();
        EditText phoneNumEditText = (EditText) findViewById(R.id.edit_pickup_phone);
        long phoneNumber = Long.parseLong(phoneNumEditText.getText().toString());
        String shopName = ((EditText) findViewById(R.id.edit_pickup_shop)).getText().toString();
        String street = ((EditText) findViewById(R.id.editText_edit_address_street)).getText().toString();
        String area = ((EditText) findViewById(R.id.edit_pickup_area)).getText().toString();
        String landmark = ((EditText) findViewById(R.id.editText_edit_address_landmark)).getText().toString();
        String city = ((EditText) findViewById(R.id.edit_pickup_city)).getText().toString();
        String state = ((EditText) findViewById(R.id.edit_pickup_state)).getText().toString();
        EditText pinCodeEditText = ((EditText) findViewById(R.id.edit_pinCode));
        long pincode = Long.parseLong(pinCodeEditText.getText().toString());


        if (name.length() <= 0) {
            Toast.makeText(EditAddressActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
        }
        if (phoneNumEditText.getText().length() <= 0) {
            Toast.makeText(EditAddressActivity.this, "Enter phone Number", Toast.LENGTH_SHORT).show();
        }

        if (ValidationUtil.isFieldBlank(shopName)) {
            Toast.makeText(EditAddressActivity.this, "Enter Shop Name/Number", Toast.LENGTH_SHORT).show();
            return;

        }
        if (ValidationUtil.isFieldBlank(street)) {
            Toast.makeText(EditAddressActivity.this, "Enter Street", Toast.LENGTH_SHORT).show();
            return;

        }

        if (ValidationUtil.isFieldBlank(area)) {
            Toast.makeText(EditAddressActivity.this, "Enter Area", Toast.LENGTH_SHORT).show();
            return;

        }

        if (ValidationUtil.isFieldBlank(landmark)) {
            Toast.makeText(EditAddressActivity.this, "Enter landmark", Toast.LENGTH_SHORT).show();
            return;

        }

        if (ValidationUtil.isFieldBlank(city)) {
            Toast.makeText(EditAddressActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
            return;

        }

        if (ValidationUtil.isFieldBlank(state)) {
            Toast.makeText(EditAddressActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
            return;

        }

        if (!ValidationUtil.isPinCodeValid(String.valueOf(pincode))) {
            Toast.makeText(EditAddressActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
            return;

        }

        try {

            inputBody.put("primaryUserMobileNumber", seller.getPhoneNumber());
            inputBody.put("pickUpUser", name);
            inputBody.put("pickUpUsermobileNumber", phoneNumber);
            inputBody.put("plotNumber", shopName);
            inputBody.put("street", street);
            inputBody.put("area", area);
            inputBody.put("landmark", landmark);
            inputBody.put("city", city);
            inputBody.put("state", state);
            inputBody.put("pincode", pincode);

            inputBody.put("updateAgainstPhoneNumber", pickupUserMobileNumber);

        } catch (JSONException e) {
            Log.e("Error occurred while trying to form json payload ", e.getMessage());
            displayErrorPage();
        }


    }


    public void registerUser(View view) {
        prepareInputJSON();
        try {
            String responseBody = new BackgroundUtil(RestEndpointURLs.EDIT_ADDRESS, inputBody, "POST").execute().get();

            Intent i = new Intent(getApplicationContext(), PickupRequestActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("seller", seller);
            i.putExtra("tyreCountDetails", tyreDetails);
            i.putExtra("amount", amount);
            Bundle bundle = new Bundle();
            i.putExtras(bundle);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        } catch (InterruptedException e) {
            Log.e("Network connection iterrupted when trying to edit address", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred trying to edit address", e.getMessage());
            displayErrorPage();
        }
    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}
