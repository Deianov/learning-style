package bg.geist.util;

import bg.geist.constant.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAdapter {
    private static final SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.GERMANY);

    public static Date toDate(String s) throws Exception {
        return formatter.parse(s);
    }

    public static String toString(Date date) throws Exception {
        return date.toString();
    }
}
