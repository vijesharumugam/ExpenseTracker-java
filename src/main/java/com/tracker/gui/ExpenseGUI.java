package com.tracker.gui;

import com.tracker.dao.CategoryDAO;
import com.tracker.dao.ExpenseDAO;
import com.tracker.model.Category;
import com.tracker.model.Expense;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseGUI extends JFrame {
    private JComboBox<Category> categoryDropdown;
    private JTextField descriptionField;
    private JTextField amountField;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JTable expenseTable;
    private DefaultTableModel tableModel;

    private final CategoryDAO categoryDAO;
    private final ExpenseDAO expenseDAO;

    public ExpenseGUI() {
        setTitle("Expense Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        categoryDAO = new CategoryDAO();
        expenseDAO = new ExpenseDAO();

        initializeComponents();
        setupLayout();
        setupActions();
        loadCategories();
        loadExpenses();
    }

    private void initializeComponents() {
        categoryDropdown = new JComboBox<>();
        descriptionField = new JTextField(15);
        amountField = new JTextField(10);

        addButton = new JButton("ADD");
        updateButton = new JButton("UPDATE");
        deleteButton = new JButton("DELETE");
        refreshButton = new JButton("REFRESH");

        String[] columnNames = {"ID", "Category", "Description", "Amount", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        expenseTable = new JTable(tableModel);
        expenseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        expenseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedExpense();
            }
        });
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(categoryDropdown, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(amountField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
    }

    private void setupActions() {
        addButton.addActionListener(e -> addExpense());
        updateButton.addActionListener(e -> updateExpense());
        deleteButton.addActionListener(e -> deleteExpense());
        refreshButton.addActionListener(e -> loadExpenses());
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllTypes();
            categoryDropdown.removeAllItems();
            for (Category c : categories) {
                categoryDropdown.addItem(c);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadExpenses() {
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            updateTable(expenses);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading expenses: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Expense> expenses) {
        tableModel.setRowCount(0);
        for (Expense exp : expenses) {
            Category cat = null;
            try {
                // ✅ FIXED: use instance, not class
                cat = categoryDAO.getCategoryById(exp.getCategoryId());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String categoryName = (cat != null) ? cat.getType() : "Unknown";
            Object[] row = {
                    exp.getId(),
                    categoryName,
                    exp.getDescription(),
                    exp.getAmount(),
                    exp.getDate()
            };
            tableModel.addRow(row);
        }
    }

    private void loadSelectedExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow >= 0) {
            String categoryName = (String) tableModel.getValueAt(selectedRow, 1);
            String description = (String) tableModel.getValueAt(selectedRow, 2);
            int amount = (int) tableModel.getValueAt(selectedRow, 3);

            descriptionField.setText(description);
            amountField.setText(String.valueOf(amount));

            for (int i = 0; i < categoryDropdown.getItemCount(); i++) {
                if (categoryDropdown.getItemAt(i).getType().equals(categoryName)) {
                    categoryDropdown.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void addExpense() {
        try {
            Category category = (Category) categoryDropdown.getSelectedItem();
            String description = descriptionField.getText().trim();
            int amount = Integer.parseInt(amountField.getText().trim());

            if (category == null || description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Expense expense = new Expense(category.getId(), description, amount);
            expense.setDate(LocalDateTime.now());
            expenseDAO.insertExpense(expense);

            loadExpenses();
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding expense: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select an expense to update!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Category category = (Category) categoryDropdown.getSelectedItem();
            String description = descriptionField.getText().trim();
            int amount = Integer.parseInt(amountField.getText().trim());

            Expense expense = new Expense(id, category.getId(), description, amount, LocalDateTime.now());
            expenseDAO.updateExpense(expense);
            loadExpenses();
            clearForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating expense: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        int selectedRow = expenseTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Select an expense to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            expenseDAO.deleteExpense(id);
            loadExpenses();
            clearForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting expense: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        descriptionField.setText("");
        amountField.setText("");
        if (categoryDropdown.getItemCount() > 0)
            categoryDropdown.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseGUI().setVisible(true));
    }
}
