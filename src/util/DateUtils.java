package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;


public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");


    public static synchronized Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new ParseException("Date string cannot be empty", 0);
        }
        DATE_FORMAT.setLenient(false);
        return DATE_FORMAT.parse(dateStr.trim());
    }


    public static synchronized String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }


    public static boolean isValidDate(String dateStr) {
        try {
            parseDate(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public static String getCurrentDateAsString() {
        return formatDate(new Date());
    }


    public static Date getCurrentDate() {
        return new Date();
    }


    public static java.sql.Date convertToSqlDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }


    public static Date convertFromSqlDate(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        return new Date(sqlDate.getTime());
    }


    public static Timestamp convertToTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }
}
