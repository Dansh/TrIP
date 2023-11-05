package com.example.trip;

public class AuthInputHandler {
    public static boolean isEmailValid(String email){
        return email.length() != 0;
    }

    public static boolean isPasswordValid(String password){
        return password.length() >= 8;
    }
}
