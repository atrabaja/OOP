package com.mycompany.motorph;

import com.mycompany.motorph.employee.EmployeeInformation;
import com.mycompany.motorph.model.Employee;
import com.opencsv.exceptions.CsvValidationException;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Employee Search Page for MotorPH Payroll System.
 * 
 * Allows users to select an employee and view their full information.
 * Implements EmployeeInformationManager interface.
 * 
 * @author Lance
 */
public class EmployeeSearchPage extends javax.swing.JFrame implements EmployeeInformationManager {

    // Color Constants
    private static final Color LIGHT_BLUE = new Color(203, 203, 239);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color RED = new Color(191, 47, 47);

    private boolean toggleOnButtonClicked = false;
    private int clickCount = 0;

    /**
     * Creates new EmployeeSearchPage.
     */
    public EmployeeSearchPage() {
        initComponents();
        populateEmployeeTable();
        setupTableMouseListener();
    }

    /**
     * Initializes UI components.
     */
    private void initComponents() {
        setTitle("Employee Search");
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);

        lblMotorPhHeader = new JLabel("MotorPH Payroll System", SwingConstants.CENTER);
        lblMotorPhHeader.setFont(new Font("Leelawadee", Font.BOLD, 18));

        lblEmployeeSearchHeader = new JLabel("Employee Search", SwingConstants.CENTER);
        lblEmployeeSearchHeader.setFont(new Font("Leelawadee", Font.BOLD, 16));
        lblEmployeeSearchHeader.setOpaque(true);
        lblEmployeeSearchHeader.setBackground(new Color(223, 54, 54));
        lblEmployeeSearchHeader.setForeground(Color.WHITE);

        // Table for employee search
        tblBasicEmployeeInformation = new JTable(new DefaultTableModel(
            new Object[][] {},
            new String[] {"Employee Number", "Last Name", "First Name", "SSS #", "PhilHealth #", "TIN", "Pag-IBIG #"}
        ));
        tblBasicEmployeeInformation.setRowHeight(24);
        tblBasicEmployeeInformation.getTableHeader().setReorderingAllowed(false);
        tblBasicEmployeeInformation.setEnabled(false);
        JScrollPane tableScrollPane = new JScrollPane(tblBasicEmployeeInformation);

        lblEmployeeSelectionToggle = new JLabel("Employee Selection Toggle", SwingConstants.CENTER);
        lblEmployeeSelectionToggle.setFont(new Font("Leelawadee UI", Font.PLAIN, 14));

        tglOn = createToggleButton("On", e -> handleToggleOn());
        tglOff = createToggleButton("Off", e -> handleToggleOff());
        tglOff.setSelected(true);

        JPanel togglePanel = new JPanel();
        togglePanel.add(tglOn);
        togglePanel.add(tglOff);

        btnBack = createButton("Back", e -> dispose());
        btnExit = createButton("Exit", e -> System.exit(0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnBack);
        buttonPanel.add(btnExit);

        pnlMain.add(lblMotorPhHeader, BorderLayout.NORTH);
        pnlMain.add(lblEmployeeSearchHeader, BorderLayout.CENTER);
        pnlMain.add(tableScrollPane, BorderLayout.CENTER);
        pnlMain.add(lblEmployeeSelectionToggle, BorderLayout.SOUTH);
        pnlMain.add(togglePanel, BorderLayout.SOUTH);
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
     * Creates a JToggleButton with the specified text and action listener.
     */
    private JToggleButton createToggleButton(String text, ActionListener actionListener) {
        JToggleButton button = new JToggleButton(text);
        button.addActionListener(actionListener);
        button.setBackground(WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Handles enabling selection of employees.
     */
    private void handleToggleOn() {
        clickCount++;
        toggleOnButtonClicked = (clickCount % 2 != 0);
        tblBasicEmployeeInformation.setEnabled(toggleOnButtonClicked);
        tglOn.setSelected(toggleOnButtonClicked);
        tglOff.setSelected(!toggleOnButtonClicked);
    }

    /**
     * Handles disabling selection of employees.
     */
    private void handleToggleOff() {
        clickCount = 0;
        toggleOnButtonClicked = false;
        tblBasicEmployeeInformation.setEnabled(false);
        tglOn.setSelected(false);
        tglOff.setSelected(true);
    }

    /**
     * Populates the employee table with all employees' basic information.
     */
    private void populateEmployeeTable() {
        try {
            EmployeeInformation employeeInformation = new EmployeeInformation();
            List<Employee> employees = employeeInformation.getAllEmployees();

            DefaultTableModel model = (DefaultTableModel) tblBasicEmployeeInformation.getModel();
            model.setRowCount(0);

            for (Employee employee : employees) {
                model.addRow(new Object[]{
                    employee.getEmployeeNumber(),
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getSssNumber(),
                    employee.getPhilHealthNumber(),
                    employee.getTin(),
                    employee.getPagIbigNumber()
                });
            }
        } catch (IOException | ParseException | CsvValidationException e) {
            showErrorDialog("Error loading employee data: " + e.getMessage());
        }
    }

    /**
     * Adds a mouse listener to the employee table to handle row clicks.
     */
    private void setupTableMouseListener() {
        tblBasicEmployeeInformation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = tblBasicEmployeeInformation.getSelectedRow();
                if (rowIndex != -1 && toggleOnButtonClicked) {
                    showEmployeeInformation(rowIndex);
                }
            }
        });
    }

    /**
     * Displays the selected employee's information.
     */
    private void showEmployeeInformation(int rowIndex) {
        try {
            DefaultTableModel model = (DefaultTableModel) tblBasicEmployeeInformation.getModel();
            int employeeNumber = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());

            EmployeeInformation employeeInformation = new EmployeeInformation();
            List<String> employeeDetails = employeeInformation.showEmployeeInformation(employeeNumber);

            new EmployeeInformationFrame(employeeDetails).setVisible(true);
        } catch (IOException | ParseException | CsvValidationException e) {
            showErrorDialog("Error displaying employee details: " + e.getMessage());
        }
    }

    /**
     * Displays an error dialog with a given message.
     */
    @Override
    public void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(this, "Error: " + errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Main method to launch the Employee Search Page.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeSearchPage::new);
    }
}
