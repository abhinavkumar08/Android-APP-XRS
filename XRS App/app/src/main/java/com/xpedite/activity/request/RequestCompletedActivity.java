package com.xpedite.activity.request;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xpedite.R;
import com.xpedite.activity.login.UserProfileActivity;
import com.xpedite.domain.Seller;

public class RequestCompletedActivity extends AppCompatActivity {

    private Seller seller;

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent goToUserProfile = new Intent(getApplicationContext(), UserProfileActivity.class);
        goToUserProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        goToUserProfile.putExtra("seller", seller);
        startActivity(goToUserProfile);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_completed);
        Intent intent = getIntent();
        seller = intent.getParcelableExtra("seller");
        String lastReq = intent.getStringExtra("lastRequest");

        TextView statusNote = (TextView) findViewById(R.id.textView_request_completed_status_note);
        TextView requestCompletedNote = (TextView) findViewById(R.id.textView_request_completed_note);

        if("cancelled".equalsIgnoreCase(lastReq)){

            statusNote.setText(getString(R.string.cancelled));
            requestCompletedNote.setText(R.string.req_cancelled_goto_home);
        }else if("requestUpdated".equals(lastReq)){

            statusNote.setText(getString(R.string.submission_successful));
            requestCompletedNote.setText(getString(R.string.submission_thank_you_message));

        }
    }


    public void gotoHome(View view) {

        Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("seller", seller);
        startActivity(i);
    }
}
