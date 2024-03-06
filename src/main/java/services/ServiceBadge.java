
package services;

import models.Badge;
import services.CRUD;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceBadge implements CRUD<Badge> {

    private Connection cnx;

    public ServiceBadge() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Badge badge) throws SQLException {
       // String query = "INSERT INTO Badge (`nom_badge`, `type`, `formation_id`) VALUES (?, ?, ?)";
         String query = "INSERT INTO Badge (`nom_badge`, `type` , `img_badge`) VALUES (?, ?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, badge.getNomBadge());
            ps.setString(2, badge.getType());
            ps.setString(3, badge.getImgBadge());
            ps.executeUpdate();
            System.out.println("Badge Added!");
        }
    }

    @Override
    public void updateOne(Badge badge) throws SQLException {
        String query = "UPDATE Badge SET `nom_badge`=?, `type`=? ,`img_badge`=? WHERE `id_badge`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, badge.getNomBadge());
            ps.setString(2, badge.getType());
            ps.setString(3, badge.getImgBadge());
            ps.setInt(4, badge.getIdBadge());
            ps.executeUpdate();
            System.out.println("Badge Updated!");
        }
    }

    @Override
    public void deleteOne(Badge badge) throws SQLException {
        String query = "DELETE FROM Badge WHERE `id_badge`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, badge.getIdBadge());
            ps.executeUpdate();
            System.out.println("Badge Deleted!");
        }
    }

    @Override
    public List<Badge> selectAll() throws SQLException {
        List<Badge> badges = new ArrayList<>();
        String query = "SELECT * FROM Badge";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Badge badge = new Badge();
                badge.setIdBadge(rs.getInt("id_badge"));
                badge.setNomBadge(rs.getString("nom_badge"));
                badge.setType(rs.getString("type"));
                badge.setImgBadge(rs.getString("img_badge"));
                badges.add(badge);
            }
        }
        return badges;
    }


    public int countTotalBadges() throws SQLException {
        int totalBadges = 0;
        String query = "SELECT COUNT(*) AS total FROM Badge"; // Change table name if needed
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                totalBadges = rs.getInt("total");
            }
        }
        return totalBadges;
    }

    public String findMostAttributedBadge() throws SQLException {
        String query = "SELECT id_badge, COUNT(*) AS count FROM usr_badge GROUP BY id_badge ORDER BY count DESC LIMIT 1"; // Corrected table name
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int badgeId = rs.getInt("id_badge");
                String badgeName = getBadgeNameById(badgeId);
                return badgeName;
            }
        }
        return "No Badge Found";
    }


    private String getBadgeNameById(int badgeId) throws SQLException {
        String query = "SELECT nom_badge FROM Badge WHERE id_badge = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, badgeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nom_badge");
                }
            }
        }
        return "Unknown Badge";
    }

}

