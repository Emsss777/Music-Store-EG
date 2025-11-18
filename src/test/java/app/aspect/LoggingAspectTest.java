package app.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import static app.util.LogMessages.AFTER_METHOD_EXEC;
import static app.util.LogMessages.ANOTHER_METHOD_EXEC;
import static app.util.LogMessages.BEFORE_METHOD_EXEC;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LoggingAspectTest {

    @Autowired
    private ImportantTestService importantTestService;

    @Autowired
    private UserControllerStub userController;

    private Logger aspectLogger;
    private ListAppender<ch.qos.logback.classic.spi.ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {

        aspectLogger = (Logger) LoggerFactory.getLogger(LoggingAspect.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        aspectLogger.addAppender(listAppender);
        aspectLogger.setLevel(Level.INFO);
    }

    @AfterEach
    void tearDown() {
        if (aspectLogger != null && listAppender != null) {
            aspectLogger.detachAppender(listAppender);
            listAppender.stop();
        }
    }

    @Test
    void aroundAdvice_logsBeforeAndAfter_forVeryImportantAnnotatedMethod() {

        String result = importantTestService.importantOperation();

        assertThat(result).isEqualTo("ok");
        String combinedLogs = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .reduce("", (a, b) -> a + "\n" + b);

        assertThat(combinedLogs).contains(BEFORE_METHOD_EXEC);
        assertThat(combinedLogs).contains(AFTER_METHOD_EXEC);
    }

    @Test
    void afterAdvice_logsForUserControllerBeanMethods() {

        userController.ping();

        String combinedLogs = listAppender.list.stream()
                .map(ILoggingEvent::getFormattedMessage)
                .reduce("", (a, b) -> a + "\n" + b);

        assertThat(combinedLogs).contains(ANOTHER_METHOD_EXEC);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        ImportantTestService importantTestService() {
            return new ImportantTestService();
        }

        @Bean(name = "userController")
        UserControllerStub userControllerStub() {
            return new UserControllerStub();
        }
    }

    static class ImportantTestService {

        @VeryImportant
        public String importantOperation() {
            return "ok";
        }
    }

    static class UserControllerStub {

        public void ping() {}
    }
}
