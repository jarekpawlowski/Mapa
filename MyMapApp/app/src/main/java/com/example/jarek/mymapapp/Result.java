package com.example.jarek.mymapapp;

/**
 * Created by jarek on 12.01.2017.
 */

public class Result {
    private static final String OKAY_STATUS = "OK";

    private String status;

    public String getStatus( ) {

        return this.status;
    }

    public boolean isOkay( ) {

        return OKAY_STATUS.equals( this.getStatus( ) );
    }
}
