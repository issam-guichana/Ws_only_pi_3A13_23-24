
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
        String query = "INSERT INTO Badge (`nom_badge`, `type`, `formation_id`) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, badge.getNomBadge());
            ps.setString(2, badge.getType());
            ps.setInt(3, badge.getFormationId());
            ps.executeUpdate();
            System.out.println("Badge Added!");
        }
    }

    @Override
    public void updateOne(Badge badge) throws SQLException {
        String query = "UPDATE Badge SET `nom_badge`=?, `type`=?, `formation_id`=? WHERE `id_badge`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, badge.getNomBadge());
            ps.setString(2, badge.getType());
            ps.setInt(3, badge.getFormationId());
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
                badge.setFormationId(rs.getInt("formation_id"));
                badges.add(badge);
            }
        }
        return badges;
    }
}
