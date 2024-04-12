package com.example.demo.Models;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

interface DateAdapter {
    Date convert(String dateString);
}

public class StringToDateAdapter implements DateAdapter {

    @Override
    public Date convert(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle the error appropriately
        }
    }
}