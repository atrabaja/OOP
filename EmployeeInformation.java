package com.mycompany.motorph.employee;

import com.mycompany.motorph.model.Employee;
import com.mycompany.motorph.data.EmployeeDataReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

/**
 * Manages employee information retrieval and updates.
 * <p>
 * Allows searching for employees and modifying their records.
 * </p>
 * 
 * @author Lance
 */
public class EmployeeInformation {

    private static final SimpleDateFormat BIRTHDATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    // File Path Constant
    private static final String EMPLOYEES_DATA_PATH = "src/main/resources/data/employee_information.csv";

    private final EmployeeDataReader employeeDataReader;

    /**
     * Constructor for EmployeeInformation.
     */
    public EmployeeInformation() {
        this.employeeDataReader = new EmployeeDataReader();
    }

    /**
     * Retrieves employee information by employee number.
     *
     * @param employeeNumber The employee number to search for
     * @return A list of strings containing employee details
     * @throws IOException If an I/O error occurs while reading the file
     * @throws CsvValidationException If data from a row is invalid
     * @throws ParseException If a parsing error occurs
     */
    public List<String> getEmployeeInformation(final int employeeNumber) throws IOException, CsvValidationException, ParseException {
        List<Employee> employees = getAllEmployees();

        Optional<Employee> employee = employees.stream()
                .filter(e -> e.getEmployeeNumber() == employeeNumber)
                .findFirst();

        return employee.map(Employee::getEmployeeInformation)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found for ID: " + employeeNumber));
    }

    /**
     * Updates employee information in the CSV file.
     *
     * @param employeeNumber The employee number to update
     * @param updatedEmployeeInfo The updated details
     * @throws IOException If an I/O error occurs while writing to the file
     * @throws CsvValidationException If CSV data is invalid
     * @throws ParseException If parsing error occurs
     */
    public void updateEmployeeInformation(final int employeeNumber, final List<String> updatedEmployeeInfo) throws IOException, CsvValidationException, ParseException {
        List<Employee> employees = employeeDataReader.readEmployees(EMPLOYEES_DATA_PATH);

        Optional<Employee> employeeOpt = employees.stream()
                .filter(e -> e.getEmployeeNumber() == employeeNumber)
                .findFirst();

        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Cannot update: Employee ID " + employeeNumber + " not found.");
        }

        Employee employee = employeeOpt.get();
        updateEmployee(employee, updatedEmployeeInfo);

        employeeDataReader.writeEmployees(EMPLOYEES_DATA_PATH, employees);
    }

    /**
     * Retrieves the list of all employees.
     *
     * @return A list of Employee objects
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If CSV data is invalid
     * @throws ParseException If parsing error occurs
     */
    public List<Employee> getAllEmployees() throws IOException, CsvValidationException, ParseException {
        return employeeDataReader.readEmployees(EMPLOYEES_DATA_PATH);
    }

    /**
     * Updates an Employee object with new data.
     *
     * @param employee The employee object to update
     * @param updatedEmployeeInfo A list containing updated employee details
     * @throws ParseException If parsing error occurs
     */
    private void updateEmployee(final Employee employee, final List<String> updatedEmployeeInfo) throws ParseException {
        employee.setLastName(updatedEmployeeInfo.get(0));
        employee.setFirstName(updatedEmployeeInfo.get(1));
        employee.setBirthdate(BIRTHDATE_FORMAT.parse(updatedEmployeeInfo.get(2)));
        employee.setAddress(updatedEmployeeInfo.get(3));
        employee.setPhoneNumber(updatedEmployeeInfo.get(4));
        employee.setSssNumber(updatedEmployeeInfo.get(5));
        employee.setPhilHealthNumber(updatedEmployeeInfo.get(6));
        employee.setTin(updatedEmployeeInfo.get(7));
        employee.setPagIbigNumber(updatedEmployeeInfo.get(8));
        employee.setStatus(updatedEmployeeInfo.get(9));
        employee.setPosition(updatedEmployeeInfo.get(10));
        employee.setImmediateSupervisor(updatedEmployeeInfo.get(11));
        employee.setBasicSalary(parseDouble(updatedEmployeeInfo.get(12)));
        employee.setRiceSubsidy(parseDouble(updatedEmployeeInfo.get(13)));
        employee.setPhoneAllowance(parseDouble(updatedEmployeeInfo.get(14)));
        employee.setClothingAllowance(parseDouble(updatedEmployeeInfo.get(15)));
        employee.setGrossSemimonthlyRate(parseDouble(updatedEmployeeInfo.get(16)));
        employee.setHourlyRate(parseDouble(updatedEmployeeInfo.get(17)));
    }

    /**
     * Parses a string into a double, handling empty values.
     *
     * @param value The input string
     * @return Parsed double or 0.0 if invalid
     */
    private double parseDouble(final String value) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
