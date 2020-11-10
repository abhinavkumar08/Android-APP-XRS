package com.xpedite.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Address;
import com.xpedite.domain.UserStatus;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;
import com.xpedite.support.ValidationUtil;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SignUpActivity extends AppCompatActivity {

    private JSONObject inputBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private boolean prepareInputJSON() {

        inputBody = new JSONObject();
        String name = ((EditText) findViewById(R.id.new_pickup_name)).getText().toString();

        if (ValidationUtil.isFieldBlank(name)) {
            Toast.makeText(SignUpActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }

       String password = ((EditText) findViewById(R.id.new_pwd)).getText().toString();
        if (ValidationUtil.isFieldBlank(password) || password.length()<6) {
            Toast.makeText(SignUpActivity.this, "Password Should be greater than equal to 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        String phoneNumberString = ((EditText) findViewById(R.id.new_pickup_phone)).getText().toString();
        if (!ValidationUtil.isValidMobile(phoneNumberString)) {
            Toast.makeText(SignUpActivity.this, "Enter Valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        long phoneNumber = Long.parseLong(phoneNumberString);

       String shopName = ((EditText) findViewById(R.id.new_pickup_shop)).getText().toString();
        if (ValidationUtil.isFieldBlank(shopName)) {
            Toast.makeText(SignUpActivity.this, "Enter shopName", Toast.LENGTH_SHORT).show();
            return false;
        }

        String street = ((EditText) findViewById(R.id.new_pickup_street)).getText().toString();
        if (ValidationUtil.isFieldBlank(street)) {
            Toast.makeText(SignUpActivity.this, "Enter street", Toast.LENGTH_SHORT).show();
            return false;
        }

        String landmark = ((EditText) findViewById(R.id.new_pickup_landmark)).getText().toString();

       String area = ((EditText) findViewById(R.id.new_pickup_area)).getText().toString();
        if (ValidationUtil.isFieldBlank(area)) {
            Toast.makeText(SignUpActivity.this, "Enter area", Toast.LENGTH_SHORT).show();
            return false;
        }

       String city = ((EditText) findViewById(R.id.new_pickup_city)).getText().toString();
        if (ValidationUtil.isFieldBlank(city)) {
            Toast.makeText(SignUpActivity.this, "Enter city", Toast.LENGTH_SHORT).show();
            return false;
        }

       String state = ((EditText) findViewById(R.id.new_pickup_state)).getText().toString();
        if (ValidationUtil.isFieldBlank(state)) {
            Toast.makeText(SignUpActivity.this, "Enter state", Toast.LENGTH_SHORT).show();
            return false;
        }

        String pinCodeEditString = ((EditText) findViewById(R.id.new_pinCode)).getText().toString();
        if (!ValidationUtil.isPinCodeValid(pinCodeEditString)) {
            Toast.makeText(SignUpActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
            return false;
        }

       long pincode = Long.parseLong(pinCodeEditString);


        Address address = new Address();
        address.setPlotNumber(shopName);
        address.setMobileNumber(phoneNumber);
        address.setStreet(street);
        address.setLandmark(landmark);
        address.setArea(area);
        address.setCity(city);
        address.setState(state);
        address.setPinCode(pincode);

        try {

            inputBody.put("name", name);
            inputBody.put("phoneNumber", phoneNumber);
            inputBody.put("password", password);

            JSONArray addressArray = new JSONArray();
            JSONObject addressObj = new JSONObject();
            addressObj.put("mobileNumber", address.getMobileNumber());
            addressObj.put("plotNumber", address.getPlotNumber());
            addressObj.put("area", address.getArea());
            addressObj.put("city", address.getCity());
            addressObj.put("pinCode", address.getPinCode());
            addressObj.put("state", address.getState());
            addressObj.put("street", address.getStreet());
            addressObj.put("landmark", address.getLandmark());
            addressArray.put(addressObj);

            inputBody.put("listOfAddresses", addressArray);

        } catch (JSONException e) {
            Log.e("Error occurred while creating payload json for sign up activity", e.getMessage());
            displayErrorPage();
        }

        return true;
    }


    public void registerUser(View view) {

        boolean isInputJSONValid =   prepareInputJSON();

        if(isInputJSONValid) {

            try{

            String signUpResponse = new BackgroundUtil(RestEndpointURLs.SIGN_UP, inputBody, "POST").execute().get();
            if (signUpResponse != null) {

                    ObjectMapper mapper = new ObjectMapper();
                    UserStatus registerStatusObj = mapper.readValue(signUpResponse, UserStatus.class);
                    String registerStatus = registerStatusObj.getStatus();

                switch (registerStatus){

                    case "alreadyRegistered" :
                        Toast.makeText(getApplicationContext(), "User is Already Registered", Toast.LENGTH_LONG).show();
                        break;

                    case "registered" :
                        Toast.makeText(getApplicationContext(), "User Registered Successfully.", Toast.LENGTH_LONG).show();
                        Intent ob = new Intent(SignUpActivity.this, SignInActivity.class);
                        startActivity(ob);
                        break;

                    case "phoneRegisteredAsAlternateAddress" :
                        Toast.makeText(getApplicationContext(), "Phone number is alreay registered under a primary account", Toast.LENGTH_LONG).show();
                        break;

                    default:

                        displayErrorPage();
                        break;

                }

            } else {
                Log.e("Error Occurred while trying to register user", "No response from server");
                displayErrorPage();
            }

            }catch (IOException e) {
                Log.e("Error occurred while trying to register user", e.getMessage());
                displayErrorPage();
            } catch (InterruptedException e) {
            Log.e("Background thread interrupted while attempting to sing up user", e.getMessage());
                displayErrorPage();
            } catch (ExecutionException e) {
            Log.e("Execution exception occurred while attempting to sing up user", e.getMessage());
                displayErrorPage();
            }

        }

    }


}


