package com.mycompany.motorph.calculation;

import com.mycompany.motorph.model.DateRange;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A class for calculating employee working hours.
 * <p>
 * It processes attendance data to compute total hours worked, 
 * assumed working hours, and number of days within a given date range.
 * </p>
 *
 * @author Lance
 */
public class TimeCalculation {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    private static final int ATTENDANCE_EXPECTED_COL_LENGTH = 6;
    private static final double ASSUMED_HOURS_PER_DAY = 9.0;

    /**
     * Calculates the total hours worked by the employee within the inputted date range.
     *
     * @param attendanceDataList The list containing attendance data
     * @param employeeNumber The employee number
     * @param dateRange The date range
     * @return The total hours worked
     * @throws ParseException If there is an error parsing dates
     */
    public double calculateTotalHoursWorked(final List<String[]> attendanceDataList, final int employeeNumber, final DateRange dateRange) throws ParseException {
        double totalHoursWorked = 0.0;

        for (String[] data : attendanceDataList) {
            // Ensure data length matches the expected format and employee number matches
            if (data.length == ATTENDANCE_EXPECTED_COL_LENGTH && Integer.parseInt(data[0]) == employeeNumber) {
                // Parse attendance date, time-in, and time-out
                final Date attendanceDate = DATE_FORMAT.parse(data[3]);
                if (!dateRange.isWithinDateRange(attendanceDate)) {
                    continue; // Skip records outside the date range
                }

                final Date attendanceTimeIn = TIME_FORMAT.parse(data[4]);
                final Date attendanceTimeOut = TIME_FORMAT.parse(data[5]);

                // Calculate and accumulate total hours worked
                totalHoursWorked += calculateWorkedHours(attendanceTimeIn, attendanceTimeOut);
            }
        }

        return totalHoursWorked;
    }

    /**
     * Calculates the assumed hours worked based on the number of workdays within a date range.
     *
     * @param dateRange The date range
     * @return The assumed hours worked
     */
    public double calculateAssumedHoursWorked(final DateRange dateRange) {
        return ASSUMED_HOURS_PER_DAY * getNumberOfDays(dateRange);
    }

    /**
     * Calculates the number of days within the inputted date range.
     *
     * @param dateRange The date range
     * @return The number of days (including start and end date)
     */
    public long getNumberOfDays(final DateRange dateRange) {
        return ChronoUnit.DAYS.between(
                dateRange.getStartDate().toInstant(), 
                dateRange.getEndDate().toInstant()
        ) + 1; // +1 to include both start and end date
    }

    /**
     * Calculates the number of hours worked between time-in and time-out.
     *
     * @param timeIn The time the employee clocked in
     * @param timeOut The time the employee clocked out
     * @return The number of hours worked, including fractional hours
     */
    private double calculateWorkedHours(final Date timeIn, final Date timeOut) {
        // Calculate the difference in milliseconds
        final long timeDifferenceMillis = timeOut.getTime() - timeIn.getTime();

        // Convert milliseconds to hours (including fractional part)
        return timeDifferenceMillis / (double) TimeUnit.HOURS.toMillis(1);
    }
}
