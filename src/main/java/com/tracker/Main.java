package com.tracker;

import javax.swing.*;

import com.tracker.gui.ExpenseTrackerGUI;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ExpenseTrackerGUI().setVisible(true);
        });
    }
}
