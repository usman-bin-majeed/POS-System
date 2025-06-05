package org.example.src.GUI;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManagerLogin {

    // Database path (update this to your database location)
    private static final String DB_PATH = "jdbc:sqlite:src/main/java/org/example/src/DataBaseHandlers/database/testing.db";

    public static void main(String[] args) {
        // Initialize the frame
        JFrame frame = new JFrame("Manager Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());



        // Components
        JLabel titleLabel = new JLabel("Manager Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);

        // Layout using GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        frame.add(usernameLabel, gbc);

        gbc.gridx = 1;
        frame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(passwordLabel, gbc);

        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(loginButton, gbc);

        gbc.gridy = 4;
        frame.add(errorLabel, gbc);

        // Action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int managerId = 0;
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    errorLabel.setText("Please fill in all fields.");
                } else {
                    try (Connection conn = DriverManager.getConnection(DB_PATH);
                         PreparedStatement stmt = conn.prepareStatement("SELECT password FROM MANAGER WHERE username = ?")) {

                        stmt.setString(1, username);

                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            String storedPassword = rs.getString("password");
                            if (storedPassword.equals(password)) {
                                // Successful login
                                JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                errorLabel.setText("");

                                frame.dispose(); // Close the login window

                                try (Connection conn_2 = DriverManager.getConnection(DB_PATH);
                                     PreparedStatement stmtId = conn_2.prepareStatement("SELECT id FROM MANAGER WHERE username = ?")) {

                                    stmtId.setString(1, username);
                                    ResultSet rsForId = stmtId.executeQuery();

                                    if (rsForId.next()) {
                                        int id = rsForId.getInt("id");
                                        System.out.println("Manager ID: " + id);

                                        // Redirect to Manager Options or Dashboard
                                        ManagerGUI.main(new String[]{String.valueOf(id)});

                                    } else {
                                        errorLabel.setText("Manager not found.");
                                    }

                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                    errorLabel.setText("Database error. Please try again.");
                                }

                            } else {
                                errorLabel.setText("Incorrect password.");
                            }
                        } else {
                            errorLabel.setText("Manager not found.");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        errorLabel.setText("Database error. Please try again.");
                    }
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
