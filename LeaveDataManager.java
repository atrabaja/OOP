package com.mycompany.motorph.data;

import com.mycompany.motorph.model.Leave;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Manages leave applications, including saving, loading, and retrieving leave data.
 * <p>
 * Reads leave applications from a CSV file, processes leave requests, and updates records.
 * </p>
 * 
 * @author Lance
 */
public class LeaveDataManager {

    private static final String LEAVE_DATA_PATH = "src/main/resources/data/leave_balances.csv";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Constants for Leave Types
    private static final double SICK_LEAVE_MULTIPLIER = 1500.0;
    private static final double VACATION_LEAVE_MULTIPLIER = 1500.0;
    private static final double EMERGENCY_LEAVE_MULTIPLIER = 500.0;

    // CSV File Header
    private static final String[] HEADER = {
            "Employee Number", "Leave Type", "Start Date", "End Date", "Reason", 
            "Sick Leave", "Vacation Leave", "Emergency Leave"
    };

    /**
     * Saves a new leave application to the CSV file.
     *
     * @param leave The leave application to be saved
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If CSV validation fails
     */
    public void saveLeaveApplication(final Leave leave) throws IOException, CsvValidationException {
        List<Leave> leaves = loadLeaveApplications();

        // Check if the leave application already exists for the employee
        if (leaves.stream().anyMatch(l -> l.getEmployeeNumber() == leave.getEmployeeNumber())) {
            throw new IllegalArgumentException("Record already exists for employee number: " + leave.getEmployeeNumber());
        }

        // Calculate leave amounts
        calculateLeaveAmounts(leave);

        // Add new leave application
        leaves.add(leave);

        // Write updated leave records to the CSV file
        try (CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(LEAVE_DATA_PATH)))) {
            writer.writeNext(HEADER);
            for (Leave l : leaves) {
                writer.writeNext(formatLeaveData(l));
            }
        }
    }

    /**
     * Retrieves all leave applications for a specific employee.
     *
     * @param employeeNumber The employee number
     * @return A list of leave applications for the specified employee
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If CSV validation fails
     */
    public List<Leave> getLeavesByEmployeeNumber(final int employeeNumber) throws IOException, CsvValidationException {
        return loadLeaveApplications()
                .stream()
                .filter(leave -> leave.getEmployeeNumber() == employeeNumber)
                .collect(Collectors.toList());
    }

    /**
     * Loads all leave applications from the CSV file.
     *
     * @return A list of leave applications
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If CSV validation fails
     */
    private List<Leave> loadLeaveApplications() throws IOException, CsvValidationException {
        List<Leave> leaves = new ArrayList<>();

        if (!Files.exists(Paths.get(LEAVE_DATA_PATH))) {
            return leaves; // Return empty list if file doesn't exist
        }

        try (CSVReader reader = new CSVReader(new BufferedReader(new FileReader(LEAVE_DATA_PATH)))) {
            String[] data;
            reader.readNext(); // Skip header

            while ((data = reader.readNext()) != null) {
                if (data.length < 8) continue; // Skip corrupted rows

                Leave leave = new Leave(
                        parseInteger(data[0]), data[1], data[2], data[3], data[4]
                );
                leave.setSickLeaveAmount(parseDouble(data[5]));
                leave.setVacationLeaveAmount(parseDouble(data[6]));
                leave.setEmergencyLeaveAmount(parseDouble(data[7]));

                leaves.add(leave);
            }
        }
        return leaves;
    }

    /**
     * Calculates leave amounts based on leave type and duration.
     *
     * @param leave The leave application to calculate amounts for
     */
    private void calculateLeaveAmounts(final Leave leave) {
        LocalDate startDate = LocalDate.parse(leave.getStartDate(), DATE_FORMAT);
        LocalDate endDate = LocalDate.parse(leave.getEndDate(), DATE_FORMAT);

        long diffInDays = TimeUnit.DAYS.convert(
                Math.abs(endDate.toEpochDay() - startDate.toEpochDay()), TimeUnit.DAYS
        ) + 1; // Include both start and end dates

        // Reset amounts to 0
        leave.setSickLeaveAmount(0);
        leave.setVacationLeaveAmount(0);
        leave.setEmergencyLeaveAmount(0);

        switch (leave.getLeaveType()) {
            case "Sick Leave" -> leave.setSickLeaveAmount(SICK_LEAVE_MULTIPLIER * diffInDays);
            case "Vacation Leave" -> leave.setVacationLeaveAmount(VACATION_LEAVE_MULTIPLIER * diffInDays);
            case "Emergency Leave" -> leave.setEmergencyLeaveAmount(EMERGENCY_LEAVE_MULTIPLIER * diffInDays);
        }
    }

    /**
     * Converts a Leave object into a string array for CSV writing.
     *
     * @param leave The Leave object
     * @return A string array representing leave data
     */
    private String[] formatLeaveData(final Leave leave) {
        return new String[]{
                String.valueOf(leave.getEmployeeNumber()),
                leave.getLeaveType(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                String.valueOf(leave.getSickLeaveAmount()),
                String.valueOf(leave.getVacationLeaveAmount()),
                String.valueOf(leave.getEmergencyLeaveAmount())
        };
    }

    /**
     * Safely parses a string into an integer.
     *
     * @param value The input string
     * @return Parsed integer or 0 if invalid
     */
    private int parseInteger(final String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Safely parses a string into a double.
     *
     * @param value The input string
     * @return Parsed double or 0.0 if invalid
     */
    private double parseDouble(final String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
