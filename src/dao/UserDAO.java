
package dao;

import model.User;
import db.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {


    public boolean createUser(User user) {
        String sql = "INSERT INTO Users (UserID, Name, Email, Password) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));
                user.setPassword(rs.getString("Password"));

                return user;
            }

        } catch (SQLException e) {
            System.err.println("Error getting user by email: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return null;
    }


    public User login(String email, String password) {
        String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));

                return user;
            }

        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return null;
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("UserID"));
                user.setName(rs.getString("Name"));
                user.setEmail(rs.getString("Email"));

                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all users: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return users;
    }


    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET Name = ?, Email = ?, Password = ? WHERE UserID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getUserID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public boolean updateUserImage(int userID, byte[] image) {
        String sql = "UPDATE Users SET UserImage = ? WHERE UserID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBytes(1, image);
            pstmt.setInt(2, userID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user image: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, userID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }
}
