package com.tracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.tracker.model.Category;
import com.tracker.util.DatabaseConnection;

public class CategoryDAO {
    private static final String GET_ALL_CATEGORY = "SELECT * FROM category";
    private static final String INSERT_CATEGORY = "INSERT INTO category(type) VALUES (?)";
    private static final String UPDATE_CATEGORY = "UPDATE category SET type = ? WHERE id = ?";
    private static final String DELETE_CATEGORY = "DELETE FROM category WHERE id = ?";

    public List<Category> getAllTypes() throws SQLException {
        List<Category> types = new ArrayList<>();
        try (
            Connection cn = DatabaseConnection.getConnection();
            PreparedStatement stat = cn.prepareStatement(GET_ALL_CATEGORY);
            ResultSet rs = stat.executeQuery()
        ) {
            while (rs.next()) {
                types.add(getType(rs));
            }
        }
        return types;
    }

    public void insertType(Category category) throws SQLException {
        try (
            Connection cn = DatabaseConnection.getConnection();
            PreparedStatement stat = cn.prepareStatement(INSERT_CATEGORY)
        ) {
            stat.setString(1, category.getType());
            stat.executeUpdate();
        }
    }

    public void updateType(Category category) throws SQLException {
        try (
            Connection cn = DatabaseConnection.getConnection();
            PreparedStatement stat = cn.prepareStatement(UPDATE_CATEGORY)
        ) {
            stat.setString(1, category.getType());
            stat.setInt(2, category.getId());
            stat.executeUpdate();
        }
    }

    public void deleteType(int id) throws SQLException {
        try (
            Connection cn = DatabaseConnection.getConnection();
            PreparedStatement stat = cn.prepareStatement(DELETE_CATEGORY)
        ) {
            stat.setInt(1, id);
            stat.executeUpdate();
        }
    }

    private Category getType(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String type = rs.getString("type");
        return new Category(id, type);
    }
    public Category getCategoryById(int id) throws SQLException {
    String query = "SELECT * FROM category WHERE id = ?";
    try (Connection cn = DatabaseConnection.getConnection();
         PreparedStatement ps = cn.prepareStatement(query)) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Category(rs.getInt("id"), rs.getString("type"));
        }
    }
    return null;
}

}
