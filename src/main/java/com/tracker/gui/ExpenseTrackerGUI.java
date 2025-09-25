package com.tracker.gui;

import javax.swing.*;
import java.awt.*;

public class ExpenseTrackerGUI extends JFrame {
    private JButton categoryButton;
    private JButton expenseButton;

    public ExpenseTrackerGUI() {
        initializeComponents();
        setupEventListers();
    }

    private void initializeComponents() {
        categoryButton = new JButton("CATEGORY");
        expenseButton = new JButton("EXPENSE");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(categoryButton);
        buttonPanel.add(expenseButton);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(buttonPanel, gbc);

        setTitle("Expense Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void setupEventListers() {
        categoryButton.addActionListener(e -> new CategoryGUI().setVisible(true));
        expenseButton.addActionListener(e -> new ExpenseGUI().setVisible(true));
    }

   
}
