package org.example.src.GUI;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesManOptions extends JFrame {
    public SalesManOptions(int id) {
        setTitle("SalesManOptions");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Use null layout for custom positioning

        JButton createBillButton = new JButton("Create Bill");
        createBillButton.setBounds(50, 50, 200, 30);
        createBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SalesManOptions.this, "Create Bill clicked!");
                dispose();
                new BillGeneratorSalesMan(id);

            }
        });

        JButton signUpButton = new JButton("Sign Up New Customer");
        signUpButton.setBounds(50, 100, 200, 30);
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SalesManOptions.this, "Sign Up New Customer clicked!");
                dispose();
                SignUpCustomerGUI.main(new String[]{String.valueOf(id)});

            }
        });

        JButton logout = new JButton("Sign Up New Customer");
        logout.setBounds(50, 100, 200, 30);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(SalesManOptions.this, "You Logged Out");
                dispose();
                mainpage.main(null);
            }
        });

        add(createBillButton);
        add(signUpButton);
    }

    public static void main(String[] args) {
            int id = Integer.parseInt(args[0]);
            SalesManOptions frame = new SalesManOptions(id);
            frame.setVisible(true);

    }
}
