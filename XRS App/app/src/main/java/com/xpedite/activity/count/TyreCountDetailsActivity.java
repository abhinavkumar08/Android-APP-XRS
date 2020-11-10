package com.xpedite.activity.count;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.request.PickupRequestActivity;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;
import com.xpedite.domain.UserRateDetail;
import com.xpedite.support.ValidationUtil;

public class TyreCountDetailsActivity extends AppCompatActivity {

    private Seller seller = null;
    private TyreSize tyreSize = null;
    private UserRateDetail userRateDetail;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyre_count_details);
        Intent intent = getIntent();
        if(intent!=null){
            seller = intent.getParcelableExtra("seller");
            userRateDetail = intent.getParcelableExtra("userRateDetail");
            if(userRateDetail!=null){
                displayRate();
            }

        }


    }

    private void displayRate(){

        TextView smallTyreRateTextView = ((TextView)findViewById(R.id.textView_tyre_count_rate_display_small));
        TextView mediumTyreRateTextView = ((TextView)findViewById(R.id.textView_tyre_count_rate_display_med));
        TextView largeTyreRateTextView = ((TextView)findViewById(R.id.textView_tyre_count_rate_display_large));

        int smallTyreRate = userRateDetail.getSmallTyrePrice();
        int mediumTyrePrice = userRateDetail.getMediumTyrePrice();
        int largeTyrePrice = userRateDetail.getLargeTyrePrice();

        smallTyreRateTextView.setText("@ Rs."+smallTyreRate+"/Tyre");
        mediumTyreRateTextView.setText("@ Rs."+mediumTyrePrice+"/Tyre");
        largeTyreRateTextView.setText("@ Rs."+largeTyrePrice+"/Tyre");
    }

    private boolean fetchTyreCountDetails() {

        String smallTyreCountString = ((EditText)findViewById(R.id.editText_small_tyre_count)).getText().toString();
        if(!ValidationUtil.isTyreCountValid(smallTyreCountString))
        {
            Toast.makeText(TyreCountDetailsActivity.this, "Enter valid Small Tyre Count", Toast.LENGTH_SHORT).show();
            return false;
        }
        int smallCount = Integer.parseInt(smallTyreCountString);

        String mediumTyreCountString = ((EditText)findViewById(R.id.editText_med_tyre_count)).getText().toString();
        if(!ValidationUtil.isTyreCountValid(mediumTyreCountString))
        {
            Toast.makeText(TyreCountDetailsActivity.this, "Enter valid Medium Tyre Count", Toast.LENGTH_SHORT).show();
            return false;
        }
        int mediumCount = Integer.parseInt(mediumTyreCountString);

        String largeTyreCountString = ((EditText)findViewById(R.id.editText_large_tyre_count)).getText().toString();
        if(!ValidationUtil.isTyreCountValid(largeTyreCountString))
        {
            Toast.makeText(TyreCountDetailsActivity.this, "Enter valid Large Tyre Count", Toast.LENGTH_SHORT).show();
            return false;
        }
        int largeCount = Integer.parseInt(largeTyreCountString);

        int smallTyreRate = userRateDetail.getSmallTyrePrice();
        int mediumTyrePrice = userRateDetail.getMediumTyrePrice();
        int largeTyrePrice = userRateDetail.getLargeTyrePrice();
        int sum = smallCount + mediumCount + largeCount;
        amount = smallCount*smallTyreRate + mediumCount*mediumTyrePrice+largeCount*largeTyrePrice;

        if(sum==0){
            Toast.makeText(TyreCountDetailsActivity.this, "Total Tyre Count Canot be Zero", Toast.LENGTH_SHORT).show();
            return false;
        }

        tyreSize = new TyreSize();
        tyreSize.setSmallTyreCount(smallCount);
        tyreSize.setMediumTyreCount(mediumCount);
        tyreSize.setLargeTyreCount(largeCount);

        return true;
    }

    public void chooseAddress(View view) {

       boolean isInputValid = fetchTyreCountDetails();
        if(isInputValid) {
            Intent ob = new Intent(TyreCountDetailsActivity.this, PickupRequestActivity.class);
            ob.putExtra("seller" , seller);
            ob.putExtra("tyreCountDetails" , tyreSize);
            ob.putExtra("amount", String.valueOf(amount));
            startActivity(ob);
        }
    }
}
