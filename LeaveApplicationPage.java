package com.mycompany.motorph;

import com.mycompany.motorph.data.LeaveDataManager;
import static com.mycompany.motorph.model.DateRange.createDateRange;
import com.mycompany.motorph.model.Leave;
import com.opencsv.exceptions.CsvValidationException;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Leave Application Page for MotorPH Payroll System.
 * 
 * Allows employees to apply for leave by selecting leave type, dates, and providing a reason.
 * Implements EmployeeInformationManager interface.
 * 
 * @author Lance
 */
public class LeaveApplicationPage extends javax.swing.JFrame implements EmployeeInformationManager {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color RED = new Color(191, 47, 47);

    /**
     * Constructor for LeaveApplicationPage.
     */
    public LeaveApplicationPage() {
        initComponents();
        setupDateChoosers();
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setTitle("Leave Application");
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        lblMotorPhHeader = new JLabel("MotorPH Payroll System", SwingConstants.CENTER);
        lblMotorPhHeader.setFont(new Font("Leelawadee", Font.BOLD, 18));

        lblLeaveApplicationHeader = new JLabel("Leave Application", SwingConstants.CENTER);
        lblLeaveApplicationHeader.setFont(new Font("Leelawadee", Font.BOLD, 16));
        lblLeaveApplicationHeader.setOpaque(true);
        lblLeaveApplicationHeader.setBackground(new Color(223, 54, 54));
        lblLeaveApplicationHeader.setForeground(Color.WHITE);

        lblEmployeeNumber = new JLabel("Employee #: ");
        txtEmployeeNumber = new JTextField(10);

        lblLeaveType = new JLabel("Leave Type: ");
        cmbLeaveType = new JComboBox<>(new String[] {"Sick Leave", "Vacation Leave", "Emergency Leave"});

        lblStartDate = new JLabel("Start Date (MM/DD): ");
        lblEndDate = new JLabel("End Date (MM/DD): ");

        jdcStartDate = new JDateChooser();
        jdcStartDate.setDateFormatString("MM/dd");

        jdcEndDate = new JDateChooser();
        jdcEndDate.setDateFormatString("MM/dd");

        lblReasonForLeave = new JLabel("Reason for Leave: ");
        txaReasonForLeave = new JTextArea(3, 20);
        txaReasonForLeave.setLineWrap(true);
        txaReasonForLeave.setWrapStyleWord(true);
        JScrollPane scrollReason = new JScrollPane(txaReasonForLeave);

        btnApply = createButton("Apply", e -> applyLeave());
        btnBack = createButton("Back", e -> dispose());
        btnExit = createButton("Exit", e -> System.exit(0));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(lblEmployeeNumber);
        inputPanel.add(txtEmployeeNumber);
        inputPanel.add(lblLeaveType);
        inputPanel.add(cmbLeaveType);
        inputPanel.add(lblStartDate);
        inputPanel.add(jdcStartDate);
        inputPanel.add(lblEndDate);
        inputPanel.add(jdcEndDate);
        inputPanel.add(lblReasonForLeave);
        inputPanel.add(scrollReason);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnBack);
        buttonPanel.add(btnApply);
        buttonPanel.add(btnExit);

        pnlMain.add(lblMotorPhHeader, BorderLayout.NORTH);
        pnlMain.add(lblLeaveApplicationHeader, BorderLayout.CENTER);
        pnlMain.add(inputPanel, BorderLayout.CENTER);
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
     * Configures date choosers to be non-editable and have a white background.
     */
    private void setupDateChoosers() {
        configureDateChooser(jdcStartDate);
        configureDateChooser(jdcEndDate);
    }

    /**
     * Makes a JDateChooser non-editable and have a white background.
     */
    private void configureDateChooser(JDateChooser dateChooser) {
        ((JTextFieldDateEditor) dateChooser.getDateEditor()).setEditable(false);
        ((JTextFieldDateEditor) dateChooser.getDateEditor()).getUiComponent().setBackground(Color.WHITE);
    }

    /**
     * Handles applying for leave.
     */
    private void applyLeave() {
        try {
            int employeeNumber = Integer.parseInt(txtEmployeeNumber.getText().trim());

            if (employeeNumber <= 0 || employeeNumber > 34) {
                showErrorDialog("Employee not found.");
                return;
            }

            String leaveType = (String) cmbLeaveType.getSelectedItem();

            Date startDate = jdcStartDate.getDate();
            if (startDate == null) {
                showErrorDialog("Please select a start date.");
                return;
            }
            String startDateStr = formatDate(startDate);

            Date endDate = jdcEndDate.getDate();
            if (endDate == null) {
                showErrorDialog("Please select an end date.");
                return;
            }
            String endDateStr = formatDate(endDate);

            String reason = txaReasonForLeave.getText().trim();
            if (reason.isEmpty()) {
                showErrorDialog("Please provide a reason for leave.");
                return;
            }

            Leave leave = new Leave(employeeNumber, leaveType, startDateStr, endDateStr, reason);
            createDateRange(startDateStr, endDateStr);

            LeaveDataManager leaveDataManager = new LeaveDataManager();
            leaveDataManager.saveLeaveApplication(leave);

            showInformationDialog("Leave application submitted successfully.");
        } catch (NumberFormatException e) {
            showErrorDialog("Invalid employee number.");
        } catch (ParseException | IOException | CsvValidationException e) {
            showErrorDialog("Error submitting leave: " + e.getMessage());
        }
    }

    /**
     * Formats date from JDateChooser to MM/dd format.
     */
    private String formatDate(Date date) {
        return new SimpleDateFormat("MM/dd").format(date);
    }

    /**
     * Displays an error dialog.
     */
    @Override
    public void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an information dialog.
     */
    private void showInformationDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method to launch the Leave Application Page.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LeaveApplicationPage::new);
    }
}
