package com.xpedite.support;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhinkum on 4/21/18.
 * Validation class
 */

public final class ValidationUtil {

    private ValidationUtil() {

    }

    public static boolean isValidMobile(String phone) {
        Pattern p = Pattern.compile("\\d{10}");
        Matcher m = p.matcher(phone);

        return m.matches();
    }

    public static boolean isFieldBlank(String field) {
       return field.isEmpty();
    }

    public static boolean isPinCodeValid(String pincode) {
        String userInputPattern = "^[1-9][0-9]{5}$";
        Boolean isValid = pincode.matches(userInputPattern);

        return isValid;
    }

    public static boolean isTyreCountValid(String tyreCount)
    {
        try {

            int tyreCountInt = Integer.parseInt(tyreCount);
            if(tyreCountInt>=0 && tyreCountInt<=10000)
                return true;
        }
        catch (NumberFormatException e) {

            return false;
        }
        return false;
    }
}
