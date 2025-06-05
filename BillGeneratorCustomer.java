package org.example.src.GUI;

import org.example.src.MainClasses.Bill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BillGeneratorCustomer {
    private static final String dbFilePath = "src/main/java/org/example/src/DataBaseHandlers/database/testing.db";
    private static final String DB_URL = "jdbc:sqlite:" + dbFilePath;

    private JFrame frame;
    private JTextField searchField, quantityField, itemIdField;
    private JComboBox<String> itemComboBox;
    private JTable cartTable;
    private DefaultTableModel tableModel;
    private JButton addButton, nextButton, logoutButton;
    private ArrayList<Bill.ItemQuantity> cart;
    private int loggedId;

    public BillGeneratorCustomer(int loggedId) {
        this.loggedId = loggedId;
        cart = new ArrayList<>();

        frame = new JFrame("Bill Generator (Logged in as " + loggedId + ")");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel searchLabel = new JLabel("Search Item:");
        searchField = new JTextField();
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterItems();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterItems();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterItems();
            }
        });

        JLabel itemLabel = new JLabel("Select Item:");
        itemComboBox = new JComboBox<>();
        loadItemsIntoComboBox();
        itemComboBox.addActionListener(e -> updateItemIdField());

        JLabel itemIdLabel = new JLabel("Item ID:");
        itemIdField = new JTextField();
        itemIdField.setEditable(false);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();

        addButton = new JButton("Add to Cart");
        addButton.addActionListener(new AddToCartActionListener());

        topPanel.add(itemIdLabel);
        topPanel.add(searchLabel);
        topPanel.add(itemLabel);
        topPanel.add(quantityLabel);
        topPanel.add(itemIdField);
        topPanel.add(searchField);
        topPanel.add(itemComboBox);
        topPanel.add(quantityField);
        topPanel.add(new JLabel());
        topPanel.add(addButton);

        frame.add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Item Name", "Item ID", "Unit Price", "Quantity", "Total Price"}, 0);
        cartTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cart Items"));
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        nextButton = new JButton("Next");
        nextButton.addActionListener(new NextButtonActionListener());
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> frame.dispose());
        bottomPanel.add(nextButton);
        bottomPanel.add(logoutButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadItemsIntoComboBox() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Class.forName("org.sqlite.JDBC");
                String query = "SELECT name FROM ITEM";
                try (PreparedStatement stmt = conn.prepareStatement(query);
                     ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        itemComboBox.addItem(rs.getString("name"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterItems() {
        String searchText = searchField.getText().toLowerCase();
        itemComboBox.removeAllItems();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String query = "SELECT name FROM ITEM WHERE name LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, searchText + "%");
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            itemComboBox.addItem(rs.getString("name"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateItemIdField();
    }

    private void updateItemIdField() {
        String selectedItem = (String) itemComboBox.getSelectedItem();
        if (selectedItem != null) {
            int itemId = getItemIdByName(selectedItem);
            itemIdField.setText(itemId == -1 ? "" : String.valueOf(itemId));
        } else {
            itemIdField.setText("");
        }
    }

    private int getItemIdByName(String itemName) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String query = "SELECT id FROM ITEM WHERE name = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, itemName);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("id");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getAvailableQuantity(int itemId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String query = "SELECT quantity FROM ITEM WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, itemId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getInt("quantity");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double getUnitPriceById(int itemId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String query = "SELECT price FROM ITEM WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, itemId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            return rs.getDouble("price");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private class AddToCartActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedItem = (String) itemComboBox.getSelectedItem();
            int itemId = getItemIdByName(selectedItem);
            int enteredQuantity;

            try {
                enteredQuantity = Integer.parseInt(quantityField.getText());
                if (enteredQuantity <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int availableQuantity = getAvailableQuantity(itemId);
            if (enteredQuantity > availableQuantity) {
                JOptionPane.showMessageDialog(frame,
                        "Only " + availableQuantity + " units of " + selectedItem + " are available. Please enter a valid quantity.",
                        "Insufficient Stock",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            double unitPrice = getUnitPriceById(itemId);
            double totalPrice = unitPrice * enteredQuantity;

            cart.add(new Bill.ItemQuantity(itemId, enteredQuantity));
            tableModel.addRow(new Object[]{selectedItem, itemId, unitPrice, enteredQuantity, totalPrice});
            quantityField.setText("");
        }
    }

    private class NextButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Cart is empty. Please add items.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            frame.dispose();
            new DeliveryGui(cart , loggedId);
        }
    }

    public static void main(String[] args) {
        new BillGeneratorCustomer(12);
    }
}
