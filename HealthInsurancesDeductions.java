package com.mycompany.motorph.calculation;

/**
 * A class that calculates Pag-IBIG and PhilHealth deductions/contributions.
 * It ensures proper computation of employee deductions based on salary.
 * 
 * @author Lance
 */
public class HealthInsurancesDeduction {

    // Constants for PhilHealth Calculation
    private static final double MIN_PHILHEALTH_DEDUCTION = 300;
    private static final double MAX_PHILHEALTH_DEDUCTION = 1800;
    private static final double PHILHEALTH_EMPLOYEE_SHARE = 0.50;
    private static final double PHILHEALTH_MIN_SALARY = 10000;
    private static final double PHILHEALTH_MAX_SALARY = 60000;
    private static final double PHILHEALTH_PREMIUM_RATE = 0.03;

    // Constants for Pag-IBIG Calculation
    private static final double PAGIBIG_MIN_SALARY = 1000;
    private static final double PAGIBIG_MAX_SALARY = 1500;
    private static final double PAGIBIG_RATE_1000_TO_1500 = 0.03;
    private static final double PAGIBIG_RATE_ABOVE_1500 = 0.04;
    private static final double MAX_PAGIBIG_DEDUCTION = 100;

    /**
     * Calculates the PhilHealth deduction based on the employee's gross wage.
     *
     * @param grossWage The employee's gross wage.
     * @return The calculated PhilHealth deduction amount.
     */
    public double calculatePhilHealthDeduction(final double grossWage) {
        return calculatePhilHealthPremium(grossWage) * PHILHEALTH_EMPLOYEE_SHARE;
    }

    /**
     * Calculates the Pag-IBIG deduction based on the employee's gross wage.
     *
     * @param grossWage The employee's gross wage.
     * @return The calculated Pag-IBIG deduction amount.
     */
    public double calculatePagIbigDeduction(final double grossWage) {
        double contributionRate = (grossWage <= PAGIBIG_MAX_SALARY) ? PAGIBIG_RATE_1000_TO_1500 : PAGIBIG_RATE_ABOVE_1500;
        return Math.min(grossWage * contributionRate, MAX_PAGIBIG_DEDUCTION);
    }

    /**
     * Calculates the PhilHealth monthly premium based on the employee's gross wage.
     *
     * @param grossWage The employee's gross wage.
     * @return The calculated PhilHealth premium amount.
     */
    private double calculatePhilHealthPremium(final double grossWage) {
        return Math.max(MIN_PHILHEALTH_DEDUCTION, Math.min(grossWage * PHILHEALTH_PREMIUM_RATE, MAX_PHILHEALTH_DEDUCTION));
    }
}
