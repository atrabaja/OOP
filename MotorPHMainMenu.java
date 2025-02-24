package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;

/**
 * Main Menu for the MotorPH application.
 * <p>
 * Provides navigation to employee search and leave management.
 * </p>
 * 
 * @author Lance
 */
public class MotorPHMainMenu extends javax.swing.JFrame {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = Color.WHITE;
    private static final Color RED = new Color(191, 47, 47);

    /**
     * Constructor for MotorPHMainMenu.
     */
    public MotorPHMainMenu() {
        initComponents();
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setTitle("Payroll System Main Menu");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pnlMain = new JPanel(new GridLayout(6, 1, 10, 10));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(WHITE);

        lblMotorPhHeader = new JLabel("MotorPH Payroll System", SwingConstants.CENTER);
        lblMotorPhHeader.setFont(new Font("Leelawadee", Font.BOLD, 18));

        lblMainMenuHeader = new JLabel("Main Menu", SwingConstants.CENTER);
        lblMainMenuHeader.setFont(new Font("Leelawadee", Font.BOLD, 16));
        lblMainMenuHeader.setOpaque(true);
        lblMainMenuHeader.setBackground(RED);
        lblMainMenuHeader.setForeground(WHITE);

        lblIwantTo = new JLabel("I want to...", SwingConstants.LEFT);
        lblIwantTo.setFont(new Font("Leelawadee UI", Font.BOLD, 14));

        btnSearchEmployee = createButton("Search an Employee", e -> openEmployeeSearch());
        btnManageLeave = createButton("Manage Leave", e -> openManageLeave());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(WHITE);
        btnGoBackToLogin = createButton("Go Back to Login", e -> returnToLogin());
        btnExit = createButton("Exit", e -> System.exit(0));
        buttonPanel.add(btnGoBackToLogin);
        buttonPanel.add(btnExit);

        pnlMain.add(lblMotorPhHeader);
        pnlMain.add(lblMainMenuHeader);
        pnlMain.add(lblIwantTo);
        pnlMain.add(btnSearchEmployee);
        pnlMain.add(btnManageLeave);
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
     * Opens the Employee Search Page.
     */
    private void openEmployeeSearch() {
        new EmployeeSearchPage().setVisible(true);
    }

    /**
     * Opens the Manage Leave Page.
     */
    private void openManageLeave() {
        new ManageLeaveMenu().setVisible(true);
    }

    /**
     * Returns to the login screen.
     */
    private void returnToLogin() {
        new LoginPage().setVisible(true);
        this.dispose();
    }

    /**
     * Main method to launch the Main Menu.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MotorPHMainMenu::new);
    }

    // UI Components
    private JPanel pnlMain;
    private JLabel lblMotorPhHeader, lblMainMenuHeader, lblIwantTo;
    private JButton btnSearchEmployee, btnManageLeave, btnGoBackToLogin, btnExit;
}
