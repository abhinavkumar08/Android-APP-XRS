package com.xpedite.activity.address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.request.SubmitRequestActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abhinav kumar  on 10/6/17.
 * Confirm Pickup Address After pop up
 *
 */

public class ConfirmPickUpAddressPopUp extends DialogFragment {

    private JSONObject inputBodyForUserRequest;
    private Seller seller;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle argumentBundle = getArguments();

        if(argumentBundle!=null){
            final String address = argumentBundle.getString("textInfo");
            final String amount = argumentBundle.getString("amount");
            String finalAddress = null;
            String pickupUserMobileNumber = null;
                    if(address!=null) {
                         finalAddress = address.trim().replaceAll(" ","").replaceAll("\n","");
                    }

            seller = argumentBundle.getParcelable("seller");
            TyreSize tyreDetails = argumentBundle.getParcelable("tyreCountDetails");
            if(finalAddress!=null){
                String addressArray[] = finalAddress.split(",");

                if(addressArray.length>=2){
                    pickupUserMobileNumber = addressArray[1];
                }
            }


            inputBodyForUserRequest = new JSONObject();
            try {
                inputBodyForUserRequest.put("pickupUserMobileNumber", pickupUserMobileNumber);
                if(tyreDetails!=null){
                    inputBodyForUserRequest.put("smallTyreCount" , tyreDetails.getSmallTyreCount());
                    inputBodyForUserRequest.put("mediumTyreCount" , tyreDetails.getMediumTyreCount());
                    inputBodyForUserRequest.put("largeTyreCount" , tyreDetails.getLargeTyreCount());
                    inputBodyForUserRequest.put("amountPaid" , amount);
                }


            } catch (JSONException e) {
                Log.e("Error occurred while trying to form json paylod ", e.getMessage());
                displayErrorPage();
            }

            final String finalAddressForPickup = finalAddress;
            builder.setMessage("Are you sure")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Intent ob = new Intent(getActivity(), SubmitRequestActivity.class);
                            ob.putExtra("seller" , seller);
                            ob.putExtra("inputBodyForUserRequest" , inputBodyForUserRequest.toString());
                            ob.putExtra("address", finalAddressForPickup);
                            startActivity(ob);
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