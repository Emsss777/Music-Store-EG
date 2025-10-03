package app.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExceptionMessages {

    public static final String BAD_CREDENTIALS = "Bad Credentials!";
    public static final String USERNAME_DOES_NOT_EXIST = "Username [{}] does NOT Exist!";
    public static final String USER_DOES_NOT_EXIST = "User with ID [%s] does NOT Exist!";
    public static final String USERNAME_ALREADY_EXIST = "Username [%s] Already Exist!";
    public static final String INCORRECT_USERNAME_OR_PASSWORD = "Incorrect Username or Password!";
    public static final String USER_CANNOT_BE_EMPTY = "Username cannot be empty!";
    public static final String PASSWORD_CANNOT_BE_EMPTY = "Password cannot be empty!";
    public static final String PASSWORD_DO_NOT_MATCH = "Passwords do NOT Match!";
    public static final String EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty!";
    public static final String USERNAME_INVALID_LENGTH = "Username length must be between 3 and 20 characters!";
    public static final String PASSWORD_INVALID_LENGTH = "Password length must be between 3 and 20 characters!";
    public static final String SELECT_A_COUNTRY = "You must select a country!";
}
