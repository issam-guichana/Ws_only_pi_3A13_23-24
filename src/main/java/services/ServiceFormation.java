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
import models.User_formation;
import utils.DBConnection;

public class ServiceFormation implements CRUDF<Formation> {
    private Connection cnx = DBConnection.getInstance().getCnx();

    public ServiceFormation() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override

    public void insertOne(Formation formation) throws SQLException {
        String query = "INSERT INTO formation (`nom_form`, `description`, `user_id`, `cat_id`, `prix`, `certif_id`) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = this.cnx.prepareStatement(query);

        ps.setString(1, formation.getNom_form());
        ps.setString(2, formation.getDescription());
        ps.setInt(3, formation.getUser_id().getUser_id());
        ps.setInt(4, formation.getCategorie().getId_cat());
        ps.setInt(5, formation.getPrix());
        ps.setInt(6, formation.getCertif_id());
        ps.executeUpdate();
        System.out.println("Formation Added!");
    }

    @Override
    public void updateOne(Formation formation) throws SQLException {
        String query = "UPDATE `formation` SET `nom_form`=?, `description`=?, `user_id`=?, `cat_id`=?, `prix`=?, `certif_id`=? WHERE `id_form`=?";
        PreparedStatement ps = this.cnx.prepareStatement(query);

        ps.setString(1, formation.getNom_form());
        ps.setString(2, formation.getDescription());
        ps.setInt(3, formation.getUser_id().getUser_id());
        ps.setInt(4, formation.getCategorie().getId_cat());
        ps.setInt(5, formation.getPrix());
        ps.setInt(6, formation.getCertif_id());
        ps.setInt(7, formation.getId_form());
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

    public Categorie getCategorieById(int id) {
        Categorie categorie = null;

        try {
            String query = "SELECT * FROM `categorie` WHERE `id_cat` = ?";
            PreparedStatement pstmt = cnx.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                categorie = new Categorie(); // Créer une nouvelle instance de Categorie
                categorie.setId_cat(rs.getInt("id_cat"));
                categorie.setNom_cat(rs.getString("nom_cat"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return categorie;
    }
    public Categorie getCategorieByName(String name) {
        Categorie categorie = null;

        try {
            String query = "SELECT * FROM `categorie` WHERE `nom_cat` = ?";
            PreparedStatement pstmt = cnx.prepareStatement(query);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                categorie = new Categorie(); // Créer une nouvelle instance de Categorie
                categorie.setId_cat(rs.getInt("id_cat"));
                categorie.setNom_cat(rs.getString("nom_cat"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return categorie;
    }


    public List<Formation> selectAll() throws SQLException {
        List<Formation> formations = new ArrayList();
        String query = "SELECT * FROM `formation`";
        Statement st = this.cnx.createStatement();

        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId_form(rs.getInt("id_form"));
            formation.setNom_form(rs.getString("nom_form"));
            formation.setDescription(rs.getString("description"));

            User_formation user = new User_formation(rs.getInt("user_id"));
            formation.setUser_id(user);
            formation.setPrix(rs.getInt("prix"));
            formation.setCertif_id(rs.getInt("certif_id"));
            Categorie categorie = getCategorieById(rs.getInt("cat_id"));
            formation.setCategorie(categorie);
            formations.add(formation);
        }

        return formations;
    }

    public List<Formation> getFormationsByCategorie(Categorie categorie) {
        List<Formation> formations = new ArrayList<>();

        try {
            String query = "SELECT * FROM `formation` WHERE `cat_id` = ?";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, categorie.getId_cat());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Formation formation = new Formation();
                formation.setId_form(rs.getInt("id_form"));
                formation.setNom_form(rs.getString("nom_form"));
                formation.setDescription(rs.getString("description"));
                User_formation user = new User_formation(rs.getInt("user_id"));
                formation.setUser_id(user);
                formation.setCategorie(categorie);

                formations.add(formation);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return formations;
    }

    public List<Integer> getAllFormateursIDs() throws SQLException {
        List<Integer> formateursIDs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getInstance().getCnx();
            String sql = "SELECT DISTINCT user_id FROM formation";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("user_id");
                formateursIDs.add(userID);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return formateursIDs;
    }
    private static final int BATCH_SIZE = 10;
    public List<Formation> getFormationsBatch(int startIndex) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation LIMIT ?, ?";
        PreparedStatement pstmt = cnx.prepareStatement(query);
        pstmt.setInt(1, startIndex);
        pstmt.setInt(2, BATCH_SIZE);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId_form(rs.getInt("id_form"));
            formation.setNom_form(rs.getString("nom_form"));
            formation.setDescription(rs.getString("description"));
            User_formation user = new User_formation(rs.getInt("user_id"));
            formation.setUser_id(user);
            int categorieId = rs.getInt("cat_id");
            Categorie categorie = getCategorieById(categorieId);
            formation.setCategorie(categorie);
            formations.add(formation);
        }

        return formations;
    }
    public void insertOneWithoutId(Formation formation) throws SQLException {
        String query = "INSERT INTO formation (`nom_form`, `description`, `user_id`, `cat_id`) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = this.cnx.prepareStatement(query);

        ps.setString(1, formation.getNom_form());
        ps.setString(2, formation.getDescription());

        ps.setInt(4, formation.getCategorie().getId_cat()); // Utilisation de l'identifiant de la catégorie
        ps.executeUpdate();
        System.out.println("Formation Added!");
    }
    public List<Formation> searchFormations(String keyword) throws SQLException {
        List<Formation> filteredFormations = new ArrayList<>();
        String query = "SELECT * FROM `formation` WHERE `nom_form` LIKE ?";
        PreparedStatement pstmt = cnx.prepareStatement(query);
        pstmt.setString(1, "%" + keyword + "%");
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Formation formation = new Formation();
            formation.setId_form(rs.getInt("id_form"));
            formation.setNom_form(rs.getString("nom_form"));
            formation.setDescription(rs.getString("description"));
            formation.setUser_id(new User_formation(rs.getInt("user_id"))); // Utilisation du constructeur avec l'ID utilisateur
            formation.setPrix(rs.getInt("prix"));
            formation.setCertif_id(rs.getInt("certif_id"));
            int categorieId = rs.getInt("cat_id");
            Categorie categorie = getCategorieById(categorieId);
            formation.setCategorie(categorie);
            filteredFormations.add(formation);
        }

        return filteredFormations;
    }

    public List<String> getAllFormationNames() throws SQLException {
        List<String> formationNames = new ArrayList<>();
        String query = "SELECT nom_form FROM formation";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                formationNames.add(resultSet.getString("nom_form"));
            }
        }
        return formationNames;
    }
    public List<Integer> getAllCertificatIDs() throws SQLException {
        List<Integer> certificatIDs = new ArrayList<>();
        String query = "SELECT DISTINCT certificat_id FROM formation";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                certificatIDs.add(resultSet.getInt("certificat_id"));
            }
        }
        return certificatIDs;
    }
    public List<Formation> getFormationsByFormateur(User_formation formateur) throws SQLException {
        List<Formation> formations = new ArrayList<>();

        // Requête SQL pour sélectionner les formations du formateur
        String query = "SELECT * FROM formation WHERE user_id = ?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Définir les paramètres de la requête
            statement.setInt(1, formateur.getUser_id());

            // Exécuter la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                // Parcourir les résultats et créer des objets Formation
                while (resultSet.next()) {
                    Formation formation = new Formation();
                    formation.setId_form(resultSet.getInt("id_form"));
                    formation.setNom_form(resultSet.getString("nom_form"));
                    formation.setDescription(resultSet.getString("description"));
                    formation.setPrix(resultSet.getInt("prix"));
                    formation.setCategorie(getCategorieById(resultSet.getInt("cat_id")));
                    // Autres attributs de la formation à définir...
                    formations.add(formation);
                }
            }
        }

        return formations;
    }
    public List<Formation> getFormationsByClient(User_formation client) throws SQLException {
        List<Formation> formations = new ArrayList<>();

        // Requête SQL pour sélectionner les formations du client
        String query = "SELECT f.* " +
                "FROM formation f " +
                "INNER JOIN inscription i ON f.id_form = i.id_form " +
                "WHERE i.id_client = ? AND i.paye = true";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            // Définir les paramètres de la requête
            statement.setInt(1, client.getUser_id());

            // Exécuter la requête
            try (ResultSet resultSet = statement.executeQuery()) {
                // Parcourir les résultats et créer des objets Formation
                while (resultSet.next()) {
                    Formation formation = new Formation();
                    formation.setId_form(resultSet.getInt("id_form"));
                    formation.setNom_form(resultSet.getString("nom_form"));
                    formation.setDescription(resultSet.getString("description"));
                    formation.setPrix(resultSet.getInt("prix"));
                    formation.setCategorie(getCategorieById(resultSet.getInt("cat_id")));
                    // Autres attributs de la formation à définir...
                    formations.add(formation);
                }
            }
        }

        return formations;
    }



}


