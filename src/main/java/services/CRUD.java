package services;

import java.sql.SQLException;
import java.util.List;


public interface CRUD<T> {
    void InsertOne(T t) throws SQLException;
    List<T> SelectAll() throws SQLException;
    void UpdateOne(int id_msg,String nouveauContenu) throws SQLException;

    void DeleteOne(int id_msg) throws SQLException;
    //void SelectOne(T t) throws SQLException;




}
