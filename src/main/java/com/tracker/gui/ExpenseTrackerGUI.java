package com.tracker.gui;
import javax.swing.*;


import java.awt.*;
import com.tracker.gui.CategoryGUI;
import com.tracker.gui.ExpenseGUI;

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

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(categoryButton);
        buttonPanel.add(expenseButton);

        add(buttonPanel, BorderLayout.CENTER);

        setTitle("Expense Tracker");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void openCategory() {
    CategoryGUI categoryGUI = new CategoryGUI();
    categoryGUI.setVisible(true);
}

    private void openExpense(){
        ExpenseGUI expenseGUI = new ExpenseGUI();
        expenseGUI.setVisible(true);
    }

    private void setupEventListers(){
        categoryButton.addActionListener((e) -> {
            openCategory();
        });
        expenseButton.addActionListener((e) -> {
            openExpense();
        });
    }

}
