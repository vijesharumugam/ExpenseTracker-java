package com.tracker.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import com.tracker.model.Category;
import com.tracker.dao.CategoryDAO;

public class CategoryGUI extends JFrame {
    private CategoryDAO categoryDAO;
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField typeField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public CategoryGUI() {
    categoryDAO = new CategoryDAO();
    initializeComponents();
    setupLayout();
    loadTypes();
    setupEventListeners();
}


    private void initializeComponents() {
        setTitle("Category Manager");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnName = {"ID", "Type"};
        tableModel = new DefaultTableModel(columnName, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoryTable = new JTable(tableModel);
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedType();
            }
        });

        typeField = new JTextField(20);

        addButton = new JButton("ADD");
        updateButton = new JButton("UPDATE");
        deleteButton = new JButton("DELETE");
        refreshButton = new JButton("REFRESH");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        inputPanel.add(new JLabel("Type : "), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(typeField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(categoryTable), BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Select a category to update or delete."));
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void loadSelectedType() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow >= 0) {
            String type = (String) tableModel.getValueAt(selectedRow, 1);
            typeField.setText(type);
        }
    }

    private void loadTypes() {
        try {
            List<Category> types = categoryDAO.getAllTypes();
            updateTable(types);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading types: " + e.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<Category> types) {
        tableModel.setRowCount(0);
        for (Category type : types) {
            Object[] rowData = {type.getId(), type.getType()};
            tableModel.addRow(rowData);
        }
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addType());
        updateButton.addActionListener(e -> updateType());
        deleteButton.addActionListener(e -> deleteType());
        refreshButton.addActionListener(e -> refreshType());
    }

    private void addType() {
        String type = typeField.getText().trim();
        if (type.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the type", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Category category = new Category(type);
            categoryDAO.insertType(category);
            loadTypes();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding type: " + e.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateType() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String newType = typeField.getText().trim();
        if (newType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the type", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Category category = new Category(id, newType);
            categoryDAO.updateType(category);
            loadTypes();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error updating type: " + e.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteType() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        try {
            categoryDAO.deleteType(id);
            loadTypes();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting type: " + e.getMessage(),
                    "Database error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshType() {
        loadTypes();
        clearForm();
    }

    private void clearForm() {
        typeField.setText("");
        categoryTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CategoryGUI categoryGUI = new CategoryGUI();
            categoryGUI.setVisible(true);
        });
    }
}
