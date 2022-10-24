package com.line.dao;

import com.line.domain.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Map;

public class UserDao {

//    private ConnectionMaker connectionMaker;

    private final DataSource dataSource;
    private final JdbcContext jdbcContext;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource); // 구체적인 클래스 이름이 들어가는 구간
    }

//    public void setJdbcContext(JdbcContext jdbcContext) { // set으로 안하는 이유? xml설정 방식에서 set사용하기 떄문
//        this.jdbcContext = jdbcContext;
//    }


    public void add(final User user) throws SQLException {
        // user를 생성자로 굳이 넘겨주지 않아도 됨
        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
                PreparedStatement pstm = conn.prepareStatement("INSERT INTO users(id, name, password) values (?, ?, ?)");
                pstm.setString(1, user.getId());
                pstm.setString(2, user.getName());
                pstm.setString(3, user.getPassword());
                return pstm;
            }
        });
//        AddStrategy addStrategy = new AddStrategy(user);
//        JdbcContextWithStatementStrategy(addStrategy);
    }

    // JdbcContextWithStatementStrategy으로 공통로직 분리
    // WithStatementStrategy는 파라미터로 StatementStrategy를 받는 다는 것을 암시
    //
    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users");
    }

    // 공통로직 jdbcContext 넘겨주기
    // deleteAll() 안에 있을 때는 private인데 넘겨줄때 public으로 바뀜 - 둘다 public하면 안되나?
//    private void executeSql(final String query)throws SQLException {
//        this.jdbcContext.workWithStatementStrategy(new StatementStrategy() {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
//                return conn.prepareStatement(query);
//            }
//        });
//    }



//        StatementStrategy st = new DeleteAllStrategy();
//        JdbcContextWithStatementStrategy(st);
//        StatementStrategy deleteAllStrategy = new StatementStrategy() {
//            @Override
//            public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
//                return conn.prepareStatement("delete from users");
//            }
//        };
//    }


    // JdbcContext는 executeUpdate()를 사용하는 모든 곳에 사용할 수 있음
    // 왜냐하면 PreparedStatement만 바뀌기 떄문에 StatementStrategy의 구현체만 바꿔주면 긴 로직을 재용할 수 있음
    public void JdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();

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
        return 0;
//        String sql = "SELECT count(*) FROM users";
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            conn = connectionMaker.makeConnection();
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            rs.next();
//            return rs.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException();
//        } finally {
//            close(conn, ps, rs);
//        }
    }
//    public User findById(String id){
//        String sql = "select * from users where users.id=?";
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//
//        try {
//            conn = connectionMaker.makeConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, id);
//            rs = ps.executeQuery();
//
//
//            if (rs.next()) {
//                User user = new User(rs.getString("id"),
//                        rs.getString("name"), rs.getString("password"));
//                return user;
//            } else {
//                throw new RuntimeException();
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException();
//        } finally {
//            close(conn, ps, rs);
//        }
//    }

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
