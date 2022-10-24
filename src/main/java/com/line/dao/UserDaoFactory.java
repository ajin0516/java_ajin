package com.line.dao;

import com.line.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configuration // spring이 관리할거야
public class UserDaoFactory {

    public UserDao awsUserDao(){
        return new UserDao(awsDataSource());
    }

    @Bean
    public DataSource awsDataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        Map<String, String> env = System.getenv();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl(env.get("DB_HOST"));
        dataSource.setUsername(env.get("DB_USER"));
        dataSource.setPassword(env.get("DB_PASSWORD"));
        return dataSource;
    }

    @Bean
    public DataSource localDataSource() {
        Map<String, String> env = System.getenv();
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("localhost");
        dataSource.setUsername("root");
        dataSource.setPassword("12345678");
        return dataSource;
    }

    // 나도 관리해줘!
//    @Bean
//    public UserDao  awsUserDao(){  // 날개 5개 선풍날개
//        AWSConnectionMaker awsConnectionMaker = new AWSConnectionMaker();
//        UserDao userDao = new UserDao(awsConnectionMaker);
//        return userDao;
//    }
//
//    @Bean
//    public UserDao localUserDao(){  // 날개 4개 선풍기
//        UserDao userDao = new UserDao(new LocalConnectionMaker());
//        return  userDao;
//    }
}
