package com.xpedite.support;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by abhinkum on 10/6/18.
 * Utility class for common functionality.
 */

public class CommonUtils  {


    public static String getDate(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", Locale.ENGLISH);

        return sdf.format(date);

    }


}
