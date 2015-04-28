package com.blinky.peestash.app;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by murielLys on 16/04/15.
 */
public class Verify {

    public static Boolean valid = false;

    /* V�rification email et confirmation d'e-mail identiques
    */

    public Boolean checkEmail(String email, String confirmEmail)
    {

        if(email.equals(confirmEmail))
        {
            valid=true;
        }
        else {
            valid=false;
        }
        return valid;
    }
    /* V�rification de la structure de l'email avec un regex
    */
    public Boolean checkEmailWriting(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
        {
            valid = true;
        }
        return valid;
    }

    /* V�rification de la structure du mot de passe
    */

    public Boolean checkMdpWriting(String password)
    {
        if(password.length()>3)
        {
            valid = true;
        }
        return valid;
    }
    /* V�rification du mot de passe et v�rification de mot de passe
    */
    public Boolean checkMdp(String password, String confirmPassword)
    {

        if(password.equals(confirmPassword))
        {
            valid=true;
        }
        else {
            valid=false;
        }
        return valid;
    }

}