package com.mycompany.motorph.calculation;

import com.mycompany.motorph.model.DateRange;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for calculating wages based on hours worked.
 * <p>
 * It reads employee and attendance data, calculates total hours worked, late arrival deductions,
 * and determines net wages using payroll deduction rules.
 * </p>
 *
 * @author Lance
 */
public class WageCalculation {

    private final TimeCalculation timeCalculator;
    private final NetWageCalculation netWageCalculation;

    // File paths for data
    private static final String EMPLOYEE_DATA_PATH = "src/main/resources/data/employee_information.csv";
    private static final String ATTENDANCE_DATA_PATH = "src/main/resources/data/employee_attendance.csv";

    // Constants for CSV indices
    private static final int EMPLOYEE_NUM_INDEX = 0;
    private static final int HOURLY_RATE_INDEX = 18;
    private static final int EMPLOYEE_EXPECTED_COL_LENGTH = 19;

    /**
     * Constructor for WageCalculation.
     */
    public WageCalculation() {
        this.timeCalculator = new TimeCalculation();
        this.netWageCalculation = new NetWageCalculation();
    }

    /**
     * Calculates the wage for an employee over a given date range.
     *
     * @param employeeNumber The employee number
     * @param dateRange The date range for wage calculation
     * @return The wage breakdown as a list of formatted strings
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If data validation fails
     * @throws ParseException If a parsing error occurs
     */
    public List<String> calculateWage(final int employeeNumber, final DateRange dateRange)
            throws IOException, CsvValidationException, ParseException {

        final List<String[]> attendanceData = loadAttendanceData();
        final double hourlyRate = getEmployeeHourlyRate(employeeNumber);
        
        // Compute total and assumed hours worked
        final double totalHoursWorked = timeCalculator.calculateTotalHoursWorked(attendanceData, employeeNumber, dateRange);
        final double assumedHoursWorked = timeCalculator.calculateAssumedHoursWorked(dateRange);

        // Compute late arrival deduction
        final double lateArrivalDeduction = netWageCalculation.calculateLateArrivalDeduction(attendanceData, employeeNumber, dateRange);

        // Use actual hours worked if available; otherwise, use assumed hours
        final double hoursForCalculation = (totalHoursWorked > 0) ? totalHoursWorked : assumedHoursWorked;

        // Compute and return the wage breakdown
        return netWageCalculation.getWageInformation(employeeNumber, hourlyRate, hoursForCalculation, lateArrivalDeduction);
    }

    /**
     * Retrieves the hourly rate for a specific employee from the CSV file.
     *
     * @param employeeNumber The employee number
     * @return The hourly rate
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If data validation fails
     */
    private double getEmployeeHourlyRate(final int employeeNumber) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(EMPLOYEE_DATA_PATH))) {
            reader.readNext(); // Skip header row
            String[] data;

            while ((data = reader.readNext()) != null) {
                if (data.length == EMPLOYEE_EXPECTED_COL_LENGTH) {
                    try {
                        if (Integer.parseInt(data[EMPLOYEE_NUM_INDEX]) == employeeNumber) {
                            return Double.parseDouble(data[HOURLY_RATE_INDEX]);
                        }
                    } catch (NumberFormatException e) {
                        // Log and ignore malformed employee number entries
                        System.err.println("Invalid employee number in CSV: " + data[EMPLOYEE_NUM_INDEX]);
                    }
                }
            }
        }

        throw new IllegalArgumentException("Hourly rate not found for employee number: " + employeeNumber);
    }

    /**
     * Loads employee attendance data from a CSV file.
     *
     * @return List of attendance records
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If data validation fails
     */
    private List<String[]> loadAttendanceData() throws IOException, CsvValidationException {
        final List<String[]> attendanceRecords = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(ATTENDANCE_DATA_PATH))) {
            reader.readNext(); // Skip header row
            String[] data;
            while ((data = reader.readNext()) != null) {
                attendanceRecords.add(data);
            }
        }

        return attendanceRecords;
    }
}
