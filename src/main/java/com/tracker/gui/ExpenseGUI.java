package com.tracker.gui;

import javax.swing.*;

public class ExpenseGUI extends JFrame {

    public ExpenseGUI() {
        setTitle("Expense");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);


        JLabel label = new JLabel("This is the Expense Window", SwingConstants.CENTER);
        add(label);
    }

    
    public static void main(String[] args) {
        ExpenseGUI expenseGUI = new ExpenseGUI();
        expenseGUI.setVisible(true);
    }
}
