package com.example.android.quitit;

/**
 * Created by Ayush vaid on 24-07-2017.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateEntry {

    public boolean validateName(String name)
    {
        Character I;
        for(int i=0; i<name.length();i++)
        {
            if(Character.isDigit(name.charAt(i)) == true)
                return false;
        }
        return true;
    }
    public boolean validateEmail(String email)
    {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

}
