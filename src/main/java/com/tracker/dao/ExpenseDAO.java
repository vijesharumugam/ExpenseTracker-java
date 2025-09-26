package com.tracker.dao;

import com.tracker.model.Expense;
import com.tracker.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    private static final String INSERT_EXPENSE =
            "INSERT INTO expense (category_id, description, amount, date) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL_EXPENSES =
            "SELECT * FROM expense ORDER BY date DESC";
    private static final String UPDATE_EXPENSE =
            "UPDATE expense SET category_id = ?, description = ?, amount = ?, date = ? WHERE id = ?";
    private static final String DELETE_EXPENSE =
            "DELETE FROM expense WHERE id = ?";

    public void insertExpense(Expense exp) throws SQLException {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_EXPENSE)) {
            ps.setInt(1, exp.getCategoryId());
            ps.setString(2, exp.getDescription());
            ps.setInt(3, exp.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(exp.getDate()));
            ps.executeUpdate();
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        List<Expense> list = new ArrayList<>();
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(GET_ALL_EXPENSES);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Expense(
                        rs.getInt("id"),
                        rs.getInt("category_id"),
                        rs.getString("description"),
                        rs.getInt("amount"),
                        rs.getTimestamp("date").toLocalDateTime()
                ));
            }
        }
        return list;
    }

    public void updateExpense(Expense exp) throws SQLException {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE_EXPENSE)) {
            ps.setInt(1, exp.getCategoryId());
            ps.setString(2, exp.getDescription());
            ps.setInt(3, exp.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(exp.getDate()));
            ps.setInt(5, exp.getId());
            ps.executeUpdate();
        }
    }

    public void deleteExpense(int id) throws SQLException {
        try (Connection cn = DatabaseConnection.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_EXPENSE)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
