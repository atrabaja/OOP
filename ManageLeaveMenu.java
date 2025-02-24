package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;

/**
 * A class that represents the Leave Management Menu in the application.
 * Allows users to navigate to either the leave application page or the leave balance page.
 * 
 * @author Lance
 */
public class ManageLeaveMenu extends javax.swing.JFrame {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = Color.WHITE;
    private static final Color RED = new Color(191, 47, 47);

    /**
     * Constructor for ManageLeaveMenu.
     */
    public ManageLeaveMenu() {
        initComponents();
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setTitle("Leave Management Menu");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pnlMain = new JPanel(new GridLayout(5, 1, 10, 10));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(WHITE);

        lblMotorPhHeader = new JLabel("MotorPH Payroll System", SwingConstants.CENTER);
        lblMotorPhHeader.setFont(new Font("Leelawadee", Font.BOLD, 18));

        lblLeaveManagementMenuHeader = new JLabel("Leave Management Menu", SwingConstants.CENTER);
        lblLeaveManagementMenuHeader.setFont(new Font("Leelawadee", Font.BOLD, 16));
        lblLeaveManagementMenuHeader.setOpaque(true);
        lblLeaveManagementMenuHeader.setBackground(RED);
        lblLeaveManagementMenuHeader.setForeground(WHITE);

        btnLeaveApplication = createButton("Leave Application", e -> openLeaveApplication());
        btnLeaveBalance = createButton("Leave Balance", e -> openLeaveBalance());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(WHITE);
        btnBack = createButton("Back", e -> dispose());
        btnExit = createButton("Exit", e -> System.exit(0));
        buttonPanel.add(btnBack);
        buttonPanel.add(btnExit);

        pnlMain.add(lblMotorPhHeader);
        pnlMain.add(lblLeaveManagementMenuHeader);
        pnlMain.add(btnLeaveApplication);
        pnlMain.add(btnLeaveBalance);
        pnlMain.add(buttonPanel);

        add(pnlMain);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Creates a styled JButton with hover effects.
     */
    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBackground(WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(actionListener);

        // Button Hover Effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(LIGHT_BLUE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WHITE);
            }
        });

        return button;
    }

    /**
     * Opens the Leave Application Page.
     */
    private void openLeaveApplication() {
        new LeaveApplicationPage().setVisible(true);
    }

    /**
     * Opens the Leave Balance Page.
     */
    private void openLeaveBalance() {
        new LeaveBalancePage().setVisible(true);
    }

    /**
     * Main method to launch the Manage Leave Menu.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageLeaveMenu::new);
    }

    // UI Components
    private JPanel pnlMain;
    private JLabel lblMotorPhHeader, lblLeaveManagementMenuHeader;
    private JButton btnLeaveApplication, btnLeaveBalance, btnBack, btnExit;
}
