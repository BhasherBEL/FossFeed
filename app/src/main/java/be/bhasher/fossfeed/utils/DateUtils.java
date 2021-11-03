package be.bhasher.fossfeed.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DateUtils {
    public static Calendar parseDate(String text) throws ParseException, NullPointerException {
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.getDefault());
        calendar.setTime(Objects.requireNonNull(dateFormat.parse(text)));
        return calendar;
    }

    public static String formatDate(Calendar calendar){
        long diff = (Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis())/1000;

        if(diff < 60) return diff + "s";
        if(diff < 60*60) return diff/60 + "m";
        if(diff < 60*60*24) return diff/3600 + "h";
        return diff/86400 + "d";
    }
}
