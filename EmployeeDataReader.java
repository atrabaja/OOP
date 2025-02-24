package com.mycompany.motorph.data;

import com.mycompany.motorph.model.Employee;
import com.mycompany.motorph.util.CurrencyUtil;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing of employee data from a CSV file.
 * <p>
 * Reads employee records from a CSV file, processes data, and writes updates
 * to maintain an up-to-date employee database.
 * </p>
 *
 * @author Lance
 */
public class EmployeeDataReader {

    private static final SimpleDateFormat BIRTHDATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final int EMPLOYEE_EXPECTED_COL_LENGTH = 19;

    // CSV File Header
    private static final String[] HEADER = {
            "Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
            "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor",
            "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance",
            "Gross Semi-monthly Rate", "Hourly Rate"
    };

    /**
     * Reads employee data from the given CSV file path.
     *
     * @param filePath Path to the CSV file
     * @return A list of Employee objects
     * @throws IOException If an error occurs during file reading
     * @throws CsvValidationException If the CSV data is invalid
     * @throws ParseException If there's an issue parsing date formats
     */
    public List<Employee> readEmployees(final String filePath) throws IOException, CsvValidationException, ParseException {
        List<Employee> employees = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new BufferedReader(new FileReader(filePath)))) {
            String[] data;
            reader.readNext(); // Skip header

            while ((data = reader.readNext()) != null) {
                if (data.length == EMPLOYEE_EXPECTED_COL_LENGTH) {
                    employees.add(createEmployeeFromData(data));
                } else {
                    throw new IllegalArgumentException("Invalid data length: " + data.length + " in row: " + String.join(",", data));
                }
            }
        }

        return employees;
    }

    /**
     * Writes a list of employees to a CSV file.
     *
     * @param filePath  Path to the CSV file
     * @param employees List of Employee objects
     * @throws IOException If an error occurs during file writing
     */
    public void writeEmployees(final String filePath, final List<Employee> employees) throws IOException {
        try (CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(filePath)))) {
            writer.writeNext(HEADER);

            for (Employee employee : employees) {
                writer.writeNext(formatEmployeeData(employee));
            }
        }
    }

    /**
     * Converts an Employee object into a String array for CSV writing.
     *
     * @param employee The Employee object
     * @return A string array representing the employee's data
     */
    private String[] formatEmployeeData(final Employee employee) {
        return new String[]{
                String.valueOf(employee.getEmployeeNumber()),
                employee.getLastName(),
                employee.getFirstName(),
                BIRTHDATE_FORMAT.format(employee.getBirthdate()),
                employee.getAddress(),
                employee.getPhoneNumber(),
                employee.getSssNumber(),
                employee.getPhilHealthNumber(),
                employee.getTin(),
                employee.getPagIbigNumber(),
                employee.getStatus(),
                employee.getPosition(),
                employee.getImmediateSupervisor(),
                CurrencyUtil.formatCurrency(employee.getBasicSalary()),
                CurrencyUtil.formatCurrency(employee.getRiceSubsidy()),
                CurrencyUtil.formatCurrency(employee.getPhoneAllowance()),
                CurrencyUtil.formatCurrency(employee.getClothingAllowance()),
                CurrencyUtil.formatCurrency(employee.getGrossSemimonthlyRate()),
                CurrencyUtil.formatCurrency(employee.getHourlyRate())
        };
    }

    /**
     * Parses a CSV row into an Employee object.
     *
     * @param employeeData The CSV row data
     * @return An Employee object
     * @throws ParseException If there's an issue parsing the date format
     */
    private Employee createEmployeeFromData(final String[] employeeData) throws ParseException {
        Employee employee = new Employee();

        employee.setEmployeeNumber(parseInteger(employeeData[0]));
        employee.setLastName(employeeData[1]);
        employee.setFirstName(employeeData[2]);
        employee.setBirthdate(parseDate(employeeData[3]));
        employee.setAddress(employeeData[4]);
        employee.setPhoneNumber(employeeData[5]);
        employee.setSssNumber(employeeData[6]);
        employee.setPhilHealthNumber(employeeData[7]);
        employee.setTin(employeeData[8]);
        employee.setPagIbigNumber(employeeData[9]);
        employee.setStatus(employeeData[10]);
        employee.setPosition(employeeData[11]);
        employee.setImmediateSupervisor(employeeData[12]);
        employee.setBasicSalary(parseDouble(employeeData[13]));
        employee.setRiceSubsidy(parseDouble(employeeData[14]));
        employee.setPhoneAllowance(parseDouble(employeeData[15]));
        employee.setClothingAllowance(parseDouble(employeeData[16]));
        employee.setGrossSemimonthlyRate(parseDouble(employeeData[17]));
        employee.setHourlyRate(parseDouble(employeeData[18]));

        return employee;
    }

    /**
     * Parses a String into an integer safely.
     *
     * @param value The input string
     * @return The parsed integer, or 0 if invalid
     */
    private int parseInteger(final String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Parses a date string into a Date object.
     *
     * @param value The date string
     * @return The parsed Date object, or null if invalid
     * @throws ParseException If the date format is incorrect
     */
    private java.util.Date parseDate(final String value) throws ParseException {
        return value.isEmpty() ? null : BIRTHDATE_FORMAT.parse(value.trim());
    }

    /**
     * Parses a String into a double, handling commas properly.
     *
     * @param value The input string
     * @return The parsed double value, or 0.0 if invalid
     */
    private double parseDouble(final String value) {
        try {
            return Double.parseDouble(value.replaceAll(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
