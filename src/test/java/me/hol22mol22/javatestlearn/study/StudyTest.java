package me.hol22mol22.javatestlearn.study;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @Test
    void create_under_score(){
        Study study = new Study();
        assertNotNull(study);

        System.out.println("create");

    }
    @Test
    @Disabled
    void create_under_score1(){
        System.out.println("create1");

    }

    @Test
    @DisplayName("스터디 만들기 🍕")
    void create_test_with_displayName(){
        System.out.println("test");

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