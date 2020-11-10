package com.xpedite.activity.request;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.address.DeletePickUpAddressPopup;
import com.xpedite.activity.address.EditAddressActivity;
import com.xpedite.activity.address.PickupFromNewAddressActivity;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.activity.login.UserProfileActivity;
import com.xpedite.domain.Address;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.activity.address.ConfirmPickUpAddressPopUp;
import com.xpedite.support.RestEndpointURLs;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PickupRequestActivity extends AppCompatActivity {

    private static final int TEXTVIEW = 1;
    private static final int PICKUPBUTTON = 2;
    private static final int EDITBUTTON = 3;
    private static final int DELETEBUTTON = 4;
    private Seller seller = null;
    private TyreSize tyreDetails = null;

    private static final String PICK_UP_FROM_THIS_ADDRESS = "Pick up from this Address";
    private static final String PICK_UP_FROM_NEW_ADDRESS = "Pick up from New Address";
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";



    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent goToUserProfile = new Intent(getApplicationContext(), UserProfileActivity.class);
        goToUserProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        goToUserProfile.putExtra("seller", seller);
        goToUserProfile.putExtra("tyreCountDetails", tyreDetails);
        startActivity(goToUserProfile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_request);

        renderAddressChoice();

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    private List<Address> getSellerListOfAddresses(String phoneNumberUsername) {

        List<Address> listOfAddresses = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String sellerDetailsInJson = new BackgroundUtil(RestEndpointURLs.SELLER + "/" + phoneNumberUsername, null, "GET").execute().get();
            JSONObject responseJSON = new JSONObject(sellerDetailsInJson);

            Object listOfAddressesFromJson = responseJSON.get("listOfAddresses");
            if (!(listOfAddressesFromJson instanceof JSONArray)) {
                JSONArray jArray = new JSONArray();
                jArray.put(listOfAddressesFromJson);
                responseJSON.put("listOfAddresses", jArray);
            }
            Seller seller = mapper.readValue(responseJSON.toString(), Seller.class);
            if(seller!=null){
                listOfAddresses = seller.getListOfAddresses();
            }

        } catch (InterruptedException e) {
            Log.e("Network connection iterrupted when trying to get address", e.getMessage());
            displayErrorPage();
        } catch (ExecutionException e) {
            Log.e("Execution exception occurred trying to get address", e.getMessage());
            displayErrorPage();
        } catch (JSONException e) {
            Log.e("JSONException occurred while trying to get listOfAddress", e.getMessage());
            displayErrorPage();
        } catch (JsonParseException e) {
            Log.e("JSON Parsing Exception occurred while trying to get listOfAddress", e.getMessage());
            displayErrorPage();
        } catch (JsonMappingException e) {
            Log.e("JSON Mapping Exception occurred while trying to get listOfAddress", e.getMessage());
            displayErrorPage();
        } catch (IOException e) {
           Log.e("IOException occurred while trying to get listOfAddress ", e.getMessage());
            displayErrorPage();
        }
        return listOfAddresses;
    }


    private void renderAddressChoice() {

        Intent intent = getIntent();
        seller = intent.getParcelableExtra("seller");
        tyreDetails = intent.getParcelableExtra("tyreCountDetails");
        String phoneNumber = seller.getPhoneNumber();
        final String amount = intent.getStringExtra("amount");

        List<Address> listOfAddresses = getSellerListOfAddresses(phoneNumber);
        Collections.sort(listOfAddresses, new Comparator<Address>() {
            @Override
            public int compare(Address address1, Address address2) {
                return address2.getIsPrimaryUser().charAt(0) - (address1.getIsPrimaryUser().charAt(0));
            }
        });

        TableLayout tableLayout = (TableLayout) findViewById(R.id.pickup_request_address_layout);

        for (int i = 0; i < listOfAddresses.size(); i++) {
            Address address = listOfAddresses.get(i);

            final TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.setMargins(1, 1, 1, 1);
            row.setLayoutParams(lp);
            //row.setBackgroundColor(Color.parseColor("#FFFFFF"));
            row.setBackgroundResource(R.drawable.tablerowborder);

            LinearLayout firstLevelLinearLayout = new LinearLayout(this);
            firstLevelLinearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
            firstLevelLinearLayout.setOrientation(TableRow.VERTICAL);


            LinearLayout secondLevelLinearLayout = new LinearLayout(this);
            secondLevelLinearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            secondLevelLinearLayout.setOrientation(TableRow.HORIZONTAL);

            TextView textView = new TextView(this);
            textView.setTextSize(20);
            //textView.setBackgroundColor(Color.parseColor("#dcdcdc"));
            textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setText(address.toString());
            textView.setPadding(20, 20, 20, 20);
            textView.setGravity(Gravity.CENTER);
            textView.setMaxWidth(3000);
            textView.setId(TEXTVIEW * 10 + i);

           /* ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams imageViewlayout = new LinearLayout.LayoutParams(5, 25, 1);
            imageView.setLayoutParams(imageViewlayout);
            imageView.setBackgroundResource(R.drawable.unchecked);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setId(i);*/

            secondLevelLinearLayout.addView(textView);
            //secondLevelLinearLayout.addView(imageView);

            Button button = new Button(this);

            button.setText(PICK_UP_FROM_THIS_ADDRESS);
            button.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            button.setId(PICKUPBUTTON * 10 + i);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int buttonId = v.getId();
                    int rowId = buttonId % 10;
                    int textId = TEXTVIEW * 10 + rowId;
                    String textInfo = ((TextView) findViewById(textId)).getText().toString();
                    ConfirmPickUpAddressPopUp confirmPickUpAddressPopUp = new ConfirmPickUpAddressPopUp();
                    Bundle requestBundle = new Bundle();
                    requestBundle.putString("textInfo", textInfo);
                    requestBundle.putString("amount", amount);
                    requestBundle.putParcelable("seller", seller);
                    requestBundle.putParcelable("tyreCountDetails", tyreDetails);
                    confirmPickUpAddressPopUp.setArguments(requestBundle);
                    confirmPickUpAddressPopUp.show(getFragmentManager(), "ConfirmPickUpAddressPopUp");

                }
            });

            LinearLayout thirdLevelLinearLayout = new LinearLayout(this);
            thirdLevelLinearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
            thirdLevelLinearLayout.setOrientation(TableRow.HORIZONTAL);


            Button buttonEdit = new Button(this);
            buttonEdit.setText(EDIT);
            buttonEdit.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
            buttonEdit.setId(EDITBUTTON * 10 + i);
            buttonEdit.setBackgroundColor(Color.rgb(143, 188, 143));

            buttonEdit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int buttonId = v.getId();
                    int rowId = buttonId % 10;
                    int textId = TEXTVIEW * 10 + rowId;


                    String textInfo = ((TextView) findViewById(textId)).getText().toString();

                    Intent ob = new Intent(PickupRequestActivity.this, EditAddressActivity.class);
                    ob.putExtra("id", String.valueOf(textId));
                    ob.putExtra("seller", seller);
                    ob.putExtra("textInfo", textInfo);
                    ob.putExtra("tyreCountDetails", tyreDetails);
                    ob.putExtra("amount", amount);

                    startActivity(ob);
                }
            });


            Button buttonDelete = new Button(this);
            buttonDelete.setText(DELETE);
            buttonDelete.setLayoutParams(new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
            buttonDelete.setId(DELETEBUTTON * 10 + i);
            buttonDelete.setBackgroundColor(Color.rgb(255, 160, 122));
            // buttonDelete.setTextColor(Color.WHITE);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    int buttonId = v.getId();
                    int rowId = buttonId % 10;
                    int textId = TEXTVIEW * 10 + rowId;


                    String textInfo = ((TextView) findViewById(textId)).getText().toString();

                    DeletePickUpAddressPopup deletePickUpAddressPopup = new DeletePickUpAddressPopup();
                    Bundle requestBundle = new Bundle();
                    requestBundle.putString("textInfo", textInfo);
                    requestBundle.putString("amount", amount);
                    requestBundle.putParcelable("seller", seller);
                    requestBundle.putInt("index", rowId);
                    requestBundle.putParcelable("tyreCountDetails", tyreDetails);
                    deletePickUpAddressPopup.setArguments(requestBundle);
                    //confirmPickUpAddressPopUp.show(getSupportFragmentManager(), "ConfirmPickUpAddressPopUp");
                    deletePickUpAddressPopup.show(getFragmentManager(), "DeletePickUpAddressPopup");

                }
            });

            thirdLevelLinearLayout.addView(buttonEdit);
            thirdLevelLinearLayout.addView(buttonDelete);
            thirdLevelLinearLayout.setPadding(20, 0, 20, 40);

            firstLevelLinearLayout.addView(secondLevelLinearLayout);
            firstLevelLinearLayout.addView(button);
            firstLevelLinearLayout.addView(thirdLevelLinearLayout);

            row.addView(firstLevelLinearLayout);
            tableLayout.addView(row);

        }

        Button deletePrimaryAddress = (Button) findViewById(4*10);
        deletePrimaryAddress.setEnabled(false);

        TableRow finalRow = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        lp.setMargins(1, 1, 1, 1);
        finalRow.setLayoutParams(lp);
        finalRow.setBackgroundResource(R.drawable.tablerowborder);

        LinearLayout finalRowFirstLinearLayout = new LinearLayout(this);
        finalRowFirstLinearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1));
        finalRowFirstLinearLayout.setOrientation(TableRow.VERTICAL);

        LinearLayout finalRowsecondLinearLayout = new LinearLayout(this);
        finalRowsecondLinearLayout.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        finalRowsecondLinearLayout.setOrientation(TableRow.HORIZONTAL);

        TextView finalRowTextView = new TextView(this);
        //textView.setTextSize(20);
        //textView.setBackgroundColor(Color.parseColor("#dcdcdc"));
        finalRowTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        finalRowTextView.setTextColor(Color.parseColor("#000000"));
        finalRowTextView.setText(PICK_UP_FROM_NEW_ADDRESS);
        finalRowTextView.setPadding(20, 40, 20, 40);
        finalRowTextView.setGravity(Gravity.CENTER);
        finalRowTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent ob = new Intent(PickupRequestActivity.this, PickupFromNewAddressActivity.class);

                ob.putExtra("seller", seller);
                ob.putExtra("tyreCountDetails", tyreDetails);
                ob.putExtra("amount", amount);
                startActivity(ob);

            }
        });

        finalRowsecondLinearLayout.addView(finalRowTextView);
        finalRowFirstLinearLayout.addView(finalRowsecondLinearLayout);
        finalRow.addView(finalRowFirstLinearLayout);

        tableLayout.addView(finalRow);

    }

}
