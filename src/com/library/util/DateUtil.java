
package com.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static LocalDate parse(String s) {
        return LocalDate.parse(s, f);
    }
    public static String format(LocalDate d) {
        return d.format(f);
    }
}
