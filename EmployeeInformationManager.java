/*
 * Employee Information Manager Interface
 */
package com.mycompany.motorph;

import java.util.List;

/**
 * Interface for managing and processing employee information.
 * Ensures all implementing classes follow a standard structure
 * for retrieving, updating, and handling employee data.
 * 
 * @author Lance
 */
public interface EmployeeInformationManager {

    /**
     * Displays an error dialog with the provided error message.
     * 
     * @param errorMessage The message to be displayed in the error dialog.
     */
    void showErrorDialog(String errorMessage);

    /**
     * Populates and retrieves employee information based on the provided employee number.
     * 
     * @param employeeNumber The unique identifier of the employee.
     * @return A list of employee details.
     */
    List<String> populateEmployeeInformation(int employeeNumber);

    /**
     * Updates an employee's information in the system.
     * 
     * @param employeeNumber The unique identifier of the employee.
     * @param updatedInfo A list of updated employee details.
     * @return True if the update was successful, false otherwise.
     */
    boolean updateEmployeeInformation(int employeeNumber, List<String> updatedInfo);

    /**
     * Deletes an employee's information from the system.
     * 
     * @param employeeNumber The unique identifier of the employee.
     * @return True if deletion was successful, false otherwise.
     */
    boolean deleteEmployeeInformation(int employeeNumber);
}
