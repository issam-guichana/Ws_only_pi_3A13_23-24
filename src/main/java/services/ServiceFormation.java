package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Categorie;
import models.Formation;
import utils.DBConnection;

public abstract class ServiceFormation implements CRUDF<Formation> {
    private static Connection cnx = DBConnection.getInstance().getCnx();

    public ServiceFormation() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override

    public void insertOne(Formation formation) throws SQLException {
        String query = "INSERT INTO formation (`nom_form`, `description`, `user_id`, `cat_id`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = this.cnx.prepareStatement(query);

        ps.setString(1, formation.getNom_form());
        ps.setString(2, formation.getDescription());
        ps.setInt(3, formation.getUser_id());
        ps.setInt(4, formation.getCategorie().getId_cat()); // Utilisation de l'identifiant de la catégorie
        ps.executeUpdate();
        System.out.println("Formation Added!");
    }

    @Override
    public void updateOne(Formation formation) throws SQLException {
        String query = "UPDATE `formation` SET `nom_form`=?, `description`=?, `user_id`=?, `cat_id`=? WHERE `id_form`=?";
        PreparedStatement ps = this.cnx.prepareStatement(query);

        ps.setString(1, formation.getNom_form());
        ps.setString(2, formation.getDescription());
        ps.setInt(3, formation.getUser_id());
        ps.setInt(4, formation.getCategorie().getId_cat()); // Utilisation de l'identifiant de la catégorie
        ps.setInt(5, formation.getId_form());
        ps.executeUpdate();
        System.out.println("Formation Updated!");
    }


    @Override
    public void deleteOne(Formation formation) throws SQLException {
        String query = "DELETE FROM `formation` WHERE `id_form`=?";
        PreparedStatement ps = this.cnx.prepareStatement(query);


        ps.setInt(1, formation.getId_form());
        ps.executeUpdate();
        System.out.println("Formation!");

    }

    public static Categorie getCategorieById(int id) {
        Categorie categorie = null;

        try {
            String query = "SELECT * FROM `categories` WHERE `id_cat` = ?";
            PreparedStatement pstmt = cnx.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                categorie = new Categorie();
                categorie.setId_cat(rs.getInt("id_cat"));
                categorie.setNom_cat(rs.getString("nom_cat"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return categorie;
    }

    @Override
    public List<Formation> selectAll() throws SQLException {
        List<Formation> formations = new ArrayList()

        String query = "SELECT * FROM `formation`";
        Statement st = this.cnx.createStatement();

        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId_form(rs.getInt("id_form"));
            formation.setNom_form(rs.getString("nom_form"));
            formation.setDescription(rs.getString("description"));
            formation.setUser_id(rs.getInt("user_id"));
            Categorie categorie = getCategorieById(rs.getInt("cat_id"));
            formation.setCategorie(categorie);
            formations.add(formation);
        }


        return formations;
    }

    public static List<Formation> getFormationsByCategory(String categoryName) {
        List<Formation> formations = new ArrayList<>();

        try {
            // Requête SQL pour sélectionner les formations associées à la catégorie spécifiée
            String query = "SELECT * FROM `formation` WHERE `cat_nom` = ?";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();

            // Parcours des résultats de la requête et ajout des formations à la liste
            while (rs.next()) {
                Formation formation = new Formation();
                formation.setId_form(rs.getInt("id_form"));
                formation.setNom_form(rs.getString("nom_form"));
                formation.setDescription(rs.getString("description"));
                formation.setUser_id(rs.getInt("user_id"));
                int categorieId = rs.getInt("cat_id");
                Categorie categorie = getCategorieById(categorieId);
                formation.setCategorie(categorie);

                formations.add(formation);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return formations;
    }

}


