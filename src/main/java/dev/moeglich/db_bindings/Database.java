package dev.moeglich.db_bindings;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static Connection connection;

    static Connection getInstance() throws SQLException {
        if (connection != null) {
            return connection;
        }

        String host = System.getenv("DB_HOST");
        String port = System.getenv("DB_PORT");
        String dbName = System.getenv("DB_NAME");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);

        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static Note findById(int id) throws SQLException {
        Connection connection = getInstance();
        String query = "SELECT id, content, created_at FROM entries WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Note(rs.getInt("id"), rs.getBytes("content"), rs.getTimestamp("created_at"));
        } else {
            throw new SQLException("No entry found with id " + id);
        }
    }

    public static Note create(byte[] content) throws SQLException {
        Connection connection = getInstance();
        String query = "INSERT INTO entries (content) VALUES (?) RETURNING id, content, created_at";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setBytes(1, content);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Note(rs.getInt("id"), rs.getBytes("content"), rs.getTimestamp("created_at"));
        } else {
            throw new SQLException("Failed to insert entry");
        }
    }

    public static Note update(int id, byte[] content) throws SQLException {
        Connection connection = getInstance();
        String query = "UPDATE entries SET content = ? WHERE id = ? RETURNING created_at";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setBytes(1, content);
        pstmt.setInt(2, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Note(id, content, rs.getTimestamp("created_at"));
        } else {
            throw new SQLException("No entry found with id " + id);
        }
    }

    public static ArrayList<Note> getAll() throws SQLException {
        Connection connection = getInstance();
        String query = "SELECT id, content, created_at FROM entries ORDER BY created_at DESC";
        ArrayList<Note> entries = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            entries.add(new Note(rs.getInt("id"), rs.getBytes("content"), rs.getTimestamp("created_at")));
        }
        return entries;
    }

    public static void deleteById(int id) throws SQLException {
        Connection connection = getInstance();
        String query = "DELETE FROM entries WHERE id = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setInt(1, id);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("No entry found with id " + id);
        }
    }
}
