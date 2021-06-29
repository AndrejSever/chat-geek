package ru.gb.chat.server;

import static org.junit.jupiter.api.Assertions.*;

class ListAuthServiceTest {
    User user1;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        user1 = new User("log1","pass1","nick1");
        System.out.println("Start Test");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.out.println("End Test");
    }

    @org.junit.jupiter.api.Test
    void getInstance() {
        assertNotNull(user1);
    }

    @org.junit.jupiter.api.Test
    void findByLoginAndPassword() {
        assertSame("nick", user1.getNickname());
    }

    @org.junit.jupiter.api.Test
    void findByLoginOrNick() {
        assertNotNull(user1);
    }

    @org.junit.jupiter.api.Test
    void save() {
        assertNull(user1);
    }

    @org.junit.jupiter.api.Test
    void remove() {
        assertSame("nick1", user1.getNickname());
    }
}