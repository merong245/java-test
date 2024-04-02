package me.hol22mol22.javatestlearn.study;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
@ContextConfiguration(initializers = StudyServiceTest.ContainerPropertyInitializer.class)
class StudyServiceTest {

    @Mock StudyService studyService;

    @Value("${container.port}") int port;

    @Container
    static DockerComposeContainer composeContainer =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("study-db",5432);
            ;

    @Test
    void doDummyTest(){
        System.out.println("========");
        System.out.println(port);
        //given
        String expect = "BDD Test";
        given(studyService.dummyTest()).willReturn(expect);

        //when
        String answer = studyService.dummyTest();

        //then
        assertEquals(expect,answer);
    }

    static class ContainerPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of("container.port=" +composeContainer.getServicePort("study-db",5432))
                    .applyTo(context.getEnvironment());
        }
    }
}