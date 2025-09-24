package com.tracker.gui;

import javax.swing.*;

public class CategoryGUI extends JFrame {

    public CategoryGUI() {
        setTitle("Category");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes only this window
        setLocationRelativeTo(null);

        JLabel label = new JLabel("This is the Category Window", SwingConstants.CENTER);
        add(label);
    }

   
    public static void main(String[] args) {
        CategoryGUI categoryGUI = new CategoryGUI();
        categoryGUI.setVisible(true);
    }
}
