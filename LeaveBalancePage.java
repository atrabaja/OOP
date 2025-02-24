package com.mycompany.motorph;

import com.mycompany.motorph.data.LeaveDataManager;
import com.mycompany.motorph.employee.EmployeeInformation;
import com.mycompany.motorph.model.Leave;
import com.mycompany.motorph.util.CurrencyUtil;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Leave Balance Page for MotorPH Payroll System.
 * 
 * Allows employees to view their remaining leave balances.
 * Implements EmployeeInformationManager interface.
 * 
 * @author Lance
 */
public class LeaveBalancePage extends javax.swing.JFrame implements EmployeeInformationManager {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color RED = new Color(191, 47, 47);

    /**
     * Constructor for LeaveBalancePage.
     */
    public LeaveBalancePage() {
        initComponents();
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setTitle("Leave Balance");
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        lblMotorPhHeader = new JLabel("MotorPH Payroll System", SwingConstants.CENTER);
        lblMotorPhHeader.setFont(new Font("Leelawadee", Font.BOLD, 18));

        lblLeaveBalanceHeader = new JLabel("Leave Balance", SwingConstants.CENTER);
        lblLeaveBalanceHeader.setFont(new Font("Leelawadee", Font.BOLD, 16));
        lblLeaveBalanceHeader.setOpaque(true);
        lblLeaveBalanceHeader.setBackground(new Color(223, 54, 54));
        lblLeaveBalanceHeader.setForeground(Color.WHITE);

        lblEmployeeNumber = new JLabel("Employee #: ");
        txtEmployeeNumber = new JTextField(10);

        btnSearch = createButton("Search", e -> populateEmployeeInformation());
        btnBack = createButton("Back", e -> dispose());
        btnExit = createButton("Exit", e -> System.exit(0));

        lblFirstName = new JLabel("First Name: ");
        txtFirstName = createNonEditableTextField();

        lblLastName = new JLabel("Last Name: ");
        txtLastName = createNonEditableTextField();

        lblStartDate = new JLabel("Start Date (MM/DD): ");
        txtStartDate = createNonEditableTextField();

        lblEndDate = new JLabel("End Date (MM/DD): ");
        txtEndDate = createNonEditableTextField();

        lblSickLeave = new JLabel("Sick Leave: ");
        txtSickLeave = createNonEditableTextField();

        lblVacationLeave = new JLabel("Vacation Leave: ");
        txtVacationLeave = createNonEditableTextField();

        lblEmergencyLeave = new JLabel("Emergency Leave: ");
        txtEmergencyLeave = createNonEditableTextField();

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(lblEmployeeNumber);
        inputPanel.add(txtEmployeeNumber);
        inputPanel.add(btnSearch);

        JPanel employeeInfoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        employeeInfoPanel.add(lblFirstName);
        employeeInfoPanel.add(txtFirstName);
        employeeInfoPanel.add(lblLastName);
        employeeInfoPanel.add(txtLastName);
        employeeInfoPanel.add(lblStartDate);
        employeeInfoPanel.add(txtStartDate);
        employeeInfoPanel.add(lblEndDate);
        employeeInfoPanel.add(txtEndDate);

        JPanel leaveInfoPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        leaveInfoPanel.add(lblSickLeave);
        leaveInfoPanel.add(txtSickLeave);
        leaveInfoPanel.add(lblVacationLeave);
        leaveInfoPanel.add(txtVacationLeave);
        leaveInfoPanel.add(lblEmergencyLeave);
        leaveInfoPanel.add(txtEmergencyLeave);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnBack);
        buttonPanel.add(btnExit);

        pnlMain.add(lblMotorPhHeader, BorderLayout.NORTH);
        pnlMain.add(lblLeaveBalanceHeader, BorderLayout.CENTER);
        pnlMain.add(inputPanel, BorderLayout.CENTER);
        pnlMain.add(employeeInfoPanel, BorderLayout.CENTER);
        pnlMain.add(leaveInfoPanel, BorderLayout.CENTER);
        pnlMain.add(buttonPanel, BorderLayout.SOUTH);

        add(pnlMain);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Creates a JButton with the specified text and action listener.
     */
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBackground(WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a non-editable JTextField.
     */
    private JTextField createNonEditableTextField() {
        JTextField textField = new JTextField(10);
        textField.setEditable(false);
        textField.setBackground(new Color(242, 242, 242));
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    /**
     * Handles searching for employee leave balances.
     */
    private void populateEmployeeInformation() {
        try {
            int employeeNumber = Integer.parseInt(txtEmployeeNumber.getText().trim());

            if (employeeNumber <= 0 || employeeNumber > 34) {
                showErrorDialog("Employee not found.");
                return;
            }

            List<String> employeeInfo = new EmployeeInformation().showEmployeeInformation(employeeNumber);
            List<Leave> leaves = new LeaveDataManager().getLeavesByEmployeeNumber(employeeNumber);

            if (leaves.isEmpty()) {
                showErrorDialog("No leave data found for this employee.");
                return;
            }

            Leave leaveInfo = leaves.get(0); // Assuming most recent leave

            updateEmployeeInformationFields(employeeInfo, leaveInfo);
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid employee number.");
        } catch (ParseException | IOException | CsvValidationException e) {
            showErrorDialog("Error retrieving leave information: " + e.getMessage());
        }
    }

    /**
     * Displays an error dialog.
     */
    @Override
    public void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates employee leave balance fields.
     */
    private void updateEmployeeInformationFields(List<String> employeeInfo, Leave leaveInfo) {
        txtFirstName.setText(employeeInfo.get(0));
        txtLastName.setText(employeeInfo.get(1));
        txtStartDate.setText(leaveInfo.getStartDate());
        txtEndDate.setText(leaveInfo.getEndDate());

        txtSickLeave.setText(CurrencyUtil.formatCurrency(leaveInfo.getSickLeaveAmount()));
        txtVacationLeave.setText(CurrencyUtil.formatCurrency(leaveInfo.getVacationLeaveAmount()));
        txtEmergencyLeave.setText(CurrencyUtil.formatCurrency(leaveInfo.getEmergencyLeaveAmount()));
    }

    /**
     * Main method to launch the Leave Balance Page.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LeaveBalancePage::new);
    }
}
