package app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.stereotype.Component;

import static app.util.SuccessMessages.*;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @After("bean(userController)")
    public void logIndexControllerMethods() {

        System.out.println(AnsiOutput.toString(AnsiColor.BRIGHT_GREEN, ANOTHER_METHOD_EXEC));
    }

    @Around(value = "@annotation(app.aspect.VeryImportant)")
    public Object logVeryImportantMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        System.out.println(AnsiOutput.toString(AnsiColor.BRIGHT_BLUE, BEFORE_METHOD_EXEC));
        Object methodResult = proceedingJoinPoint.proceed();
        System.out.println(AnsiOutput.toString(AnsiColor.BRIGHT_WHITE, AFTER_METHOD_EXEC));

        return methodResult;
    }
}