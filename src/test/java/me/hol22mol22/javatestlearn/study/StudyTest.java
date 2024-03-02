package me.hol22mol22.javatestlearn.study;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static me.hol22mol22.javatestlearn.study.StudyStatus.DRAFT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
        assertEquals(message, "limit 은 0이상이어야함");
    }

    @Test
    @DisplayName("스터디 만들기 🍕 타임아웃 췍")
    void create_test_timeout() {
        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(10);
        });
    }

    @Test
    @DisplayName("스터디 만들기 🍕 타임아웃 바로 췍")
    void create_test_timeout_preemtively() {
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(10);
        });
    }

    @Test
    @DisplayName("조건에 따라 테스트 실행")
    @EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.MAC})
    void create_test_with_assume() {
        System.out.println("im here");
    }
    @Test
    @DisplayName("조건에 따라 테스트 실행 - 특정 시스템 환경에서")
    void create_test_with_assume_on_testenv() {
        assumeTrue("LOCAL".equals(System.getenv("TEST_ENV")));
        System.out.println(System.getenv("TEST_ENV"));
    }

    @Test
    @DisplayName("조건에 따라 테스트 실행 - 특정 시스템 환경에서")
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL  ")
    void create_test_with_assume_on_testenv_anno() {
    }

    @Test
    @DisplayName("조건에 따라 테스트 실행 - 맥에서만")
    @EnabledOnOs({OS.MAC})
    void create_test_with_assume_onlyMac() {
        System.out.println("MAC");
    }

    @Test
    @DisplayName("조건에 따라 테스트 실행 - 자바 8 이상 에서만")
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_11, JRE.JAVA_17})
    void create_test_with_assume_over_jdk8() {
        System.out.println("JDK over 8");
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