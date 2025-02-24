/*
 * Employee Information Frame for MotorPH Payroll System
 */
package com.mycompany.motorph;

import com.mycompany.motorph.calculation.WageCalculation;
import com.mycompany.motorph.employee.EmployeeInformation;
import com.mycompany.motorph.model.DateRange;
import static com.mycompany.motorph.model.DateRange.createMonthRange;
import com.opencsv.exceptions.CsvValidationException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Displays an employee's pay and personal information for a selected month.
 * Allows updating and deletion of employee details.
 * Implements EmployeeInformationManager interface.
 * 
 * @author Lance
 */
public class EmployeeInformationFrame extends javax.swing.JFrame implements EmployeeInformationManager {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color RED = new Color(191, 47, 47);
    private static final Color GRAY = new Color(242, 242, 242);

    private boolean deleteButtonClicked = false;

    private JButton btnCompute, btnSave, btnBack, btnExit, btnDeleteInfo, btnUpdateInfo;
    private JComboBox<String> cmbMonth;
    private JLabel lblInstruction, lblEmployeeNumber, lblEmployeeInformationHeader;
    private JPanel pnlTop, pnlMain;
    private JScrollPane scrollPaneMain;
    private JTextField txtEmployeeNumber;
    
    /**
     * Constructor: Initializes EmployeeInformationFrame.
     * 
     * @param employeeInformation List of employee details.
     */
    public EmployeeInformationFrame(List<String> employeeInformation) {
        initComponents();
        setupFrame(employeeInformation);
        assignClickHandlersToTextFields();
    }

    /**
     * Sets up the frame layout and components.
     * 
     * @param employeeDetails List of employee details.
     */
    private void setupFrame(List<String> employeeDetails) {
        setLayout(new BorderLayout());
        initializePnlTop(employeeDetails);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates the top panel with month selection and compute button.
     * 
     * @param employeeDetails List of employee details.
     */
    private void initializePnlTop(List<String> employeeDetails) {
        lblInstruction = new JLabel("Select the month for wage information");
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        cmbMonth = new JComboBox<>(months);

        btnCompute = createButton("Compute", e -> {
            String selectedMonth = String.format("%02d", cmbMonth.getSelectedIndex() + 1);
            showInformation(employeeDetails, selectedMonth);
        });

        btnCompute.addMouseListener(createButtonHoverAdapter(btnCompute, LIGHT_BLUE, WHITE));
        btnCompute.setBackground(WHITE);
        btnCompute.setCursor(new Cursor(Cursor.HAND_CURSOR));

        scrollPaneMain.setVisible(false);

        pnlTop = new JPanel();
        pnlTop.setBorder(new EmptyBorder(12, 12, 12, 12));
        pnlTop.setBackground(Color.WHITE);
        pnlTop.add(lblInstruction);
        pnlTop.add(cmbMonth);

        cmbMonth.setFocusable(false);
        btnCompute.setFocusable(false);

        add(pnlTop, BorderLayout.NORTH);
        add(btnCompute, BorderLayout.CENTER);
    }

    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    private MouseAdapter createButtonHoverAdapter(JButton button, Color hoverColor, Color defaultColor) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
        };
    }

    private void showInformation(List<String> employeeDetails, String selectedMonth) {
        remove(pnlTop);
        remove(btnCompute);

        populateEmployeeInformation(employeeDetails.get(0));

        scrollPaneMain.setPreferredSize(new Dimension(603, 627));
        scrollPaneMain.setVisible(true);
        add(scrollPaneMain, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();

        populateWageInformation(selectedMonth);
    }

    private void populateEmployeeInformation(String employeeNumberString) {
        try {
            int employeeNumber = Integer.parseInt(employeeNumberString);
            List<String> employeeInfo = new EmployeeInformation().showEmployeeInformation(employeeNumber);
            updateEmployeeInformationFields(employeeInfo);
            btnDeleteInfo.setEnabled(true);
            btnUpdateInfo.setEnabled(true);
        } catch (IOException | ParseException | CsvValidationException e) {
            showErrorDialog("Error loading employee data: " + e.getMessage());
        }
    }

    private void populateWageInformation(String selectedMonth) {
        try {
            int employeeNumber = Integer.parseInt(txtEmployeeNumber.getText());
            new EmployeeInformation().showEmployeeInformation(employeeNumber);

            DateRange dateRange = createMonthRange(selectedMonth);
            WageCalculation wageCalculation = new WageCalculation();
            List<String> wageInfo = wageCalculation.calculateWage(employeeNumber, dateRange);
            updateWageInformationFields(wageInfo, selectedMonth);
        } catch (IOException | ParseException | CsvValidationException e) {
            showErrorDialog("Error retrieving wage information: " + e.getMessage());
        }
    }

    private void updateWageInformationFields(List<String> wageInfo, String selectedMonth) {
        txtEmployeeNumber.setText(wageInfo.get(0));
    }

    private String getMonthName(String monthNumber) {
        String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        };
        int monthInt = Integer.parseInt(monthNumber);
        if (monthInt < 1 || monthInt > 12) {
            showErrorDialog("Invalid month number: " + monthNumber);
        }
        return months[monthInt - 1];
    }

    private void setFieldsEditable(boolean allowed) {
        JTextField[] textFields = { txtEmployeeNumber };
        for (JTextField field : textFields) {
            field.setBackground(allowed ? WHITE : GRAY);
            field.setEditable(allowed);
            field.setFocusable(allowed);
        }
        btnSave.setEnabled(true);
    }

    private void updateEmployeeInformation() {
        try {
            int employeeNumber = Integer.parseInt(txtEmployeeNumber.getText());
            List<String> updatedEmployeeInfo = new ArrayList<>();
            updatedEmployeeInfo.add(txtEmployeeNumber.getText());
            new EmployeeInformation().updateEmployeeInformationInCsv(employeeNumber, updatedEmployeeInfo);
            setFieldsEditable(false);
            deleteButtonClicked = false;
        } catch (IOException | ParseException | CsvValidationException e) {
            showErrorDialog("Error updating employee data: " + e.getMessage());
        }
    }

    @Override
    public void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(pnlMain, "Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
