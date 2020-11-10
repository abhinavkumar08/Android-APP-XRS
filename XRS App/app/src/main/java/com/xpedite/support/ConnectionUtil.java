package com.xpedite.support;

import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by abhinkum on 9/20/17.
 * Connection util class to make post and get request.
 */

 class ConnectionUtil {

     static HttpURLConnection post(String  urlString, JSONObject inputBody, String putOrPost) throws Exception {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( putOrPost );
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            if(inputBody!=null){
                conn.getOutputStream().write(inputBody.toString().getBytes());
            }
            return conn;
        }
        catch(MalformedURLException e){
            Log.e("Error occurred while trying to connect to url "+ urlString, e.getMessage());
            throw e;
        }
    }

    public static HttpURLConnection get(String  urlString) throws Exception {
        HttpURLConnection conn;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection)url.openConnection();
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "GET" );
            conn.setRequestProperty( "Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            conn.connect();
            return conn;
        }
        catch(MalformedURLException e){
            Log.e("Error occurred while trying to connect to url "+ urlString, e.getMessage());
            throw e;
        }

    }
}
