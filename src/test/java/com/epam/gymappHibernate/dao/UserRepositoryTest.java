package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("johndoe");
        user.setPassword("password");
        user.setActive(true);
        userRepository.saveUser(user);

        assertNotNull(user.getUserName());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe", user.getUserName());
        assertEquals("password", user.getPassword());
        assertTrue(user.isActive());
    }

    @Test

    public void testGetUserById() {
        Long userId = 1L;

        User user = userRepository.getUserById(userId);

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("Raul", user.getFirstName());
        assertEquals("Perez", user.getLastName());
        assertEquals("Raul.perez", user.getUserName());
        assertEquals("as45ww", user.getPassword());
        assertFalse(user.isActive());
    }

}