package services;

import models.Certificat;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCertificat implements CRUD<Certificat> {

    private Connection cnx;

    public ServiceCertificat() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Certificat certificat) throws SQLException {
        String query = "INSERT INTO Certificat (`nom_certif`, `date_certif`, `formation_id`) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Explicitly set nom_certif as a string
            ps.setString(1, certificat.getNomCertif());
            ps.setDate(2, certificat.getDateCertif());
            ps.setInt(3, certificat.getFormationId());
            ps.executeUpdate();
            System.out.println("Certificat Added!");
        }
    }

    @Override
    public void updateOne(Certificat certificat) throws SQLException {
        String query = "UPDATE `certificat` SET `nom_certif`=?, `date_certif`=?, `formation_id`=? WHERE `id`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, certificat.getNomCertif());
            ps.setDate(2, certificat.getDateCertif());
            ps.setInt(3, certificat.getFormationId());
            ps.setInt(4, certificat.getId());
            ps.executeUpdate();
            System.out.println("Certificat Updated!");
        }
    }

    @Override
    public void deleteOne(Certificat certificat) throws SQLException {
        String query = "DELETE FROM `certificat` WHERE `id`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, certificat.getId());
            ps.executeUpdate();
            System.out.println("Certificat Deleted!");
        }
    }

    @Override
    public  List<Certificat> selectAll() throws SQLException {
        List<Certificat> certificats = new ArrayList<>();
        String query = "SELECT * FROM `certificat`";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Certificat certificat = new Certificat();
                certificat.setId(rs.getInt("id"));
                certificat.setNomCertif(rs.getString("nom_certif"));
                certificat.setDateCertif(rs.getDate("date_certif"));
                certificat.setFormationId(rs.getInt("formation_id"));
                certificats.add(certificat);
            }
        }
        return certificats;
    }
}