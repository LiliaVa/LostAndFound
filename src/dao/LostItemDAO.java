
package dao;

import model.LostItem;
import db.DBConnection;
import util.DateUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LostItemDAO {


    public boolean reportLostItem(LostItem item) {
        String sql = "INSERT INTO LostItems (LostItemID, L_Title, L_Description, DateLost, L_Location) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, item.getLostItemID());
            pstmt.setString(2, item.getTitle());
            pstmt.setString(3, item.getDescription());
            pstmt.setDate(4, DateUtils.convertToSqlDate(item.getDateLost()));
            pstmt.setString(5, item.getLocation());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error reporting lost item: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public LostItem getLostItemById(int itemId) {
        String sql = "SELECT * FROM LostItems WHERE LostItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                LostItem item = new LostItem();
                item.setLostItemID(rs.getInt("LostItemID"));
                item.setTitle(rs.getString("L_Title"));
                item.setDescription(rs.getString("L_Description"));
                item.setDateLost(DateUtils.convertFromSqlDate(rs.getDate("DateLost")));
                item.setLocation(rs.getString("L_Location"));
                item.setImage(rs.getBytes("L_Image"));

                return item;
            }

        } catch (SQLException e) {
            System.err.println("Error getting lost item by ID: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return null;
    }


    public List<LostItem> getAllLostItems() {
        List<LostItem> lostItems = new ArrayList<>();
        String sql = "SELECT * FROM LostItems";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                LostItem item = new LostItem();
                item.setLostItemID(rs.getInt("LostItemID"));
                item.setTitle(rs.getString("L_Title"));
                item.setDescription(rs.getString("L_Description"));
                item.setDateLost(DateUtils.convertFromSqlDate(rs.getDate("DateLost")));
                item.setLocation(rs.getString("L_Location"));
                item.setImage(rs.getBytes("L_Image"));

                lostItems.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error getting all lost items: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return lostItems;
    }


    public List<LostItem> searchLostItems(String keyword) {
        List<LostItem> matchingItems = new ArrayList<>();
        String sql = "SELECT * FROM LostItems WHERE L_Title LIKE ? OR L_Description LIKE ? OR L_Location LIKE ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                LostItem item = new LostItem();
                item.setLostItemID(rs.getInt("LostItemID"));
                item.setTitle(rs.getString("L_Title"));
                item.setDescription(rs.getString("L_Description"));
                item.setDateLost(DateUtils.convertFromSqlDate(rs.getDate("DateLost")));
                item.setLocation(rs.getString("L_Location"));
                item.setImage(rs.getBytes("L_Image"));
                matchingItems.add(item);
            }

        } catch (SQLException e) {
            System.err.println("Error searching lost items: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }

        return matchingItems;
    }


    public boolean updateLostItem(LostItem item) {
        String sql = "UPDATE LostItems SET L_Title = ?, L_Description = ?, DateLost = ?, L_Location = ? WHERE LostItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, item.getTitle());
            pstmt.setString(2, item.getDescription());
            pstmt.setDate(3, DateUtils.convertToSqlDate(item.getDateLost()));
            pstmt.setString(4, item.getLocation());
            pstmt.setInt(5, item.getLostItemID());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating lost item: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public boolean updateLostItemImage(int itemId, byte[] image) {
        String sql = "UPDATE LostItems SET L_Image = ? WHERE LostItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBytes(1, image);
            pstmt.setInt(2, itemId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating lost item image: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }


    public boolean deleteLostItem(int itemId) {
        String sql = "DELETE FROM LostItems WHERE LostItemID = ?";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, itemId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting lost item: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                DBConnection.releaseConnection(conn);
            }
        }
    }
}
