package com.ClashData.Exceptions;

public class TokenException extends Exception {

    public TokenException()
    {
        super("Could not get the token from the file");
    }
}
