package TaskManager.entities.entitiesUtils;

import TaskManager.entities.User;

import java.util.regex.Pattern;

public class Validations {
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
    //private static final String FULL_NAME_REGEX = "(\\b[A-Z]{1}[a-z]+)( )([A-Z]{1}[a-z]+\\b)";
    //private static final String FULL_NAME_REGEX = ".*";
    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
    private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    /*
    Email -
    It allows numeric values from 0 to 9.
    Both uppercase and lowercase letters from a to z are allowed.
    Allowed to are underscore “_”, hyphen “-“, and dot “.”
    Dot isn't allowed at the start and end of the local part.
    Consecutive dots aren't allowed.
    For the local part, a maximum of 64 characters are allowed.
    */
    public static boolean isEmailRegexValid(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    /*
    Password -
    Must have at least one numeric character
    Must have at least one lowercase character
    Must have at least one uppercase character
    Must have at least one special symbol among @#$%
    Length should be between 8 and 20
    */
    public static boolean isPasswordRegexValid(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }

    /*
    Username -
    Username consists of alphanumeric characters (a-zA-Z0-9), lowercase, or uppercase.
    Username allowed of the dot (.), underscore (_), and hyphen (-).
    The dot (.), underscore (_), or hyphen (-) must not be the first or last character.
    The dot (.), underscore (_), or hyphen (-) does not appear consecutively, e.g., java..regex
    The number of characters must be between 5 to 20.
    */
    public static boolean isUserNamedRegexValid(String userName) {
        return Pattern.compile(USERNAME_REGEX).matcher(userName).matches();
    }

    public static boolean isFullNamedRegexValid(String name) {
        return true;// Pattern.compile(FULL_NAME_REGEX).matcher(name).matches();
    }
    public static void fullUserValid(User user){
        if(!isEmailRegexValid(user.getEmail())){
            throw new IllegalArgumentException("EMAIL NOT VALID");
        }
        if(!isUserNamedRegexValid(user.getUsername())){
            throw new IllegalArgumentException("NAME NOT VALID");
        }
        if(!isPasswordRegexValid(user.getPassword())){
            throw new IllegalArgumentException("PASSWORD NOT VALID");
        }
    }

}
