package com.mycompany.motorph.model;

import com.mycompany.motorph.util.CurrencyUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Represents an employee.
 * <p>
 * Stores employee details, provides access methods, and supports formatted output.
 * </p>
 * 
 * @author Lance
 */
public class Employee {

    private int employeeNumber;
    private String lastName;
    private String firstName;
    private Date birthdate;
    private String address;
    private String phoneNumber;
    private String sssNumber;
    private String philHealthNumber;
    private String tin;
    private String pagIbigNumber;
    private String status;
    private String position;
    private String immediateSupervisor;
    private double basicSalary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossSemimonthlyRate;
    private double hourlyRate;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    // Getters and setters
    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address != null ? address : "";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : "";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSssNumber() {
        return sssNumber != null ? sssNumber : "";
    }

    public void setSssNumber(String sssNumber) {
        this.sssNumber = sssNumber;
    }

    public String getPhilHealthNumber() {
        return philHealthNumber != null ? philHealthNumber : "";
    }

    public void setPhilHealthNumber(String philHealthNumber) {
        this.philHealthNumber = philHealthNumber;
    }

    public String getTin() {
        return tin != null ? tin : "";
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getPagIbigNumber() {
        return pagIbigNumber != null ? pagIbigNumber : "";
    }

    public void setPagIbigNumber(String pagIbigNumber) {
        this.pagIbigNumber = pagIbigNumber;
    }

    public String getStatus() {
        return status != null ? status : "";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position != null ? position : "";
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor != null ? immediateSupervisor : "";
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getRiceSubsidy() {
        return riceSubsidy;
    }

    public void setRiceSubsidy(double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public double getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public double getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public double getGrossSemimonthlyRate() {
        return grossSemimonthlyRate;
    }

    public void setGrossSemimonthlyRate(double grossSemimonthlyRate) {
        this.grossSemimonthlyRate = grossSemimonthlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * Formats the birthdate as a string.
     *
     * @return The birthdate in "MM/dd/yyyy" format, or "N/A" if null
     */
    public String getBirthdateAsString() {
        return birthdate != null ? DATE_FORMAT.format(birthdate) : "N/A";
    }

    /**
     * Retrieves formatted employee information.
     *
     * @return A list containing formatted employee details.
     */
    public List<String> getEmployeeInformation() {
        return Arrays.asList(
            String.valueOf(employeeNumber),
            getLastName(),
            getFirstName(),
            getBirthdateAsString(),
            getAddress(),
            getPhoneNumber(),
            getSssNumber(),
            getPhilHealthNumber(),
            getTin(),
            getPagIbigNumber(),
            getStatus(),
            getPosition(),
            getImmediateSupervisor(),
            formatCurrency(basicSalary),
            formatCurrency(riceSubsidy),
            formatCurrency(phoneAllowance),
            formatCurrency(clothingAllowance),
            formatCurrency(grossSemimonthlyRate),
            formatCurrency(hourlyRate)
        );
    }

    /**
     * Formats a salary value as currency.
     *
     * @param value The monetary value
     * @return Formatted currency string
     */
    private String formatCurrency(double value) {
        return CurrencyUtil.formatCurrency(value);
    }
}
