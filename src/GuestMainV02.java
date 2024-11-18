/**************************************************
 Created by Wachira Nyaga on 06/11/2024 08:43:37.
 ***************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GuestMainV02 extends JFrame {
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField contactField;
    private final JTextField emailField;
    private final JComboBox<String> roomTypeComboBox;
    private JTextField dateField;
    private final JSpinner daysField;
    private final JTextArea requirementsArea;
    private final JTextField totalAmountField;

    public GuestMainV02() {
        setTitle("Eugene's Restaurant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Automatically maximize the window
        setLayout(new BorderLayout());
        Color backgroundColor = new Color(230, 255, 230); // Light green shade
        getContentPane().setBackground(backgroundColor);

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(204, 255, 204));
        JLabel titleLabel = new JLabel("EUGENE'S");
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 102, 51));
        //JTextField searchField = new JTextField(20);
        //JButton searchButton = new JButton("Search");
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(200));
        //headerPanel.add(searchField);
        //headerPanel.add(searchButton);

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(backgroundColor);

        JLabel styledTextLabel = new JLabel("<html><center>Eugene's Restaurant<br>" +
                "<p><font face=\"Gabriola\" size=\"5\">Immerse yourself in luxurious comfort and breathtaking views.</font></p>" +
                "Reserve Now</center></html>", JLabel.CENTER);
        styledTextLabel.setFont(new Font("Georgia", Font.BOLD, 40));
        styledTextLabel.setForeground(new Color(0, 102, 51));
        styledTextLabel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));
        leftPanel.add(styledTextLabel, BorderLayout.CENTER);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Reduced insets for closer spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

// Adjusted column size of text fields for increased length
        firstNameField = createPlaceholderField("Enter first name", 30);
        addLabelAndField(formPanel, "First Name:", firstNameField, 0, 0, gbc);

        lastNameField = createPlaceholderField("Enter last name", 30);
        addLabelAndField(formPanel, "Last Name:", lastNameField, 1, 0, gbc);

        contactField = createPlaceholderField("+254 (Country Code) followed by number", 30);
        addLabelAndField(formPanel, "Contact:", contactField, 0, 1, gbc);

        emailField = createPlaceholderField("Enter email", 30);
        addLabelAndField(formPanel, "Email:", emailField, 1, 1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Room Type:"), gbc);
        roomTypeComboBox = new JComboBox<>(new String[]{"Single", "Double", "Suite", "Family", "Presidential Suite"});
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(roomTypeComboBox, gbc);

        //dateField = createPlaceholderField("Enter date (DD/MM/YYYY)", 30);
        //addLabelAndField(formPanel, "Date:", dateField, 0, 3, gbc);

        daysField = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1));
        addLabelAndField(formPanel, "Days:", daysField, 1, 3, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Request and Requirements:"), gbc);

        requirementsArea = new JTextArea(3, 20);
        requirementsArea.setLineWrap(true);
        requirementsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(requirementsArea);
        gbc.gridy = 5;
        formPanel.add(scrollPane, gbc);

        // Total Amount Field
        JLabel totalAmountLabel = new JLabel("Total Amount($):");
        totalAmountLabel.setFont(new Font("Georgia", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(totalAmountLabel, gbc);

        totalAmountField = new JTextField(20);
        totalAmountField.setEditable(false);
        totalAmountField.setFont(new Font("Georgia", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(totalAmountField, gbc);

        // Create the reserve button
        JButton reserveButton = new JButton("Reserve");
        reserveButton.setBackground(new Color(0, 153, 76));
        reserveButton.setForeground(Color.WHITE);
        reserveButton.setFont(new Font("Georgia", Font.BOLD, 18));

        // Create the cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(new Color(204, 0, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Georgia", Font.BOLD, 18));

        // Add action listener to cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Closes the project
            }
        });

        // Use GridBagConstraints to place buttons next to each other
        //GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 7; // Same row as reserveButton
        gbc.gridx = 0; // Column for reserveButton
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        formPanel.add(reserveButton, gbc);

        gbc.gridx = 2; // Column for cancelButton
        formPanel.add(cancelButton, gbc);

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserveRoom();
                clearForm();
            }
        });


        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.setBackground(backgroundColor);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Add event listener for room type and days selection
        roomTypeComboBox.addActionListener(e -> updateTotalAmount());
        daysField.addChangeListener(e -> updateTotalAmount());
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        contactField.setText("");
        emailField.setText("");
        roomTypeComboBox.setSelectedIndex(0);
        requirementsArea.setText("");
        // Clear other fields as needed
    }
    private JTextField createPlaceholderField(String placeholder, int columns) {
        JTextField field = new JTextField(placeholder, columns);
        field.setFont(new Font("Georgia", Font.PLAIN, 16));
        field.setForeground(Color.GRAY);
        field.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        return field;
    }

    private void addLabelAndField(JPanel panel, String labelText, JComponent field, int row, int col, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Georgia", Font.BOLD, 16));
        gbc.gridx = col * 2;
        gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = col * 2 + 1;
        panel.add(field, gbc);
    }

    private void reserveRoom() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String roomType = roomTypeComboBox.getSelectedItem().toString();
        int days = (int) daysField.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        final String DB_URL = "jdbc:mysql://localhost:3307/hotel_reservation";
        final String USERNAME = "root";
        final String PASSWORD = "mysql";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO Guest (firstName, lastName, email, phone,room_type, numberOfDays, amount) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, contact);
            statement.setString(5, roomType);
            statement.setInt(6, days);
            statement.setDouble(7,totalAmount);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Reservation successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


        private double totalAmount;

        private void updateTotalAmount() {
            String selectedRoomType = roomTypeComboBox.getSelectedItem().toString();
            int numberOfDays = (int) daysField.getValue();

            final String DB_URL = "jdbc:mysql://localhost:3307/hotel_reservation";
            final String USERNAME = "root";
            final String PASSWORD = "mysql";

            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String sql = "SELECT cost_per_day FROM RoomTypes WHERE room_type = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, selectedRoomType);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    double costPerDay = rs.getDouble("cost_per_day");
                    totalAmount = costPerDay * numberOfDays;
                    totalAmountField.setText(String.format("%.2f", totalAmount));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching room price.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public double getTotalAmount() {
            return totalAmount;
        }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuestMainV02 reservationInterface = new GuestMainV02();
            reservationInterface.setVisible(true);
        });
    }
}

