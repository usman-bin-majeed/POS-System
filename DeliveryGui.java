package org.example.src.GUI;

import org.example.src.ApiClasses.CordinatesConverter;
import org.example.src.ApiClasses.OSMGuiApp;
import org.example.src.DataBaseHandlers.DeliveryBoyAvailabilityChecker;
import org.example.src.GUI.BillGeneratorCustomer;
import org.example.src.MainClasses.Bill;
import org.example.src.MainClasses.SalesMan;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static org.example.src.DataBaseHandlers.GetBillDetails.fetchBillDetails;

public class DeliveryGui {
    private JFrame frame;
    private JTable deliveryDetailsTable;
    private DefaultTableModel tableModel;
    private JButton    viewRouteButton, submitButton, generateBillButton;
    private JTextField addressField, destinationXCoordField, destinationYCoordField;
    private JTextArea routeDetailsArea;
    private JLabel statusLabel, xLabel, yLabel;
    private ArrayList<Bill.ItemQuantity> cart;
    private int loggedId;

    // Fixed source coordinates (COMSATS University Islamabad Gate 1)
    private final double SOURCE_LAT = 33.65242333844067;
    private final double SOURCE_LON = 73.15700100595033;

    // A flag to track if the coordinates were successfully fetched
    private boolean coordinatesFound = false;

    public DeliveryGui(ArrayList<Bill.ItemQuantity> cart, int loggedId) {

        this.cart = cart;
        this.loggedId = loggedId;

        frame = new JFrame("Delivery Details (Logged in as " + loggedId + ")");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Delivery Details and Routes", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Cart Table
        tableModel = new DefaultTableModel(new String[]{"Item ID", "Quantity"}, 0);
        deliveryDetailsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(deliveryDetailsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Cart Items for Delivery"));
        frame.add(scrollPane, BorderLayout.CENTER);

        for (Bill.ItemQuantity item : cart) {
            tableModel.addRow(new Object[]{item.getItemId(), item.getQuantity()});
        }

        // Address and Coordinates Input Panel
        JPanel addressPanel = new JPanel(new GridLayout(5, 2, 10, 10));  // Increased rows to 4
        addressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel addressLabel = new JLabel("Delivery Address:");
        addressField = new JTextField();
        JLabel coordinatesLabel = new JLabel("Coordinates:");
        destinationXCoordField = new JTextField();
        destinationYCoordField = new JTextField();

        xLabel = new JLabel("X Coordinate (Latitude):");
        yLabel = new JLabel("Y Coordinate (Longitude):");

        JLabel routeLabel = new JLabel("Route Details:");
        routeDetailsArea = new JTextArea(3, 20);
        routeDetailsArea.setLineWrap(true);
        routeDetailsArea.setWrapStyleWord(true);
        JScrollPane routeScrollPane = new JScrollPane(routeDetailsArea);

        // Adding the new labels and fields
        addressPanel.add(addressLabel);
        addressPanel.add(addressField);
        addressPanel.add(xLabel);  // X coordinate label
        addressPanel.add(destinationXCoordField);
        addressPanel.add(yLabel);  // Y coordinate label
        addressPanel.add(destinationYCoordField);
        addressPanel.add(routeLabel);
        addressPanel.add(routeScrollPane);

        frame.add(addressPanel, BorderLayout.NORTH);

        // Status Label for fetching route status
        statusLabel = new JLabel("Status: ");
        JPanel statusPanel = new JPanel();
        addressPanel.add(statusLabel);
//        frame.add(statusPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        viewRouteButton = new JButton("View Route");
        submitButton = new JButton("Get Route");
        generateBillButton = new JButton("Generate Bill");

        submitButton.setEnabled(false);
        generateBillButton.setEnabled(false);  // Initially disabled

        viewRouteButton.addActionListener(e -> viewRoute());
        submitButton.addActionListener((ActionEvent e) -> fetchCoordinatesAndRoute());
        generateBillButton.addActionListener((ActionEvent e) -> generateBill());

        bottomPanel.add(viewRouteButton);
        bottomPanel.add(submitButton);
        bottomPanel.add(generateBillButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        addressField.addCaretListener(e -> validateInputs());
        destinationXCoordField.addCaretListener(e -> validateInputs());
        destinationYCoordField.addCaretListener(e -> validateInputs());
    }

    private void validateInputs() {
        String address = addressField.getText().trim();
        String latitude = destinationXCoordField.getText().trim();
        String longitude = destinationYCoordField.getText().trim();

        boolean isCoordinatesValid = isValidDouble(latitude) && isValidDouble(longitude);
        submitButton.setEnabled(!address.isEmpty() || isCoordinatesValid);
    }

    private void fetchCoordinatesAndRoute() {
        String address = addressField.getText().trim();

        String latitudeText = destinationXCoordField.getText().trim();
        String longitudeText = destinationYCoordField.getText().trim();
        statusLabel.setText("Fetching route...");
        routeDetailsArea.setText("");

        new Thread(() -> {
            double[] destinationCoordinates;

            if (!latitudeText.isEmpty() && !longitudeText.isEmpty() && isValidDouble(latitudeText) && isValidDouble(longitudeText)) {
                // Use manually entered coordinates
                destinationCoordinates = new double[]{Double.parseDouble(latitudeText), Double.parseDouble(longitudeText)};
            } else {
                // Fetch coordinates using the address
                destinationCoordinates = CordinatesConverter.getCoordinates(address);
            }

            SwingUtilities.invokeLater(() -> {
                if (destinationCoordinates != null) {
                    destinationXCoordField.setText(String.valueOf(destinationCoordinates[0]));
                    destinationYCoordField.setText(String.valueOf(destinationCoordinates[1]));
                    OSMGuiApp.getRouteDetails(SOURCE_LAT, SOURCE_LON, destinationCoordinates[0], destinationCoordinates[1]);
                    routeDetailsArea.setText(OSMGuiApp.getRouteDetailsString());
                    statusLabel.setText("Route found!");
                    coordinatesFound = true;  // Coordinates successfully found
                    if (OSMGuiApp.getRouteDetailsString().contains("Route Instructions"))
                        generateBillButton.setEnabled(true);  // Enable the "Generate Bill" button
                    else
                        generateBillButton.setEnabled(false);
                } else {
                    statusLabel.setText("Unable to fetch route enter Cordinates by yourself");
                    destinationXCoordField.setText("");
                    destinationYCoordField.setText("");
                    coordinatesFound = false;  // Coordinates not found
                    generateBillButton.setEnabled(false);  // Keep the button disabled
                }
            });
        }).start();
    }



    private void viewRoute() {
        String address = addressField.getText().trim();
        String coordinates = destinationXCoordField.getText().trim();
        String routeDetails = routeDetailsArea.getText().trim();

        if (address.isEmpty() || coordinates.isEmpty() || routeDetails.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No route details available to view.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Current Route Details:\n" +
                    "Address: " + address + "\nCoordinates: " + coordinates + "\nRoute: " + routeDetails, "Route Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void generateBill() {
        frame.dispose();
        if (coordinatesFound) {
            SalesMan salesMan = new SalesMan( cart ,1);
            salesMan.createBill(loggedId);


            try {
                // Delay for 1 second (1000 milliseconds)
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
            }
            String billDetails = fetchBillDetails(Bill.getHighestBillId());
            String routeDetails = routeDetailsArea.getText(); // Get the route details already displayed.

            // Create a new JFrame to display the bill and route details.
            JFrame billFrame = new JFrame("Bill and Route Details");
            billFrame.setSize(600, 400);
            billFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            billFrame.setLayout(new BorderLayout());

            // Create a text area to display the details.
            JTextArea detailsArea = new JTextArea();
            detailsArea.setEditable(false);

            int durationStartIndex = routeDetails.indexOf("Duration:") + "Duration:".length();


            int durationEndIndex = routeDetails.indexOf("minutes");


            String durationStr = routeDetails.substring(durationStartIndex, durationEndIndex).trim();


            double duration = Double.parseDouble(durationStr);
            int durationInt = (int) duration;

            System.out.println("Duration: " + durationInt + " minutes");

            detailsArea.setText("Bill Details:\n" + billDetails + "\n\nRoute Details:\n" + routeDetails  + "\n\nDe" +
                    "liveryBoy Details:\n" + DeliveryBoyAvailabilityChecker.assignDeliveryBoy(durationInt));


            JScrollPane scrollPane = new JScrollPane(detailsArea);
            billFrame.add(scrollPane, BorderLayout.CENTER);

            // Display the new frame.
            billFrame.setVisible(true);

            // Optionally show a confirmation message.
            JOptionPane.showMessageDialog(frame, "Bill and route details displayed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            billFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    System.exit(0);  // Exit the program
                }
            });
        } else {
            // Show an error message if coordinates are not found.
            JOptionPane.showMessageDialog(frame, "Coordinates must be successfully found before generating the bill.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }





    public static void main(String[] args) {
        // For testing, create a dummy cart
        ArrayList<Bill.ItemQuantity> dummyCart = new ArrayList<>();
        dummyCart.add(new Bill.ItemQuantity(1, 3));
        dummyCart.add(new Bill.ItemQuantity(2, 5));
        new DeliveryGui(dummyCart, 12);
    }
}
