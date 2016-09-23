package net.kailyard.template.utils;

import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 */
public final class DateUtil {
    private static final String[] parsePatterns = { "yyyyMMdd", "yyyy-MM-dd",
            "yyyy/MM/dd", "yyyy-MM-dd HH", "yyyy/MM/dd HH", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss.SSS" };

    private DateUtil(){}

    public static Date parseDate(String date) {
        if (Strings.isNullOrEmpty(date)) {
            throw new IllegalArgumentException("date can't be blank.");
        }

        try {
            return parseDateWithLeniency(date, Locale.CHINA, parsePatterns, true);
        } catch (ParseException ex) {
            throw Exceptions.unchecked(ex);
        }
    }

    private static Date parseDateWithLeniency(
            final String str, final Locale locale, final String[] parsePatterns, final boolean lenient) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        SimpleDateFormat parser;
        if (locale == null) {
            parser = new SimpleDateFormat();
        } else {
            parser = new SimpleDateFormat("", locale);
        }

        parser.setLenient(lenient);
        final ParsePosition pos = new ParsePosition(0);
        for (final String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }

            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
            }

            final Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }
}
