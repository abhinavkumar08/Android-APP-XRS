package com.xpedite.activity.count;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.xpedite.R;
import com.xpedite.domain.Seller;
import com.xpedite.domain.TyreSize;

public class TyreCountFinalDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyre_count_final_detail);

    }

    public void submitFinalCount(View view) {

            Intent intent = getIntent();

        if(intent!=null){

            String requestId = intent.getStringExtra("requestId");
            Seller seller = intent.getParcelableExtra("seller");
            TyreSize tyreSize = new TyreSize();
            String smallTyreCount = ((EditText) findViewById(R.id.editText_small_tyre_count_final)).getText().toString();
            String medTyreCount = ((EditText) findViewById(R.id.editText_med_tyre_count_final)).getText().toString();
            String largeTyreCount = ((EditText) findViewById(R.id.editText_large_tyre_count_final)).getText().toString();
            tyreSize.setSmallTyreCount(Integer.parseInt(smallTyreCount));
            tyreSize.setMediumTyreCount(Integer.parseInt(medTyreCount));
            tyreSize.setLargeTyreCount(Integer.parseInt(largeTyreCount));


            Intent i = new Intent(getApplicationContext(), ConfirmTyreCountAndAmountActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("seller", seller);
            i.putExtra("tyreSize", tyreSize);
            i.putExtra("requestId", requestId);
            startActivity(i);

        }

    }
}
