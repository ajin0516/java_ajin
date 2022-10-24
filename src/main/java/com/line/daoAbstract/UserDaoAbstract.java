package com.line.daoAbstract;

import com.line.domain.User;

import java.sql.*;
import java.util.Map;

public abstract class  UserDaoAbstract {

    public abstract Connection makeConnection() throws SQLException, ClassNotFoundException;


    public void add() throws ClassNotFoundException, SQLException {
        Connection conn = makeConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users(id,name,password) VALUES (?,?,?)");
        ps.setString(1, "1");
        ps.setString(2, "ajin");
        ps.setString(3, "1234");

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public User findById(String id) throws ClassNotFoundException, SQLException {
        Connection conn = makeConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT id, name, password FROM  users WHERE id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
            return user;
        }

        rs.close();
        ps.close();
        conn.close();

        return null;
    }



}
