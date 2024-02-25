package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import models.Categorie;
import utils.DBConnection;

public class ServiceCategorie implements CRUDC<Categorie> {
    private Connection cnx = DBConnection.getInstance().getCnx();

    public ServiceCategorie() { cnx = DBConnection.getInstance().getCnx();
    }
    @Override
    public void insertOne(Categorie categorie) throws SQLException {
        String query = "INSERT INTO categorie (`nom_cat`) VALUES (?)";
        PreparedStatement ps = this.cnx.prepareStatement(query);


            ps.setString(1, categorie.getNom_cat());
            ps.executeUpdate();
            System.out.println("Category Added!");


    }
    @Override
    public void updateOne(Categorie categorie) throws SQLException {
        String query = "UPDATE `categorie` SET `nom_cat`=? WHERE `id_cat`=?";
        PreparedStatement ps = this.cnx.prepareStatement(query);


            ps.setString(1, categorie.getNom_cat());
            ps.setInt(2, categorie.getId_cat());
            ps.executeUpdate();
            System.out.println("Category Updated!");


    }
    @Override
    public void deleteOne(Categorie categorie) throws SQLException {
        String query = "DELETE FROM `categorie` WHERE `id_cat`=?";
        PreparedStatement ps = this.cnx.prepareStatement(query);

            ps.setInt(1, categorie.getId_cat());
            ps.executeUpdate();
            System.out.println("Category!");


    }
    @Override
    public List<Categorie> selectAll() throws SQLException {
        List<Categorie> categories = new ArrayList();
        String query = "SELECT * FROM `categorie`";
        Statement st = this.cnx.createStatement();


            ResultSet rs = st.executeQuery(query);


                while(rs.next()) {
                    Categorie categorie = new Categorie();
                    categorie.setId_cat(rs.getInt("id_cat"));
                    categorie.setNom_cat(rs.getString("nom_cat"));
                    categories.add(categorie);
                }


        return categories;
    }
}
