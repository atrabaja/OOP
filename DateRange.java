package com.mycompany.motorph.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Represents a date range with validation.
 * <p>
 * Provides methods to check date inclusion and generate date ranges.
 * </p>
 * 
 * @author Lance
 */
public class DateRange {

    private final Date startDate;
    private final Date endDate;

    private static final String DATE_PATTERN = "MM/dd";
    private static final String MONTH_PATTERN = "MM";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);
    private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat(MONTH_PATTERN);

    static {
        DATE_FORMAT.setLenient(false); // Strict validation
        MONTH_FORMAT.setLenient(false);
    }

    /**
     * Constructs a DateRange object with validation.
     *
     * @param startDate The start date
     * @param endDate The end date
     * @throws IllegalArgumentException If end date is before start date
     */
    public DateRange(Date startDate, Date endDate) {
        if (endDate.before(startDate)) {
            throw new IllegalArgumentException("End date must be on or after the start date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    /**
     * Checks if a date falls within the range.
     *
     * @param date The date to check
     * @return true if within range, false otherwise
     */
    public boolean isWithinDateRange(Date date) {
        return !date.before(startDate) && !date.after(endDate);
    }

    /**
     * Creates a DateRange for an entire month.
     *
     * @param month The month in "MM" format
     * @return The DateRange for the month
     * @throws ParseException If the month format is invalid
     */
    public static DateRange createMonthRange(String month) throws ParseException {
        Calendar calendar = Calendar.getInstance();

        // Validate and parse month
        calendar.setTime(MONTH_FORMAT.parse(month));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = calendar.getTime();

        return new DateRange(startDate, endDate);
    }

    /**
     * Creates a DateRange from start and end dates.
     *
     * @param startDateString The start date in "MM/dd" format
     * @param endDateString The end date in "MM/dd" format
     * @return The DateRange object
     * @throws ParseException If input format is incorrect
     */
    public static DateRange createDateRange(String startDateString, String endDateString) throws ParseException {
        return new DateRange(parseDate(startDateString), parseDate(endDateString));
    }

    /**
     * Parses a date string into a Date object with validation.
     *
     * @param dateString The date string in "MM/dd" format
     * @return The parsed Date object
     * @throws ParseException If the date format is invalid
     */
    private static Date parseDate(String dateString) throws ParseException {
        return DATE_FORMAT.parse(dateString);
    }
}
