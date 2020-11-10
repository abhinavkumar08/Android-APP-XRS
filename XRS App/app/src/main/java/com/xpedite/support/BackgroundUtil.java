package com.xpedite.support;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by abhinkum on 9/20/17.
 *
 */

public class BackgroundUtil extends AsyncTask<Void, Void, String> {


    private String urlString;
    private JSONObject inputBody;
    private String requestType;

    public BackgroundUtil(String urlString, JSONObject input, String requestType) {
        this.urlString = urlString;
        this.inputBody = input;
        this.requestType = requestType;
    }

    @Override
    protected String doInBackground(Void... voids)  {
        String responseBody = null;
        HttpURLConnection conn=null;

        switch (requestType){

                case "POST":
                    try {
                        conn = ConnectionUtil.post(urlString, inputBody, "POST");
                    } catch (Exception e) {
                        Log.e("Error occurred while trying to make a POST call", e.getMessage());
                    }
                    break;

                case "PUT":
                    try {

                        conn = ConnectionUtil.post(urlString, inputBody, "PUT");
                    } catch (Exception e) {
                        Log.e("Error occurred while trying to make a PUT call", e.getMessage());
                    }
                    break;

                default:
                    try {
                        conn = ConnectionUtil.get(urlString);
                    } catch (Exception e) {
                        Log.e("Error occurred while trying to make a GET call", e.getMessage());
                    }
                    break;

        }

        if (conn != null) {

            InputStream responseStream = null;
            try {
                responseStream = conn.getInputStream();
                responseBody = getResponseBody(responseStream);
                conn.disconnect();
            } catch (IOException e) {
                Log.e("Error occurred while trying to get response body from connection Obj", e.getMessage());
            } finally {
                try {
                    if(responseStream!=null){
                        responseStream.close();
                    }
                } catch (IOException e) {
                    Log.e("Error occurred while trying to get response body from connection Obj", e.getMessage());
                }
            }

        }

        return responseBody;
    }

    @Override
    protected void onPostExecute(String respn) {
        super.onPostExecute(respn);

    }


    private String getResponseBody(InputStream responsStream) {

        String result = null;
        StringBuilder sb = new StringBuilder();
        InputStream is = null;
        BufferedReader br =null;

        try {
            is = new BufferedInputStream(responsStream);
            br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (IOException e) {
            Log.e("Error occurred while trying to read response body ", e.getMessage());

        } finally {
            if (is != null) {
                try {
                    if(br!=null){
                        br.close();
                    }
                } catch (IOException e) {
                    Log.e("Error occurred while trying to close input stream ", e.getMessage());
                }
            }
        }

        return result;
    }
}
