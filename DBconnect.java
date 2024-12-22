import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DBconnect {
    private Connection connection;

    public DBconnect() {
        connectToDatabase();
        createGUI();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/employeedb";
            String user = "root";
            String password = "0795";

            connection = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(null, "Connected to the database!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createGUI() {
        JFrame frame = new JFrame("Employee Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.getContentPane().setBackground(new Color(0xE4E4E4));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0xEDDEA4));
        JLabel titleLabel = new JLabel("Employee Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        titlePanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(new Color(0xE4E4E4));

        JButton addButton = createCustomButton("Add Employee");
        JButton updateButton = createCustomButton("Update Employee");
        JButton deleteButton = createCustomButton("Delete Employee");
        JButton viewButton = createCustomButton("View Employees");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);

        addButton.addActionListener(e -> showAddEmployeePanel());
        updateButton.addActionListener(e -> showUpdateEmployeePanel());
        deleteButton.addActionListener(e -> deleteEmployee());
        viewButton.addActionListener(e -> showEmployeesInTable());

        frame.setLayout(new BorderLayout());
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0xB5C266));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        return button;
    }

    private void showAddEmployeePanel() {
        JFrame addFrame = new JFrame("Add Employee");
        addFrame.setSize(400, 350);
        addFrame.setLayout(new GridLayout(6, 2, 10, 10));
        addFrame.getContentPane().setBackground(new Color(0xE4E4E4));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField salaryField = new JTextField();

        JButton submitButton = createCustomButton("Submit");
        JButton closeButton = createCustomButton("Close");

        addFrame.add(new JLabel("Employee ID:"));
        addFrame.add(idField);
        addFrame.add(new JLabel("Name:"));
        addFrame.add(nameField);
        addFrame.add(new JLabel("Age:"));
        addFrame.add(ageField);
        addFrame.add(new JLabel("Department:"));
        addFrame.add(deptField);
        addFrame.add(new JLabel("Salary:"));
        addFrame.add(salaryField);
        addFrame.add(submitButton);
        addFrame.add(closeButton);

        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String department = deptField.getText();
                double salary = Double.parseDouble(salaryField.getText());

                String query = "INSERT INTO employees (id, name, age, department, salary) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setInt(1, id);
                    pstmt.setString(2, name);
                    pstmt.setInt(3, age);
                    pstmt.setString(4, department);
                    pstmt.setDouble(5, salary);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(addFrame, "Employee added successfully!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(addFrame, "Please enter valid numerical values for ID, Age, and Salary.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(addFrame, "Failed to add employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> addFrame.dispose());
        addFrame.setVisible(true);
    }

    private void showUpdateEmployeePanel() {
        JFrame updateFrame = new JFrame("Update Employee");
        updateFrame.setSize(400, 350);
        updateFrame.setLayout(new GridLayout(6, 2, 10, 10));
        updateFrame.getContentPane().setBackground(new Color(0xE4E4E4));

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField salaryField = new JTextField();

        JButton submitButton = createCustomButton("Submit");
        JButton closeButton = createCustomButton("Close");

        updateFrame.add(new JLabel("Employee ID:"));
        updateFrame.add(idField);
        updateFrame.add(new JLabel("New Name:"));
        updateFrame.add(nameField);
        updateFrame.add(new JLabel("New Age:"));
        updateFrame.add(ageField);
        updateFrame.add(new JLabel("New Department:"));
        updateFrame.add(deptField);
        updateFrame.add(new JLabel("New Salary:"));
        updateFrame.add(salaryField);
        updateFrame.add(submitButton);
        updateFrame.add(closeButton);

        submitButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String department = deptField.getText();
                double salary = Double.parseDouble(salaryField.getText());

                String query = "UPDATE employees SET name = ?, age = ?, department = ?, salary = ? WHERE id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, name);
                    pstmt.setInt(2, age);
                    pstmt.setString(3, department);
                    pstmt.setDouble(4, salary);
                    pstmt.setInt(5, id);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(updateFrame, "Employee updated successfully!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updateFrame, "Please enter valid numerical values.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(updateFrame, "Failed to update employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> updateFrame.dispose());
        updateFrame.setVisible(true);
    }

    private void showEmployeesInTable() {
        JFrame viewFrame = new JFrame("View Employees");
        viewFrame.setSize(600, 400);
        viewFrame.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Name", "Age", "Department", "Salary"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JButton closeButton = createCustomButton("Close");

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getString("department"),
                    rs.getDouble("salary")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(viewFrame, "Failed to retrieve employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane, BorderLayout.CENTER);
        viewFrame.add(closeButton, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> viewFrame.dispose());
        viewFrame.setVisible(true);
    }

    private void deleteEmployee() {
        String idInput = JOptionPane.showInputDialog("Enter Employee ID to delete:");
        try {
            int id = Integer.parseInt(idInput);

            String query = "DELETE FROM employees WHERE id = ?";
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Employee deleted successfully!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid employee ID.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to delete employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DBconnect::new);
    }
}