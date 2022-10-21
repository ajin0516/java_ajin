package com.line.dao;

import com.line.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;
import java.util.Map;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao() {
        this.connectionMaker = new AWSConnectionMaker();
    }
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }


    public void add(User user) throws SQLException {
        AddStrategy addStrategy = new AddStrategy(user);
        JdbcContextWithStatementStrategy(addStrategy);
    }

    // JdbcContextWithStatementStrategy으로 공통로직 분리
    // WithStatementStrategy는 파라미터로 StatementStrategy를 받는 다는 것을 암시
    //
    public void deleteAll() throws SQLException {
        StatementStrategy st = new DeleteAllStrategy();
        JdbcContextWithStatementStrategy(st);
    }

    // JdbcContext는 executeUpdate()를 사용하는 모든 곳에 사용할 수 있음
    // 왜냐하면 PreparedStatement만 바뀌기 떄문에 StatementStrategy의 구현체만 바꿔주면 긴 로직을 재용할 수 있음
    public void JdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionMaker.makeConnection();

            ps = stmt.makePreparedStatement(conn);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {  // 무조건 닫아주기
            close(conn, ps);
        }
    }

    // deleteAll() 실행 후 테이블에 남은 게 있는지 확인해야 함

    // deletaAll()만 Test하기에는 부족, getCount()추가해서 검증해주기
    public int getCount() {
        String sql = "SELECT count(*) FROM users";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionMaker.makeConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            close(conn, ps, rs);
        }
    }
    public User findById(String id){
        String sql = "select * from users where users.id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionMaker.makeConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();


            if (rs.next()) {
                User user = new User(rs.getString("id"),
                        rs.getString("name"), rs.getString("password"));
                return user;
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        } finally {
            close(conn, ps, rs);
        }
    }

    private void close(AutoCloseable... autoCloseable) {
        for (AutoCloseable ac : autoCloseable) {
            if (ac != null) {
                try {
                    ac.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
