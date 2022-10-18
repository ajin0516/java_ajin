package com.line.dbexercise;

import com.line.dbexercise.domain.User;

import java.sql.*;
import java.util.Map;

public class UserDao {
    public void add() throws SQLException, ClassNotFoundException {
        Map<String, String> env = System.getenv();
        String dbHost = env.get("Db_HOST");
        String dbUser = env.get("Db_USER");
        String dbPassword = env.get("Db_PASSWORD");

        Class.forName("com.mysql.cj.jdbc.Driver"); // driver loading
        Connection conn = DriverManager.getConnection(dbHost, dbUser, dbPassword); // db연결
        PreparedStatement ps = conn.prepareStatement("INSERT INTO users(id, name,password) VALUES (?,?,?)");
        ps.setString(1, "1");
        ps.setString(2, "leeajin");
        ps.setString(3, "12345");

        ps.executeUpdate();  // 실행하라

        ps.close();
        conn.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Map<String, String> env = System.getenv();
        String dbHost = env.get("DB_HOST");
        String dbUser = env.get("DB_USER");
        String dbPassword = env.get("DB_PASSWORD");

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection(dbHost, dbUser, dbPassword);
        PreparedStatement ps = conn.prepareStatement("SELECT id, name, password FROM users WHERE id = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery(); // 4. (select문에 resultset, executeQuery 사용 - 주소값을 참조하고 있다고 생각하기) SQL 실행한 값 넘겨주기
        System.out.println(rs + " 삽입 성공");
        rs.next();  // 첫번째는 간 다음 다음으로 내려가라(row)
        User user = new User(rs.getString("id"), rs.getString("name"),
                rs.getString("password"));
        rs.close();
        ps.close();
        conn.close();
        return user;
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        UserDao userDao = new UserDao();

        User user = userDao.get("1");
        System.out.println("name = " + user);
    }
}

