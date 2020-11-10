package com.xpedite.activity.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xpedite.R;
import com.xpedite.activity.MainActivity;
import com.xpedite.activity.error.ErrorActivity;
import com.xpedite.support.BackgroundUtil;
import com.xpedite.support.RestEndpointURLs;
import com.xpedite.support.ValidationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
    }


    public void updatePassword(View view){

        Intent intent = getIntent();
        String phoneNumberString = intent.getStringExtra("mobileNumber");

        String newpassword = ((EditText) findViewById(R.id.editText_new_password)).getText().toString();
        if (ValidationUtil.isFieldBlank(newpassword) || newpassword.length()<6) {
            Toast.makeText(ResetPasswordActivity.this, "Password Should be greater than equal to 6 characters", Toast.LENGTH_SHORT).show();
            return;

        }

        String reEnterPassword = ((EditText) findViewById(R.id.editText_re_enter_password)).getText().toString();

        if(reEnterPassword.equals(newpassword)){
            JSONObject resetPasswordJson = new JSONObject();
            try {
                resetPasswordJson.put("mobileNumber" ,  phoneNumberString);
                resetPasswordJson.put("newPassword" ,  reEnterPassword);

                String status = new BackgroundUtil(RestEndpointURLs.RESET_PASSWORD, resetPasswordJson, "POST").execute().get();
                if("PasswordUpdated".equals(status)){
                    Toast.makeText(ResetPasswordActivity.this, "Password Updated, Please login with the new password .", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }


            } catch (JSONException e) {
                Log.e("Error occurred while to trying to form json payload to reset password", e.getMessage());
                displayErrorPage();
            } catch (InterruptedException e) {
                Log.e("Background thread interrupted while attempting to reset password", e.getMessage());
                displayErrorPage();
            } catch (ExecutionException e) {
                Log.e("Execution exception occurred while attempting to reset password", e.getMessage());
                displayErrorPage();
            }
        }else{
            Toast.makeText(ResetPasswordActivity.this, "Confirm Password does not match", Toast.LENGTH_SHORT).show();
        }

    }

    private void displayErrorPage(){

        Intent i = new Intent(getApplicationContext(), ErrorActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
