package com.example.mynews1.Utils;

import android.widget.CheckBox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String convertDateForDisplay(String date) {
        // Format Date "yyyy-MM-dd'T'HH:mm:ss"
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        Date mDate = null;

        try {
            mDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(mDate);
    }

    public static String convertDateForDisplayBis(String date){

        // Format Date "yyyy-MM-dd"
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date mDate = null;

        try {
            mDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(mDate);

    }


     /** Sets a String to filter queries in http request according to the MaterialCheckbox checked
     * @return the string needed to filter queries
     **/
    public static String configureFilterQueries(CheckBox cb1, CheckBox cb2, CheckBox cb3, CheckBox cb4, CheckBox cb5, CheckBox cb6) {
        String filterQuery = "news_desk:(";
        if (cb1.isChecked())
            filterQuery += "\"Arts\" ";
        if (cb2.isChecked())
            filterQuery += "\"Business\" ";
        if (cb3.isChecked())
            filterQuery += "\"Politics\" ";
        if (cb4.isChecked())
            filterQuery += "\"Sports\" ";
        if (cb5.isChecked())
            filterQuery += "\"Entrepreneurs\" ";
        if (cb6.isChecked())
            filterQuery += "\"Travel\" ";
        filterQuery += ")";
        return filterQuery;
    }

    /**
     * Convert String date to the http request needed format
     * @param date a given date
     * @return formatted String date for request
     */
    public static String convertDateForQuery(String date) {
        // Format Date "yyyy-MM-dd'T'HH:mm:ss"
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        Date mDate = null;

        try {
            mDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(mDate);
    }

    public static String convertTitleToId(String initialtitle) {
        if (initialtitle.length() >= 20) {
            String substring = initialtitle.substring(0, 20);
            return substring;
        } else
            return initialtitle;
    }
}

