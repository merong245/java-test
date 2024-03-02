package me.hol22mol22.javatestlearn;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;

public class FindSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private static final long THRESHOLD = 1000L;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        store.put("START_TIME", System.currentTimeMillis());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);
        Method requiredTestMethod = context.getRequiredTestMethod();
        SlowTest annotation = requiredTestMethod.getAnnotation(SlowTest.class);


        String testMethodName = context.getRequiredTestMethod().getName();

        long startTime = store.remove("START_TIME", long.class);
        long duration = System.currentTimeMillis() - startTime;


        // 어노테이션이 달려있지 않고!
        if (duration > THRESHOLD && annotation == null) {
            System.out.println("plz consider @SlowTest = " + testMethodName);
        }
    }

    private static ExtensionContext.Store getStore(ExtensionContext context) {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(testClassName, testMethodName));
        return store;
    }


}
