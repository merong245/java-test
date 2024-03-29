package me.hol22mol22.javatestlearn.study;

import me.hol22mol22.javatestlearn.FastTest;
import me.hol22mol22.javatestlearn.FindSlowTestExtension;
import me.hol22mol22.javatestlearn.SlowTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static me.hol22mol22.javatestlearn.study.StudyStatus.DRAFT;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

//@ExtendWith(FindSlowTestExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = StudyTest.ContainerPropertyInitializer.class)
class StudyTest {

    static Logger log = LoggerFactory.getLogger(StudyTest.class);

    // Junit의 전략은 테스트간 의존성을 줄이기 위해 테스트마다 다른 인스턴스로 생성되어 각 테스트에서 value의 변화를 주어도 1로 사용된다.
    int value = 1;

    @Value("${container.port}") int port;

    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)             // 포트 노출
            .withEnv("POSTGRES_DB", "studytest")       // 환경 변수 설정 ex) DB 이름
//            .waitingFor(Wait.forHttp("/hello")) // 특정 요청이 가능할 때 등 기다렸다 테스트 실행
            ;

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("container.port=" +postgreSQLContainer.getMappedPort(5432))
                    .applyTo(context.getEnvironment());
        }
    }

    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension(1000L);

    @BeforeAll
    static void beforeAll(){
        // 컨테이너 안의 로그 출력
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
        postgreSQLContainer.followOutput(logConsumer);
    }

    @Test
    @Order(1)
    void create_under_score() throws InterruptedException {
        Thread.sleep(1000L);
        Study study = new Study(0);
        System.out.println(value++);
        assertNotNull(study);

        System.out.println("create");

    }

    @Test
    @Disabled
    void create_under_score1() {
        System.out.println("create1");

    }

    @Order(2)
    @DisplayName("반복 테스트")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
    void repeat_test_10(RepetitionInfo repetitionInfo) {
        System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" +
                repetitionInfo.getTotalRepetitions());
    }

    @Order(3)
    @DisplayName("반복 테스트, 파라미터")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추웡"})
    void parameterized_test(String message) {
        System.out.println("message = " + message);
    }

    @Order(4)
    @DisplayName("반복 테스트, 정수")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 50})
    void parameterized_test_with_int(@ConvertWith(StudyConverter.class) Study study) {
        System.out.println("message = " + study.getLimit());
    }

    @DisplayName("반복 테스트, 다수 파라미터")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바스터디'", "20, '스프링"})
    void parameterized_test_with_csv(Integer limit, String name) {
        Study study = new Study(limit, name);
        System.out.println(study);
    }

    @DisplayName("반복 테스트, 다수 파라미터")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바스터디'", "20, '스프링"})
    void parameterized_test_with_csv_using_aggregator(ArgumentsAccessor argumentsAccessor) {
        Study study = new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        System.out.println(study);
    }

    @DisplayName("반복 테스트, 다수 파라미터 커스텀")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바스터디'", "20, '스프링"})
    void parameterized_test_with_csv_using_custom_aggregator(@AggregateWith(StudyAggregator.class) Study study) {
        System.out.println(study);
    }

    static class StudyAggregator implements ArgumentsAggregator {

        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }
    }

    static class StudyConverter extends SimpleArgumentConverter {

        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(Study.class, aClass, "Can Only Convert to Study");
            return new Study(Integer.parseInt(o.toString()));
        }
    }

    @FastTest
    @DisplayName("테스트 만들기 fast")
    void create_test_tag_fast() {
        System.out.println("im fast");
        System.out.println(value++);
    }

    @SlowTest
    @DisplayName("테스트 만들기 slow")
    void create_test_tag_slow() {
        System.out.println("im slow");
    }

    @Test
    @DisplayName("스터디 만들기 🍕")
    void create_test_with_displayName() {
        Study study = new Study(1);
        assertNotNull(study);
        System.out.println(value++);


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


    @BeforeEach
    void beforeEach() {
        System.out.println("Before Each");
        System.out.println(port);
    }


}