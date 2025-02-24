package com.mycompany.motorph.calculation;

import com.mycompany.motorph.model.DateRange;
import com.mycompany.motorph.util.CurrencyUtil;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class that calculates net wage.
 * <p>
 * It calculates the net wage by subtracting total deductions from the gross wage,
 * considering late arrival deductions.
 *
 * @author Lance
 */
public class NetWageCalculation {

    private final SSSDeduction sssDeduction;
    private final HealthInsurancesDeduction healthInsurancesDeduction;
    private final WithholdingTaxCalculation withholdingTaxCalculation;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    // Constants for Attendance and Late Deduction Calculation
    private static final int ATTENDANCE_EXPECTED_COL_LENGTH = 6;
    private static final int EMPLOYEE_NUM_INDEX = 0;
    private static final double LATE_ARRIVAL_DEDUCTION_PER_MINUTE = 1.66;
    private static final int LATE_HOUR_START = 8;
    private static final int LATE_MINUTE_START = 11;
    private static final int MINUTES_IN_HOUR = 60;

    /**
     * Constructor for NetWageCalculation.
     */
    public NetWageCalculation() {
        this.sssDeduction = new SSSDeduction();
        this.healthInsurancesDeduction = new HealthInsurancesDeduction();
        this.withholdingTaxCalculation = new WithholdingTaxCalculation();
    }

    /**
     * Calculates the late arrival deduction for an employee.
     *
     * @param attendanceDataList The list containing attendance data.
     * @param employeeNumber The employee number.
     * @param dateRange The date range.
     * @return The total late arrival deduction amount.
     * @throws ParseException If a date parsing error occurs.
     */
    public double calculateLateArrivalDeduction(final List<String[]> attendanceDataList, final int employeeNumber, final DateRange dateRange) throws ParseException {
        double totalLateDeduction = 0.0;

        for (String[] data : attendanceDataList) {
            if (data.length == ATTENDANCE_EXPECTED_COL_LENGTH && Integer.parseInt(data[EMPLOYEE_NUM_INDEX]) == employeeNumber) {
                Date attendanceDate = DATE_FORMAT.parse(data[3]);
                Date attendanceTimeIn = TIME_FORMAT.parse(data[4]);

                if (dateRange.isWithinDateRange(attendanceDate) && arrivedLate(attendanceTimeIn)) {
                    int lateMinutes = calculateLateMinutes(attendanceTimeIn);
                    totalLateDeduction += lateMinutes * LATE_ARRIVAL_DEDUCTION_PER_MINUTE;
                }
            }
        }
        return totalLateDeduction;
    }

    /**
     * Retrieves the wage details of an employee.
     *
     * @param employeeNumber The employee number.
     * @param hourlyRate The hourly rate.
     * @param hoursWorked The total hours worked.
     * @param lateArrivalDeduction The late arrival deduction.
     * @return A list of formatted wage details.
     */
    public List<String> getWageInformation(final int employeeNumber, final double hourlyRate, final double hoursWorked, final double lateArrivalDeduction) throws IOException, CsvValidationException {
        double grossWage = hourlyRate * hoursWorked;
        double totalDeductions = calculateTotalDeductions(grossWage, lateArrivalDeduction);
        double netWage = Math.max(grossWage - totalDeductions, 0);

        return Arrays.asList(
                CurrencyUtil.formatCurrency(grossWage),
                CurrencyUtil.formatCurrency(sssDeduction.calculateSssDeduction(grossWage)),
                CurrencyUtil.formatCurrency(healthInsurancesDeduction.calculatePhilHealthDeduction(grossWage)),
                CurrencyUtil.formatCurrency(healthInsurancesDeduction.calculatePagIbigDeduction(grossWage)),
                CurrencyUtil.formatCurrency(withholdingTaxCalculation.calculateWithholdingTax(grossWage)),
                CurrencyUtil.formatCurrency(lateArrivalDeduction),
                CurrencyUtil.formatCurrency(totalDeductions),
                CurrencyUtil.formatCurrency(netWage)
        );
    }

    /**
     * Determines if an employee is late based on their arrival time.
     *
     * @param timeIn The employee's time-in.
     * @return True if the employee is late; false otherwise.
     */
    private boolean arrivedLate(final Date timeIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeIn);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return hour > LATE_HOUR_START || (hour == LATE_HOUR_START && minute >= LATE_MINUTE_START);
    }

    /**
     * Calculates the minutes an employee is late.
     *
     * @param timeIn The time of arrival.
     * @return The total late minutes.
     */
    private int calculateLateMinutes(final Date timeIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeIn);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return ((hour - LATE_HOUR_START) * MINUTES_IN_HOUR) + (minute - LATE_MINUTE_START);
    }

    /**
     * Calculates total deductions for an employee.
     *
     * @param grossWage The gross wage before deductions.
     * @param lateArrivalDeduction The late arrival deduction.
     * @return The total deductions.
     */
    private double calculateTotalDeductions(final double grossWage, final double lateArrivalDeduction) throws IOException, CsvValidationException {
        double sss = sssDeduction.calculateSssDeduction(grossWage);
        double philHealth = healthInsurancesDeduction.calculatePhilHealthDeduction(grossWage);
        double pagIbig = healthInsurancesDeduction.calculatePagIbigDeduction(grossWage);
        double tax = withholdingTaxCalculation.calculateWithholdingTax(grossWage);

        return sss + philHealth + pagIbig + tax + lateArrivalDeduction;
    }
}
