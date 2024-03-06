package services;

import java.sql.SQLException;
import java.util.List;


public interface CRUD<T> {
    void InsertOne(T t) throws SQLException;
    List<T> SelectAll() throws SQLException;
    void UpdateOne(int id_msg,String nouveauContenu) throws SQLException;
    void DeleteOne(int id_msg) throws SQLException;
    void insertOne(T t) throws SQLException;
    void updateOne(T t) throws SQLException;
    void deleteOne(T t) throws SQLException;
    List<T> selectAll() throws SQLException;
}
