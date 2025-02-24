package com.mycompany.motorph.calculation;

import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;

/**
 * A class that calculates withholding tax using gross wage and deductions.
 * <p>
 * It calculates the withholding tax based on taxable income derived from 
 * gross wage after deducting SSS, PhilHealth, and Pag-IBIG contributions.
 * </p>
 *
 * @author Lance
 */
class WithholdingTaxCalculation {

    private final SSSDeduction sssDeduction;
    private final HealthInsurancesDeduction healthInsuranceDeduction;

    // Tax brackets and rates
    private static final double[] TAX_BRACKETS = {20832, 33333, 66667, 166667, 666667};
    private static final double[] TAX_RATES = {0.20, 0.25, 0.30, 0.32, 0.35};

    /**
     * Constructor for WithholdingTaxCalculation.
     */
    public WithholdingTaxCalculation() {
        this.sssDeduction = new SSSDeduction();
        this.healthInsuranceDeduction = new HealthInsurancesDeduction();
    }

    /**
     * Calculates the withholding tax based on gross wage.
     *
     * @param grossWage Employee's gross wage
     * @return Withholding tax amount
     * @throws IOException If an I/O error occurs while calculating deductions
     * @throws CsvValidationException If there is an error in CSV validation
     */
    public double calculateWithholdingTax(final double grossWage) throws IOException, CsvValidationException {
        final double taxableIncome = calculateTaxableIncome(grossWage);

        if (taxableIncome <= 0) {
            return 0.0;
        }

        return taxableIncome * getApplicableTaxRate(taxableIncome);
    }

    /**
     * Computes taxable income by subtracting monthly deductions from gross wage.
     *
     * @param grossWage Employee's gross wage
     * @return Taxable income after deductions
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If CSV validation fails
     */
    private double calculateTaxableIncome(final double grossWage) throws IOException, CsvValidationException {
        final double deductions = computeMonthlyDeductions(grossWage);
        return Math.max(grossWage - deductions, 0);
    }

    /**
     * Computes total monthly deductions (SSS, PhilHealth, Pag-IBIG).
     *
     * @param grossWage Employee's gross wage
     * @return Total monthly deductions
     * @throws IOException If an I/O error occurs
     * @throws CsvValidationException If CSV validation fails
     */
    private double computeMonthlyDeductions(final double grossWage) throws IOException, CsvValidationException {
        return sssDeduction.calculateSssDeduction(grossWage)
                + healthInsuranceDeduction.calculatePhilHealthDeduction(grossWage)
                + healthInsuranceDeduction.calculatePagIbigDeduction(grossWage);
    }

    /**
     * Retrieves the applicable tax rate based on taxable income.
     *
     * @param taxableIncome The employee's taxable income
     * @return The corresponding tax rate
     */
    private double getApplicableTaxRate(final double taxableIncome) {
        for (int i = TAX_BRACKETS.length - 1; i >= 0; i--) {
            if (taxableIncome >= TAX_BRACKETS[i]) {
                return TAX_RATES[i];
            }
        }
        return 0.0;
    }
}
