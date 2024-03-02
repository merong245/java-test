package me.hol22mol22.javatestlearn.study;

import org.junit.jupiter.api.*;

import static me.hol22mol22.javatestlearn.study.StudyStatus.DRAFT;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @Test
    void create_under_score() {
        Study study = new Study(0);
        assertNotNull(study);

        System.out.println("create");

    }

    @Test
    @Disabled
    void create_under_score1() {
        System.out.println("create1");

    }

    @Test
    @DisplayName("스터디 만들기 🍕")
    void create_test_with_displayName() {
        Study study = new Study(1);
        assertNotNull(study);


        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(DRAFT, study.getStatus(), () -> "스터디를 초기 값은 " + DRAFT),
                () -> assertTrue(1 < 2),
                () -> assertTrue(study.getLimit() > 0, "스터디 참석 가능 인원은 최소 1명 이상")
        );
    }

    @Test
    @DisplayName("스터디 만들기 🍕 에러췍")
    void create_test_throw() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> new Study(-10));

        String message = e.getMessage();
        assertEquals(message,"limit 은 0이상이어야함");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("Before Each");
    }


}