package org.example.src.GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SignUpCustomerGUI {

    // Database path

    public static void main(String[] args) {
        int id = Integer.parseInt(args[0]);
        String DB_PATH = "jdbc:sqlite:src/main/java/org/example/src/DataBaseHandlers/database/testing.db";

        // Initialize the frame
        JFrame frame = new JFrame("Customer Sign-Up");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Components for Sign-Up
        JLabel signupTitleLabel = new JLabel("Sign-Up Section");
        signupTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));


        JLabel firstnameLabel = new JLabel("First Name:");
        JTextField firstnameField = new JTextField(15);
        TextFieldUtil.applyAlphabeticKeyListener(firstnameField);

        JLabel lastnameLabel = new JLabel("Last Name:");
        JTextField lastnameField = new JTextField(15);
        TextFieldUtil.applyAlphabeticKeyListener(lastnameField);


        JLabel cnicLabel = new JLabel("CNIC:");
        JTextField cnicField = new JTextField(15);
        TextFieldUtil.applyNumericKeyListenerWithMaxLength(cnicField , 11);


        JLabel usernameSignupLabel = new JLabel("Username:");
        JTextField usernameSignupField = new JTextField(15);

        JLabel passwordSignupLabel = new JLabel("Password:");
        JPasswordField passwordSignupField = new JPasswordField(15);

        JButton signupButton = new JButton("Sign Up");

        JLabel signupErrorLabel = new JLabel();
        signupErrorLabel.setForeground(Color.RED);

        // Layout using GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(signupTitleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(firstnameLabel, gbc);

        gbc.gridx = 1;
        frame.add(firstnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(lastnameLabel, gbc);

        gbc.gridx = 1;
        frame.add(lastnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(cnicLabel, gbc);

        gbc.gridx = 1;
        frame.add(cnicField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(usernameSignupLabel, gbc);

        gbc.gridx = 1;
        frame.add(usernameSignupField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(passwordSignupLabel, gbc);

        gbc.gridx = 1;
        frame.add(passwordSignupField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        frame.add(signupButton, gbc);

        gbc.gridy = 7;
        frame.add(signupErrorLabel, gbc);

        // Action listener for Sign-Up button
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstname = firstnameField.getText().trim();
                String lastname = lastnameField.getText().trim();
                String cnic = cnicField.getText().trim();
                String username = usernameSignupField.getText().trim();
                String password = new String(passwordSignupField.getPassword()).trim();

                if (firstname.isEmpty() || lastname.isEmpty() || cnic.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    signupErrorLabel.setText("Please fill in all fields.");
                } else {
                    try (Connection conn = DriverManager.getConnection(DB_PATH);
                         PreparedStatement checkStmt = conn.prepareStatement("SELECT username, CNIC FROM CUSTOMER WHERE username = ? OR CNIC = ?");
                         PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO CUSTOMER (firstname, lastname, CNIC, username, password, onlineStatus) VALUES (?, ?, ?, ?, ?, 0)")) {

                        // Check if the user or CNIC already exists
                        checkStmt.setString(1, username);
                        checkStmt.setString(2, cnic);
                        ResultSet rs = checkStmt.executeQuery();

                        if (rs.next()) {
                            if (rs.getString("username").equals(username)) {
                                signupErrorLabel.setText("Username already exists.");
                            } else if (rs.getString("CNIC").equals(cnic)) {
                                signupErrorLabel.setText("CNIC already exists.");
                            }
                        } else {
                            // Insert the new user
                            insertStmt.setString(1, firstname);
                            insertStmt.setString(2, lastname);
                            insertStmt.setString(3, cnic);
                            insertStmt.setString(4, username);
                            insertStmt.setString(5, password);
                            insertStmt.executeUpdate();

                            JOptionPane.showMessageDialog(frame, "Sign-up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            signupErrorLabel.setText("");
                            frame.dispose();
                            SalesManOptions.main(new String[]{String.valueOf(id)});


                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        signupErrorLabel.setText("Database error. Please try again.");
                    }
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
