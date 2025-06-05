package org.example.src.GUI;

import org.example.src.DataBaseHandlers.*;
import org.example.src.MainClasses.DeliveryBoy;
import org.example.src.MainClasses.Manager;
import org.example.src.MainClasses.SalesMan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

class TextFieldUtil {
    // Utility method to apply numeric-only constraint
    public static void applyNumericKeyListener(JTextField textField) {
        int maxLength = 8;
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) || textField.getText().length() >= maxLength) {
                    e.consume(); // Ignore non-numeric input or input beyond max length
                }
            }
        });
    }

    public static void applyAlphabeticKeyListener(JTextField textField) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume(); // Ignore non-alphabetic input
                }
            }
        });
    }
    public static void applyNumericKeyListenerWithMaxLength(JTextField textField, int maxLength) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) || textField.getText().length() >= maxLength) {
                    e.consume(); // Ignore non-numeric input or input beyond max length
                }
            }
        });
    }
}
public class ManagerGUI {


    public static void main(String[] args) {

        Manager manager = new Manager("Ahsan" , "Khan" , "42101-1234567-1" , 1,120000 , "ahsan_manager" , "securepassword");

        // Create the main frame for the GUI
        JFrame frame = new JFrame("Manager Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Title Label for the Manager Dashboard
        JLabel titleLabel = new JLabel("Manager Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tabbed Pane to organize different panels
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Salesman Panel
        JPanel addSalesmanPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        addSalesmanPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField smFirstNameField = new JTextField();
        TextFieldUtil.applyAlphabeticKeyListener(smFirstNameField);
        JTextField smLastNameField = new JTextField();
        TextFieldUtil.applyAlphabeticKeyListener(smLastNameField);
        JTextField smCnicField = new JTextField();
        TextFieldUtil.applyNumericKeyListenerWithMaxLength(smCnicField ,11);
        JTextField smSalaryField = new JTextField();
        TextFieldUtil.applyNumericKeyListener(smSalaryField);
        JTextField smUsernameField = new JTextField();
        JTextField smPasswordField = new JTextField();
        addSalesmanPanel.add(new JLabel("First Name:"));
        addSalesmanPanel.add(smFirstNameField);
        addSalesmanPanel.add(new JLabel("Last Name:"));
        addSalesmanPanel.add(smLastNameField);
        addSalesmanPanel.add(new JLabel("CNIC:"));
        addSalesmanPanel.add(smCnicField);
        addSalesmanPanel.add(new JLabel("Salary:"));
        addSalesmanPanel.add(smSalaryField);
        addSalesmanPanel.add(new JLabel("Username:"));
        addSalesmanPanel.add(smUsernameField);
        addSalesmanPanel.add(new JLabel("Password:"));
        addSalesmanPanel.add(smPasswordField);
        JButton saveSalesmanButton = new JButton("Save Salesman");
        addSalesmanPanel.add(saveSalesmanButton);
        JLabel ErrorLabel = new JLabel();
        ErrorLabel.setForeground(Color.RED);
        addSalesmanPanel.add(ErrorLabel);

        saveSalesmanButton.addActionListener(e -> {
            if (smFirstNameField.getText().isEmpty() || smLastNameField.getText().isEmpty() ||
                    smCnicField.getText().isEmpty() || smSalaryField.getText().isEmpty() ||
                    smUsernameField.getText().isEmpty() || smPasswordField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
            String DB_URL = "jdbc:sqlite:" + dbFilePath;
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement checkStmt = conn.prepareStatement("SELECT username, CNIC FROM SALESMAN WHERE username = ? OR CNIC = ?")) {

                String username = smUsernameField.getText();

                String cnic = smCnicField.getText();

                // Check if the user or CNIC already exists
                checkStmt.setString(1, username);
                checkStmt.setString(2, cnic);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    if (rs.getString("username").equals(username)) {
                        ErrorLabel.setText("Username already exists.");
                    } else if (rs.getString("CNIC").equals(cnic)) {
                        ErrorLabel.setText("CNIC already exists.");
                    }
                } else {
                    String result = "Salesman Added Successfully!\n"
                            + "\n"
                            + "Name: " + smFirstNameField.getText() + " " + smLastNameField.getText();
                    JOptionPane.showMessageDialog(frame, result, "Success", JOptionPane.INFORMATION_MESSAGE);


                    String firstName = smFirstNameField.getText();
                    String LastName = smLastNameField.getText();
                    int salary =  Integer.parseInt(smSalaryField.getText());
                    String password = smPasswordField.getText();
                    manager.addSalesman(new SalesMan(firstName ,LastName ,cnic ,salary , username , password));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                ErrorLabel.setText("Database error. Please try again.");
            }


        });






        // Add Delivery Boy Panel
        JLabel errorlabel = new JLabel("");
        JPanel addDeliveryBoyPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        addDeliveryBoyPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextField dbFirstNameField = new JTextField();
        TextFieldUtil.applyAlphabeticKeyListener(dbFirstNameField);
        JTextField dbLastNameField = new JTextField();
        TextFieldUtil.applyAlphabeticKeyListener(dbLastNameField);
        JTextField dbCnicField = new JTextField();
        TextFieldUtil.applyNumericKeyListenerWithMaxLength(dbCnicField , 11);
        JTextField dbSalaryField = new JTextField();
        TextFieldUtil.applyNumericKeyListener(dbSalaryField);


        addDeliveryBoyPanel.add(new JLabel("First Name:"));
        addDeliveryBoyPanel.add(dbFirstNameField);
        addDeliveryBoyPanel.add(new JLabel("Last Name:"));
        addDeliveryBoyPanel.add(dbLastNameField);
        addDeliveryBoyPanel.add(new JLabel("CNIC:"));
        addDeliveryBoyPanel.add(dbCnicField);
        addDeliveryBoyPanel.add(new JLabel("Salary:"));
        addDeliveryBoyPanel.add(dbSalaryField);
        JButton saveDeliveryBoyButton = new JButton("Save Delivery Boy");
        addDeliveryBoyPanel.add(saveDeliveryBoyButton);
        addDeliveryBoyPanel.add(errorlabel);

        saveDeliveryBoyButton.addActionListener(e -> {
            if (dbFirstNameField.getText().isEmpty() || dbLastNameField.getText().isEmpty() ||
                    dbCnicField.getText().isEmpty() || dbSalaryField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
            String DB_URL = "jdbc:sqlite:" + dbFilePath;

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement checkStmt = conn.prepareStatement("SELECT CNIC FROM DELIVERYBOY WHERE CNIC = ?")) {

                String cnic = dbCnicField.getText();
                checkStmt.setString(1, cnic);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    errorlabel.setText("CNIC already exists in the database : ");
                    // System.out.println("CNIC found in database: " + rs.getString("CNIC"));
                    return;
                }

                // If CNIC does not exist, proceed to add the delivery boy
                String result = "Delivery Boy Added Successfully!\n"
                        + "Name: " + dbFirstNameField.getText() + " " + dbLastNameField.getText();
                JOptionPane.showMessageDialog(frame, result, "Success", JOptionPane.INFORMATION_MESSAGE);

                String firstName = dbFirstNameField.getText();
                String lastName = dbLastNameField.getText();
                int salary = Integer.parseInt(dbSalaryField.getText());

                manager.addDeliveryBoy(new DeliveryBoy(firstName, lastName, cnic, salary));

            } catch (SQLException ex) {
                ex.printStackTrace();
                ErrorLabel.setText("Database error. Please try again.");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(frame, "Salary must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        TextFieldUtil.applyNumericKeyListener(dbSalaryField);



        // Retrieve Items Panel
        JPanel retrieveItemsPanel = new JPanel(new BorderLayout());
        JTextArea itemsDisplayArea = new JTextArea();
        itemsDisplayArea.setEditable(false);
        JScrollPane itemsScrollPane = new JScrollPane(itemsDisplayArea);
        JButton retrieveItemsButton = new JButton("Retrieve Items");
        retrieveItemsButton.addActionListener(e -> {
            String retrievedData = RetrieveItems.fetchItemsFromDatabase(); // Fetch items from database
            itemsDisplayArea.setText(retrievedData); // Display retrieved data in the text area
        });
        retrieveItemsPanel.add(retrieveItemsButton, BorderLayout.NORTH);
        retrieveItemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

        // Insert Items from CSV Panel
        JPanel insertItemsPanel = new JPanel(new BorderLayout());
        JTextArea csvDisplayArea = new JTextArea();
        csvDisplayArea.setEditable(false);
        JScrollPane csvScrollPane = new JScrollPane(csvDisplayArea);
        JButton insertCSVButton = new JButton("Insert Items from CSV");
        insertCSVButton.addActionListener(e -> {
            InsertItemsFromCSV.main(new String[0]); // Call InsertItemsFromCSV class
            csvDisplayArea.setText("CSV Data Inserted! Check the database for updated details.");
        });
        insertItemsPanel.add(insertCSVButton, BorderLayout.NORTH);
        insertItemsPanel.add(csvScrollPane, BorderLayout.CENTER);

        // View Bill Details Panel
        JPanel viewBillPanel = new JPanel(new BorderLayout());
        JPanel Panel = new JPanel(new FlowLayout());


        JTextArea billDisplayArea = new JTextArea();
        billDisplayArea.setEditable(false);
        JScrollPane billScrollPane = new JScrollPane(billDisplayArea);

        JButton viewBillButton = new JButton("View Bill Details");
        JLabel billIdButton = new JLabel("BILL ID : ");
        JTextField billIdField = new JTextField(8);
        TextFieldUtil.applyNumericKeyListenerWithMaxLength(billIdField , 4);



        viewBillButton.addActionListener(e -> {

            if (billIdField.getText().equals("")){
                JOptionPane.showMessageDialog(frame, "BILL ID not found. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }else{

                int billId = Integer.parseInt(billIdField.getText());

                String DB_URL = "jdbc:sqlite:src/main/java/org/example/src/DataBaseHandlers/database/testing.db";

                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement stmt = conn.prepareStatement("SELECT id FROM BILL WHERE id = ?")) {

                    stmt.setInt(1, billId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String billDetails = GetBillDetails.fetchBillDetails(billId); // Fetch bill details
                        billDisplayArea.setText(billDetails); // Display bill details in the text area

                    } else {
                        // Customer ID not found in the database
                        JOptionPane.showMessageDialog(frame, "BILL ID not found. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Database error. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        Panel.add(viewBillButton);
        Panel.add(billIdButton);
        Panel.add(billIdField);

        viewBillPanel.add(Panel, BorderLayout.NORTH);
        viewBillPanel.add(billScrollPane, BorderLayout.CENTER);


        // adding SUMMARY PANEL
        JPanel billsumPanel = new JPanel(new GridBagLayout());
        JLabel yearLabel = new JLabel("Year:");
        JTextField yearField = new JTextField(10);
        TextFieldUtil.applyNumericKeyListenerWithMaxLength(yearField , 4);
        JLabel monthLabel = new JLabel("Month (Optional):");
        JTextField monthField = new JTextField(10);
        TextFieldUtil.applyNumericKeyListener(monthField);
        JButton calculateButton = new JButton("Calculate");

        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        billsumPanel.add(yearLabel, gbc);

        gbc.gridx = 1;
        billsumPanel.add(yearField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        billsumPanel.add(monthLabel, gbc);

        gbc.gridx = 1;
        billsumPanel.add(monthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        billsumPanel.add(calculateButton, gbc);

        gbc.gridy = 3;
        billsumPanel.add(scrollPane, gbc);

        calculateButton.addActionListener(e -> {
            try {
                String yearText = yearField.getText().trim();
                String monthText = monthField.getText().trim();

                if (yearText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid year.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int year = Integer.parseInt(yearText);
                int month = -1;

                if (!monthText.isEmpty()) {
                    month = Integer.parseInt(monthText);

                    if (month < 1 || month > 12) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid month (1-12).", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                double[] results = BillSummary_1.calculateProfitByDate(year, month);
                double totalCost = results[0];
                double totalBillAmount = results[1];
                double profit = results[2];

                StringBuilder summary = new StringBuilder();
                if (month == -1) {
                    summary.append("Summary for Year ").append(year).append(":\n");
                } else {
                    summary.append("Summary for ").append(year).append("-").append(String.format("%02d", month)).append(":\n");
                }
                summary.append("Total Cost: ").append(totalCost).append("\n");
                summary.append("Total Revenue: ").append(totalBillAmount).append("\n");
                summary.append("Total Profit: ").append(profit).append("\n");

                resultArea.setText(summary.toString());

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values for year and month.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "An error occurred while calculating the summary.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });








        // adding less stock PANEL



        JPanel lessStockPanel = new JPanel(new BorderLayout());
        JTextArea lessStockDisplayArea = new JTextArea();
        lessStockDisplayArea.setEditable(false);
        JScrollPane lessStockScrollPane = new JScrollPane(lessStockDisplayArea);
        JButton lessStockButton = new JButton("Retrieve Details");
        lessStockButton.addActionListener(e -> {
            String retrievedData = GetLessStockItems.getLessStockItems(); // Fetch items from database
            lessStockDisplayArea.setText(retrievedData); // Display retrieved data in the text area
        });
        lessStockPanel.add(lessStockButton, BorderLayout.NORTH);
        lessStockPanel.add(lessStockScrollPane, BorderLayout.CENTER);






        lessStockPanel.setLayout(new BoxLayout(lessStockPanel, BoxLayout.Y_AXIS));
        lessStockPanel.setBorder(BorderFactory.createTitledBorder("Less Stock Options"));

        JButton generateCSVButton = new JButton("Generate CSV for Fill Capacity");
        generateCSVButton.addActionListener(e -> {
            try {
                GenerateCSVForFillCapacity.main(null);
                JOptionPane.showMessageDialog(frame, "CSV file generated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error generating CSV file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton updateItemsButton = new JButton("Update Items from CSV");
        updateItemsButton.addActionListener(e -> {
            try {
                UpdateItemsFromCSVSelfGenerated.main(null);
                JOptionPane.showMessageDialog(frame, "Items updated successfully from CSV.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error updating items: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        lessStockPanel.add(generateCSVButton);
        lessStockPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add some space between buttons
        lessStockPanel.add(updateItemsButton);
















        // Adding panels to tabbed pane
        tabbedPane.addTab("Add Salesman", addSalesmanPanel);
        tabbedPane.addTab("Add Delivery Boy", addDeliveryBoyPanel);
        tabbedPane.addTab("Retrieve Items", retrieveItemsPanel);
        tabbedPane.addTab("Insert Items from CSV", insertItemsPanel);
        tabbedPane.addTab("View Bill Details", viewBillPanel);
        tabbedPane.addTab("View Bill Summary", billsumPanel);
        tabbedPane.addTab("Less Stock Details", lessStockPanel);



        // Adding title and tabbedPane to the frame
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);

        // Final settings for the frame
        frame.setVisible(true);
    }
}