
package com.library.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineCalculator {
    private static final double FINE_PER_DAY = 5.0; // Rs. 5 per day

    public static double calculate(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) return 0.0;
        long days = ChronoUnit.DAYS.between(dueDate, returnDate);
        return days * FINE_PER_DAY;
    }
}
