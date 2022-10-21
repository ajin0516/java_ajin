package com.line.dao;

import com.line.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStrategy implements StatementStrategy{
    public User user;

    public AddStrategy(User user){
        this.user = user;
    }
    @Override
    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
        PreparedStatement pstm = conn.prepareStatement("INSERT INTO users(id, name, password) values (?, ?, ?)");
        pstm.setString(1, user.getId());
        pstm.setString(2, user.getName());
        pstm.setString(3, user.getPassword());
        return pstm;
    }
}
