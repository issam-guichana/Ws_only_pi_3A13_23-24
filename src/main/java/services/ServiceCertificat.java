package services;

import com.mysql.cj.conf.ConnectionUrlParser;
import javafx.util.Pair;
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
        String query = "INSERT INTO Certificat (`nom_certif`, `date_certif`) VALUES (?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Explicitly set nom_certif as a string
            ps.setString(1, certificat.getNomCertif());
            ps.setDate(2, certificat.getDateCertif());
           // ps.setInt(3, certificat.getFormationId());
            ps.executeUpdate();
            System.out.println("Certificat Added!");
        }
    }

    @Override
    public void updateOne(Certificat certificat) throws SQLException {
        String query = "UPDATE `certificat` SET `nom_certif`=?, `date_certif`=? WHERE `id_certif`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, certificat.getNomCertif());
            ps.setDate(2, certificat.getDateCertif());
            //ps.setInt(3, certificat.getFormationId());
            ps.setInt(3, certificat.getId());
            ps.executeUpdate();
            System.out.println("Certificat Updated!");
        }
    }

    @Override
    public void deleteOne(Certificat certificat) throws SQLException {
        String query = "DELETE FROM `certificat` WHERE `id_certif`=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, certificat.getId());
            ps.executeUpdate();
            System.out.println("Certificat Deleted!");
        }
    }

    @Override
    public List<Certificat> selectAll() throws SQLException {
        List<Certificat> certificats = new ArrayList<>();
        String query = "SELECT * FROM `certificat`";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Certificat certificat = new Certificat();
                certificat.setId(rs.getInt("id_certif"));
                certificat.setNomCertif(rs.getString("nom_certif"));
                certificat.setDateCertif(rs.getDate("date_certif"));
                //certificat.setFormationId(rs.getInt("formation_id"));
                certificats.add(certificat);
            }
        }
        return certificats;
    }

    public List<Pair<String, Integer>> getCertificatNombrePersonnes() {
        // Code pour récupérer les données de la base de données
        // SELECT nomCertif, COUNT(*) FROM personnes GROUP BY nomCertif

        // Exemple de données simulées
        List<Pair<String, Integer>> certificatNombrePersonnes = new ArrayList<>();
        certificatNombrePersonnes.add(new Pair<>("Certificat 1", 10));
        certificatNombrePersonnes.add(new Pair<>("Certificat 2", 15));
        certificatNombrePersonnes.add(new Pair<>("Certificat 3", 5));

        return certificatNombrePersonnes;
    }
}