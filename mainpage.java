//package org.example.src.GUI;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import static java.awt.SystemColor.window;
//
//public class mainpage {
//
//
//    public static void main(String[] args) {
//        // Create the main frame
//        JFrame frame = new JFrame("Food Haven");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 300);
//
//        // Set layout
//        frame.setLayout(new BorderLayout());
//
//        // Add a title label
//        JLabel titleLabel = new JLabel("Food Haven", JLabel.CENTER);
//        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//        frame.add(titleLabel, BorderLayout.NORTH);
//
//        // Create a panel for buttons
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
//        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
//
//        // Create buttons
//        JButton managerButton = new JButton("Manager");
//        JButton customerButton = new JButton("Customer");
//        JButton salesmanButton = new JButton("Salesman");
//
//        // Add action listeners for buttons
//        managerButton.addActionListener(new ActionListener() {
//            @Override
//
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//                ManagerLogin.main(new String[]{});
//            }
//        });
//
//        customerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//                LoginCustomerGUI.main(new String[]{});
//            }
//        });
//
//        salesmanButton.addActionListener(new ActionListener() {
//
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                frame.dispose();
//                LoginSalesManGUI.main(new String[]{});
//            }
//        });
//
//        // Add buttons to panel
//        buttonPanel.add(managerButton);
//        buttonPanel.add(customerButton);
//        buttonPanel.add(salesmanButton);
//
//        // Add button panel to frame
//        frame.add(buttonPanel, BorderLayout.CENTER);
//
//        // Make the frame visible
//        frame.setVisible(true);
//    }
//}
//

package org.example.src.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainpage {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Food Haven");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        frame.setLayout(new BorderLayout());

        // Add a background panel with an image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/main/java/org/example/src/GUI/backGroundImg.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        // Add a title label
        JLabel titleLabel = new JLabel("Food Haven", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 20, 20));
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // Create styled buttons
        JButton managerButton = createStyledButton("Manager");
        JButton customerButton = createStyledButton("Customer");
        JButton salesmanButton = createStyledButton("Salesman");

        // Add action listeners for buttons
        managerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ManagerLogin.main(new String[]{});
            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginCustomerGUI.main(new String[]{});
            }
        });

        salesmanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                LoginSalesManGUI.main(new String[]{});
            }
        });

        // Add buttons to the panel
        buttonPanel.add(managerButton);
        buttonPanel.add(customerButton);
        buttonPanel.add(salesmanButton);

        // Add button panel to the background panel
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to create styled buttons
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 18));
        button.setBackground(new Color(15, 82, 46));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); //giving random buttons a ring around them
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}
