package com.line.dao;

import com.line.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // spring이 관리할거야
public class UserDaoFactory {
    // 나도 관리해줘!
    @Bean
    public UserDao awsUserDao(){  // 날개 5개 선풍날개
        AWSConnectionMaker awsConnectionMaker = new AWSConnectionMaker();
        UserDao userDao = new UserDao(awsConnectionMaker);
        return userDao;
    }

    @Bean
    public UserDao localUserDao(){  // 날개 4개 선풍기
        UserDao userDao = new UserDao(new LocalConnectionMaker());
        return  userDao;
    }
}
