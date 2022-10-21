package com.line.dao;

import com.line.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserDaoFactory.class)
class UserDaoTest {
    @Autowired
    ApplicationContext context;

    UserDao userDao;
    User user1;
    User user2;
    User user3;

    // @BeforeEach를 붙인 메서드는 테스트 메서드 실행 이전에 수행됨
    @BeforeEach
    void setUp() {
        this.userDao = context.getBean("awsUserDao" , UserDao.class); //getBean  Bean을 지정한 이름으로 조회
        userDao.deleteAll();
        userDao.add(new User("100","first","486"));
        assertEquals(1,userDao.getCount());
        userDao.deleteAll();
    }
    // test는 실행 순서에 상관없이 독립적
    @Test
    void addAndSelect()  {
        User user1 = new User("5","sdjin","12223");
        assertEquals(0, userDao.getCount());

        userDao.add(user1);

        assertEquals(1, userDao.getCount());
        User selectUser = userDao.findById(user1.getId());
        assertEquals("sdjin",selectUser.getName()) ;
        assertEquals("12223",user1.getPassword());
    }

    @Test
    void count(){
        userDao.deleteAll();
        assertEquals(0,userDao.getCount());
        user1 = new User("1","ajin","123");
        user2 = new User("2","young","456");
        user3 = new User("3","jin","789");

        UserDao userDao = context.getBean("awsUserDao", UserDao.class);
        userDao.deleteAll();
        assertEquals(0,userDao.getCount());

        userDao.add(user1);
        assertEquals(1,userDao.getCount());

        userDao.add(user2);
        assertEquals(2,userDao.getCount());

        userDao.add(user3);
        assertEquals(3,userDao.getCount());
    }

    @Test
    void findByIdEmpty(){
        assertThrows(RuntimeException.class, () -> {
            userDao.findById("1000");
        });
    }
}