package com.mycompany.motorph;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * LoginPage for the MotorPH Payroll System.
 * Provides authentication using CSV-based credentials.
 *
 * @author Lance
 */
public class LoginPage extends javax.swing.JFrame implements EmployeeInformationManager {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color RED = new Color(191, 47, 47);

    /**
     * Constructor for LoginPage.
     */
    public LoginPage() {
        initComponents();
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setTitle("Login");
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMain = new JPanel(new GridLayout(6, 2, 10, 10));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(Color.WHITE);

        lblWelcomeHeader = new JLabel("Welcome!", SwingConstants.CENTER);
        lblWelcomeHeader.setFont(new Font("Leelawadee", Font.BOLD, 18));

        lblLoginHeader = new JLabel("Login", SwingConstants.CENTER);
        lblLoginHeader.setFont(new Font("Leelawadee", Font.BOLD, 16));
        lblLoginHeader.setOpaque(true);
        lblLoginHeader.setBackground(RED);
        lblLoginHeader.setForeground(Color.WHITE);

        lblUserType = new JLabel("Select User Type:");
        cmbUserType = new JComboBox<>(new String[]{"Employee", "Admin", "IT"});
        cmbUserType.setFocusable(false);

        lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(15);

        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(15);
        txtPassword.setEchoChar('*');

        chkShowPassword = new JCheckBox("Show password");
        chkShowPassword.setFocusable(false);
        chkShowPassword.addActionListener(e -> togglePasswordVisibility());

        btnLogin = createButton("Login", e -> performLogin());

        pnlMain.add(lblWelcomeHeader);
        pnlMain.add(lblLoginHeader);
        pnlMain.add(lblUserType);
        pnlMain.add(cmbUserType);
        pnlMain.add(lblUsername);
        pnlMain.add(txtUsername);
        pnlMain.add(lblPassword);
        pnlMain.add(txtPassword);
        pnlMain.add(chkShowPassword);
        pnlMain.add(btnLogin);

        add(pnlMain);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Creates a styled JButton with an action listener.
     */
    private JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setBackground(WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Toggles password visibility.
     */
    private void togglePasswordVisibility() {
        if (chkShowPassword.isSelected()) {
            txtPassword.setEchoChar((char) 0);
        } else {
            txtPassword.setEchoChar('*');
        }
    }

    /**
     * Handles login authentication.
     */
    private void performLogin() {
        String enteredUsername = txtUsername.getText().trim();
        String enteredPassword = new String(txtPassword.getPassword()).trim();
        String selectedUserType = cmbUserType.getSelectedItem().toString();

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showErrorDialog("Username and password cannot be empty.");
            return;
        }

        boolean loginSuccessful = authenticateUser(enteredUsername, enteredPassword, selectedUserType);

        if (loginSuccessful) {
            JOptionPane.showMessageDialog(this, "Login successful as " + selectedUserType);
            openDashboard(selectedUserType);
        } else {
            showErrorDialog("Invalid credentials or user type.");
        }
    }

    /**
     * Reads CSV and verifies user credentials.
     */
    private boolean authenticateUser(String username, String password, String userType) {
        String csvFilePath = "src/main/resources/data/login_credentials.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            // Skip header row
            reader.readNext();

            String[] record;
            while ((record = reader.readNext()) != null) {
                if (record.length < 3) continue; // Ensure record has enough fields

                String csvUsername = record[0].trim();
                String csvPassword = record[1].trim();
                String csvUserType = record[2].trim();

                if (username.equals(csvUsername) && password.equals(csvPassword) && userType.equalsIgnoreCase(csvUserType)) {
                    return true;
                }
            }
        } catch (IOException | CsvValidationException e) {
            showErrorDialog("Error reading credentials file.");
        }
        return false;
    }

    /**
     * Opens the appropriate dashboard based on user type.
     */
    private void openDashboard(String userType) {
        switch (userType.toLowerCase()) {
            case "employee":
            case "admin":
            case "it":
                new MotorPHMainMenu().setVisible(true);
                dispose();
                break;
            default:
                showErrorDialog("Unknown user type.");
        }
    }

    /**
     * Displays an error dialog with the given message.
     */
    @Override
    public void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Main method to launch the Login Page.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPage::new);
    }

    // UI Components
    private JPanel pnlMain;
    private JLabel lblWelcomeHeader, lblLoginHeader, lblUserType, lblUsername, lblPassword;
    private JComboBox<String> cmbUserType;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
    private JButton btnLogin;
}
