package me.hol22mol22.javatestlearn.study;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    @Test
    void create(){
        Study study = new Study();
        assertNotNull(study);

        System.out.println("create");

    }
    @Test
    @Disabled
    void create1(){
        System.out.println("create1");

    }

    @BeforeAll
    static void beforeAll(){
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("afterAll");
    }

    @BeforeEach
    void beforeEach(){
        System.out.println("Before Each");
    }



}