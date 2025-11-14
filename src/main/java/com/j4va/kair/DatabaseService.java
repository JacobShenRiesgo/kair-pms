package com.j4va.kair;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/kairdb";
    private static final String DB_USER = "root";           // <-- change if needed
    private static final String DB_PASS = "YOUR_PASSWORD";  // <-- change this

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static void testConnection() {
        try (Connection c = connect()) {
            System.out.println("MySQL connected successfully!");
        } catch (Exception e) {
            System.err.println("MySQL connection FAILED");
            e.printStackTrace();
        }
    }

    // --- Users ---
    public static List<User> getUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id, email FROM users";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("email")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static User getUserByEmail(String email) {
        String sql = "SELECT id, email FROM users WHERE email = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new User(rs.getInt("id"), rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- Projects ---
    // Returns generated project ID or -1 on failure
    public static int insertProject(String name, LocalDate start, LocalDate end, String status) {
        String sql = "INSERT INTO project (name, start_date, end_date, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setDate(2, Date.valueOf(start));
            ps.setDate(3, Date.valueOf(end));
            ps.setString(4, status);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void assignUserToProject(int projectId, int userId) {
        String sql = "INSERT INTO user_project (user_id, project_id) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, projectId);
            ps.executeUpdate();
        } catch (Exception e) {
            // ignore duplicate key errors or print
            e.printStackTrace();
        }
    }

    public static void insertProjectStatus(int projectId, String status) {
        String sql = "INSERT INTO project_status (project_id, status) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, projectId);
            ps.setString(2, status);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load projects and map to Project class (assignee email if exists)
    public static List<Project> getAllProjects() {
        List<Project> list = new ArrayList<>();
        String sql = """
                SELECT p.id, p.name, p.start_date, p.end_date, COALESCE(ps.status, 'To Do') AS status,
                       u.email AS assignee_email
                FROM project p
                LEFT JOIN project_status ps ON ps.project_id = p.id
                LEFT JOIN user_project up ON up.project_id = p.id
                LEFT JOIN users u ON u.id = up.user_id
                """;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                LocalDate start = rs.getDate("start_date").toLocalDate();
                LocalDate end = rs.getDate("end_date").toLocalDate();
                String status = rs.getString("status");
                String assignee = rs.getString("assignee_email");
                if (assignee == null) assignee = "Unassigned";

                // priority column does not exist in DB; default to "Medium"
                list.add(new Project(id, name, assignee, "Medium", status, start, end));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
