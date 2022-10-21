package com.line.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Connection 받은 후 쿼리작성해서 PraparedStatement를 리턴하는 DeleteAllStrategy
public class DeleteAllStrategy implements StatementStrategy{
    @Override
    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
       return conn.prepareStatement("delete from users");
    }
}
