package com.xpedite.activity.address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.request.PickupRequestActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by abhinkum on 12/7/17.
 * Pop up for delete address
 */


public class DeletePickUpAddressPopup extends DialogFragment {

    private JSONObject inputBody;
    private TyreSize tyreDetails;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle argumentBundle = getArguments();

        if(argumentBundle!=null){

            String address = argumentBundle.getString("textInfo");

            final Seller seller = argumentBundle.getParcelable("seller");
            tyreDetails = argumentBundle.getParcelable("tyreCountDetails");
            final String amount = argumentBundle.getString("amount");

            if(address!=null && seller!=null && tyreDetails!=null){

                inputBody = new JSONObject();

                address = address.trim().replaceAll(" ", "").replaceAll("\n", "");


                    String addressArray[] = address.split(",");
                if(addressArray.length==9){
                    String name = addressArray[0];
                    String mobileNumber = addressArray[1];
                    String plotNumber = addressArray[2];
                    String street = addressArray[3];
                    String area = addressArray[4];
                    String landmark = addressArray[5];
                    String city = addressArray[6];
                    String state = addressArray[7];
                    String pincode = addressArray[8];

                    try {
                        inputBody.put("primaryUserMobileNumber", seller.getPhoneNumber());
                        inputBody.put("pickUpUser", name);
                        inputBody.put("pickUpUsermobileNumber", mobileNumber);
                        inputBody.put("plotNumber", plotNumber);
                        inputBody.put("street", street);
                        inputBody.put("area", area);
                        inputBody.put("landmark", landmark);
                        inputBody.put("city", city);
                        inputBody.put("state", state);
                        inputBody.put("pincode", pincode);
                    } catch (JSONException e) {
                        Log.e("Error occurred while to trying to form json payload ", e.getMessage());
                        displayErrorPage();
                    }

                }





            }


            builder.setMessage("Are you sure you want to delete this address")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            try {
                                String responseBody = new BackgroundUtil(RestEndpointURLs.DELETE_ADDRESS, inputBody, "POST").execute().get();

                                Intent i = new Intent(getActivity().getApplicationContext(), PickupRequestActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("seller", seller);
                                i.putExtra("tyreCountDetails", tyreDetails);
                                i.putExtra("amount", amount);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);


                            } catch (InterruptedException e) {
                                Log.e("Network connection iterrupted when trying to delete address", e.getMessage());
                                displayErrorPage();
                            } catch (ExecutionException e) {
                                Log.e("Execution exception occurred trying to delete address", e.getMessage());
                                displayErrorPage();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
        }

        return builder.create();
    }

    private void displayErrorPage(){

        Intent i = new Intent(getActivity(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
