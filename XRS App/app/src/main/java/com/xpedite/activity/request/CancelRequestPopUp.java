package com.xpedite.activity.request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.UserRequest;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;

import java.util.concurrent.ExecutionException;

/**
 * Created by abhinkum on 1/6/19.
 * Confirm the cancellation through pop up
 */

public class CancelRequestPopUp extends DialogFragment {

    private Seller seller;
    private UserRequest userRequest;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        seller = getArguments().getParcelable("seller");
        userRequest = getArguments().getParcelable("userRequest");

        builder.setMessage("Are you sure you want to Cancel the request")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String reqId = userRequest.getRequestId();
                        try {
                            String response = new BackgroundUtil(RestEndpointURLs.CANCEL_REQUEST+"/"+reqId, null, "PUT").execute().get();

                            Intent ob = new Intent(getActivity(), RequestCompletedActivity.class);
                            ob.putExtra("seller", seller);
                            ob.putExtra("lastRequest", "cancelled");
                            startActivity(ob);
                        } catch (InterruptedException e) {
                            Log.e("Background thread interrupted while attempting to cancel request", e.getMessage());
                            displayErrorPage();
                        } catch (ExecutionException e) {
                            Log.e("Execution exception occurred while attempting to cancel request", e.getMessage());
                            displayErrorPage();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void displayErrorPage(){

        Intent i = new Intent(getActivity(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
