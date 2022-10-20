package com.line.dao;

import com.line.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
//
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {
    @Autowired
    ApplicationContext context;

    @Test
    void addAndSelect() throws SQLException, ClassNotFoundException {
        UserDao userDao = context.getBean("awsUserDao", UserDao.class);
        UserDao userDao2 = context.getBean("awsUserDao", UserDao.class);

        System.out.println(userDao);
        System.out.println(userDao2);
//        UserDao userDao = new UserDao();
//        UserDao userDao = new UserDaoFactory().awsUserDao();

//        String id ="12";
//        User user = new User(id, "twelve", "1234");
//        userDao.add(user);

//        User findUser = userDao.findById(id);
//        Assertions.assertEquals("twelve",findUser.getName());
        userDao.deleteAll();
        Assertions.assertEquals(userDao.getCount(), 0);

        User user = new User("1","ajin","1234");
        userDao.add(user);
        Assertions.assertEquals(userDao.getCount(), 1);

    }

}