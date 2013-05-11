/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kanika
 */
public class Validations {

    static boolean emailValidation(String arg)
    {
     if ((arg.length() < 4) || !arg.contains("@") || !arg.contains(".")) {
            return true;
        }
        return false;
    }
    static boolean blankValidation(String arg){
    if ((arg == null) || (arg.length() == 0)) {
            return true;
        }
    return false;
    }
    static boolean passwordValidation(String arg)
    {
        Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#*=!])(?=[\\S]+$).{8,15})");
        Matcher matche = pattern.matcher(arg);
        if(matche.matches())
        {
            return true;
        }
     return false;
    }
}
