package com.mycompany.motorph.calculation;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that calculates SSS deductions based on gross wage.
 * <p>
 * It reads the SSS deduction table from a CSV file and determines the deduction
 * based on compensation ranges.
 * </p>
 * 
 * @author Lance
 */
public class SSSDeduction {

    // File path for the SSS deductions data
    private static final String SSS_DEDUCTIONS_PATH = "src/main/resources/data/sss_deduction.csv";

    // Constants for predefined deduction range limits
    private static final double MIN_COMPENSATION_RANGE = 3250.00;
    private static final double MAX_COMPENSATION_RANGE = 24750.00;
    private static final double MIN_DEDUCTION = 135.00;
    private static final double MAX_DEDUCTION = 1125.00;
    private static final int EXPECTED_CSV_COLUMNS = 3;

    private final List<double[]> sssCompensationRanges = new ArrayList<>();
    private final List<Double> sssDeductions = new ArrayList<>();
    private boolean dataLoaded = false;

    /**
     * Calculates SSS deduction based on gross wage.
     *
     * @param grossWage The gross wage of the employee.
     * @return The calculated SSS deduction.
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws CsvValidationException If CSV validation fails.
     */
    public double calculateSssDeduction(final double grossWage) throws IOException, CsvValidationException {
        if (!dataLoaded) {
            loadSSSDeductions();
            dataLoaded = true;
        }

        // Directly return minimum or maximum deduction if out of range
        if (grossWage < MIN_COMPENSATION_RANGE) {
            return MIN_DEDUCTION;
        } 
        if (grossWage > MAX_COMPENSATION_RANGE) {
            return MAX_DEDUCTION;
        }

        // Iterate over stored compensation ranges and find matching deduction
        for (int i = 0; i < sssCompensationRanges.size(); i++) {
            final double[] range = sssCompensationRanges.get(i);
            if (grossWage >= range[0] && grossWage <= range[1]) {
                return sssDeductions.get(i);
            }
        }

        // Default to 0 if no matching range is found (should never happen)
        return 0.0;
    }

    /**
     * Reads the SSS deductions data from a CSV file and populates the compensation ranges.
     *
     * @throws IOException If an I/O error occurs while reading the file.
     * @throws CsvValidationException If CSV validation fails.
     */
    private void loadSSSDeductions() throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(SSS_DEDUCTIONS_PATH))) {
            String[] row;

            // Read and skip header row
            reader.readNext();

            while ((row = reader.readNext()) != null) {
                if (row.length != EXPECTED_CSV_COLUMNS) {
                    throw new IllegalArgumentException("Invalid CSV format: Expected " + EXPECTED_CSV_COLUMNS + " columns but got " + row.length + " in row: " + String.join(",", row));
                }

                try {
                    double lowerRange = Double.parseDouble(row[0].trim());
                    double upperRange = Double.parseDouble(row[1].trim());
                    double deduction = Double.parseDouble(row[2].trim());

                    sssCompensationRanges.add(new double[]{lowerRange, upperRange});
                    sssDeductions.add(deduction);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number format in SSS deductions CSV: " + String.join(",", row), e);
                }
            }
        }
    }
}
