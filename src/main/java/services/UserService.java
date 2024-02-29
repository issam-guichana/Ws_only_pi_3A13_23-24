package services;

import models.User;
import utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserService implements CRUD<User> {

    private Connection cnx;
    public UserService(){
        cnx = DBconnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(User user) throws SQLException {
        String req = "INSERT INTO `user`(`id_user`,`username`, `email`, `mdp`, `age`, `role`, `gender`, `image`, `status`) VALUES " +
                "(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, user.getId_user());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getEmail());
        ps.setString(4, user.getMdp());
        ps.setInt(5, user.getAge());
        ps.setString(6, user.getRole());
        ps.setString(7, user.getGender());
        ps.setString(8, user.getImage());
        ps.setInt(9, user.getStatus());

        ps.executeUpdate();
    }

    @Override
    public void updateOne(User user) {
        try {
        String req = "UPDATE `user` SET username = ?,email = ?,mdp = ?,age = ?,role = ?,gender = ?,image = ?,status = ? WHERE id_user = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getMdp());
        ps.setInt(4, user.getAge());
        ps.setString(5, user.getRole());
        ps.setString(6, user.getGender());
        ps.setString(7, user.getImage());
        ps.setInt(8, user.getStatus());
        ps.setInt(9, user.getId_user());

        ps.executeUpdate();

        System.out.println("Utlisateur est modifié");
    } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
    }

@Override
    public void SupprimerUser(int id) {

        try {
            System.out.println("supprimé");
            PreparedStatement pre = cnx.prepareStatement("DELETE FROM user WHERE id_user = ?");
            pre.setInt(1, id);
            pre.executeUpdate();

        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
    }
    @Override
    public List<User> selectAll() throws SQLException {
        List<User> userList = new ArrayList<>();

        String req = "SELECT * FROM `user`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            User u = new User();

            u.setId_user(rs.getInt((1)));
            u.setUsername(rs.getString((2)));
            u.setEmail(rs.getString((3)));
            u.setMdp(rs.getString((4)));
            u.setAge(rs.getInt((5)));
            u.setRole(rs.getString((6)));
            u.setGender(rs.getString((7)));
            u.setImage(rs.getString((8)));
            u.setStatus(rs.getInt((9)));

            userList.add(u);
        }

        return userList;
    }
    public User ChercherParUsername(String username) {
        try {
            PreparedStatement pre = cnx.prepareStatement("SELECT * FROM user where username = ?");
            pre.setString(1, username);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                User u = new User(result.getInt(1), result.getString(2),
                        result.getString(3), result.getString(4),
                        result.getInt(5), result.getString(6), result.getString(7)
                        , result.getString(8), result.getInt(9));
                return u;
            }
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
        return null;
    }
    public User ChercherParId(int id) {

        try {
            PreparedStatement pre = cnx.prepareStatement("SELECT * FROM user where id_user = ?");
            pre.setInt(1, id);
            ResultSet result = pre.executeQuery();
            while (result.next()) {
                User u = new User(result.getInt(1), result.getString(2),
                        result.getString(3), result.getString(4),
                        result.getInt(5), result.getString(6), result.getString(7)
                        , result.getString(8), result.getInt(9));
                return u;
            }
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
        return null;
    }

    public void updateStatus(User user) {
        try {
            String req = "UPDATE `user` SET username = ?, email = ?, mdp = ?, age = ?, role = ?, gender = ?, image = ?, status = (CASE WHEN status = 1 THEN 0 ELSE 1 END) WHERE id_user = ?";
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getMdp());
            ps.setInt(4, user.getAge());
            ps.setString(5, user.getRole());
            ps.setString(6, user.getGender());
            ps.setString(7, user.getImage());
            ps.setInt(8, user.getId_user());

            ps.executeUpdate();

            System.out.println("Status modified");
        } catch (SQLException ex) {
            System.out.print(ex.getMessage());
        }
    }

}
