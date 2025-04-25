
package dao;

import model.FoundItem;
import db.DBConnection;
import util.DateUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class FoundItemDAO {


    public boolean reportFoundItem(FoundItem item) {
        String sql = "INSERT INTO FoundItems (FoundItemID, F_Title, F_Description, DateFound, F_Location) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, item.getFoundItemID());
            pstmt.setString(2, item.getTitle());
            pstmt.setString(3, item.getDescription());
            pstmt.setDate(4, DateUtils.convertToSqlDate(item.getDateFound()));
            pstmt.setString(5, item.getLocation());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error reporting found item: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public FoundItem getFoundItemById(int itemId) {
        String sql = "SELECT * FROM FoundItems WHERE FoundItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                FoundItem item = new FoundItem();
                item.setFoundItemID(rs.getInt("FoundItemID"));
                item.setTitle(rs.getString("F_Title"));
                item.setDescription(rs.getString("F_Description"));
                item.setDateFound(DateUtils.convertFromSqlDate(rs.getDate("DateFound")));
                item.setLocation(rs.getString("F_Location"));
                item.setImage(rs.getBytes("F_Image"));

                return item;
            }

        } catch (SQLException e) {
            System.err.println("Error getting found item by ID: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return null;
    }


    public List<FoundItem> getAllFoundItems() {
        List<FoundItem> foundItems = new ArrayList<>();
        String sql = "SELECT * FROM FoundItems";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                FoundItem item = new FoundItem();
                item.setFoundItemID(rs.getInt("FoundItemID"));
                item.setTitle(rs.getString("F_Title"));
                item.setDescription(rs.getString("F_Description"));
                item.setDateFound(DateUtils.convertFromSqlDate(rs.getDate("DateFound")));
                item.setLocation(rs.getString("F_Location"));
                item.setImage(rs.getBytes("F_Image"));

                foundItems.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all found items: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return foundItems;
    }


    public List<FoundItem> searchFoundItems(String keyword) {
        List<FoundItem> matchingItems = new ArrayList<>();
        String sql = "SELECT * FROM FoundItems WHERE F_Title LIKE ? OR F_Description LIKE ? OR F_Location LIKE ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            String searchTerm = "%" + keyword + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FoundItem item = new FoundItem();
                item.setFoundItemID(rs.getInt("FoundItemID"));
                item.setTitle(rs.getString("F_Title"));
                item.setDescription(rs.getString("F_Description"));
                item.setDateFound(DateUtils.convertFromSqlDate(rs.getDate("DateFound")));
                item.setLocation(rs.getString("F_Location"));
                item.setImage(rs.getBytes("F_Image"));
                matchingItems.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error searching found items: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return matchingItems;
    }


    public boolean updateFoundItem(FoundItem item) {
        String sql = "UPDATE FoundItems SET F_Title = ?, F_Description = ?, DateFound = ?, F_Location = ? WHERE FoundItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, item.getTitle());
            pstmt.setString(2, item.getDescription());
            pstmt.setDate(3, DateUtils.convertToSqlDate(item.getDateFound()));
            pstmt.setString(4, item.getLocation());
            pstmt.setInt(5, item.getFoundItemID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating found item: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public boolean updateFoundItemImage(int itemId, byte[] image) {
        String sql = "UPDATE FoundItems SET F_Image = ? WHERE FoundItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBytes(1, image);
            pstmt.setInt(2, itemId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating found item image: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public boolean deleteFoundItem(int itemId) {
        String sql = "DELETE FROM FoundItems WHERE FoundItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, itemId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting found item: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }
}
