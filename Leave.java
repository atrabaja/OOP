package com.mycompany.motorph.model;

/**
 * Represents a leave application.
 * <p>
 * Stores leave details and provides access methods.
 * </p>
 * 
 * @author Lance
 */
public class Leave {

    private int employeeNumber;
    private String leaveType;
    private String startDate;
    private String endDate;
    private String reason;
    private double sickLeaveAmount;
    private double vacationLeaveAmount;
    private double emergencyLeaveAmount;

    /**
     * Constructs a Leave object with basic details.
     * 
     * @param employeeNumber Employee's unique ID
     * @param leaveType Type of leave (e.g., Sick Leave, Vacation Leave)
     * @param startDate Leave start date (format: MM/dd/yyyy)
     * @param endDate Leave end date (format: MM/dd/yyyy)
     * @param reason Reason for leave
     */
    public Leave(int employeeNumber, String leaveType, String startDate, String endDate, String reason) {
        this.employeeNumber = employeeNumber;
        this.leaveType = (leaveType != null) ? leaveType : "Unknown";
        this.startDate = (startDate != null) ? startDate : "N/A";
        this.endDate = (endDate != null) ? endDate : "N/A";
        this.reason = (reason != null) ? reason : "No reason provided";
        this.sickLeaveAmount = 0.0;
        this.vacationLeaveAmount = 0.0;
        this.emergencyLeaveAmount = 0.0;
    }

    // Getters
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getReason() {
        return reason;
    }

    public double getSickLeaveAmount() {
        return sickLeaveAmount;
    }

    public double getVacationLeaveAmount() {
        return vacationLeaveAmount;
    }

    public double getEmergencyLeaveAmount() {
        return emergencyLeaveAmount;
    }

    // Setters
    public void setStartDate(String startDate) {
        this.startDate = (startDate != null) ? startDate : "N/A";
    }

    public void setEndDate(String endDate) {
        this.endDate = (endDate != null) ? endDate : "N/A";
    }

    public void setReason(String reason) {
        this.reason = (reason != null) ? reason : "No reason provided";
    }

    public void setSickLeaveAmount(double sickLeaveAmount) {
        this.sickLeaveAmount = sickLeaveAmount;
    }

    public void setVacationLeaveAmount(double vacationLeaveAmount) {
        this.vacationLeaveAmount = vacationLeaveAmount;
    }

    public void setEmergencyLeaveAmount(double emergencyLeaveAmount) {
        this.emergencyLeaveAmount = emergencyLeaveAmount;
    }

    /**
     * Returns a string representation of the leave object.
     * 
     * @return A formatted string with leave details.
     */
    @Override
    public String toString() {
        return "Leave{" +
                "employeeNumber=" + employeeNumber +
                ", leaveType='" + leaveType + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", reason='" + reason + '\'' +
                ", sickLeaveAmount=" + sickLeaveAmount +
                ", vacationLeaveAmount=" + vacationLeaveAmount +
                ", emergencyLeaveAmount=" + emergencyLeaveAmount +
                '}';
    }
}
