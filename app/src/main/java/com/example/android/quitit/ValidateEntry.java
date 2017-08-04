package com.example.android.quitit;

/**
 * Created by Ayush vaid on 24-07-2017.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateEntry {


    public static boolean validateEmpty(String entity)
    {
        if(entity.length()<1)
            return false;

        return true;
    }

    public static boolean validateNameDigit(String name)
    {
        Character I;
        for(int i=0; i<name.length();i++)
        {
            if(Character.isDigit(name.charAt(i)) == true)
                return false;
        }
        return true;
    }

    public static boolean validateEmail(String email)
    {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public static boolean validateAge(String ageValue)
    {
        if(!ValidateEntry.validateInteger(ageValue))
            return false;

        int age = Integer.parseInt(ageValue);
        if(age>120)
            return false;
        else
            return true;
    }

    public static boolean validatePhone(String phone)
    {
        if(!ValidateEntry.validateInteger(phone))
            return false;

        if(phone.length()!=10)
            return false;
        else
            return true;
    }

    public static boolean validateInteger(String value) //to check if value is purely integer
    {
        if(value.matches("[a-zA-Z]"))
            return false;

        return true;
    }
}
