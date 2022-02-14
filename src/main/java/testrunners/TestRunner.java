package testrunners;

import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    public static void start(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class clazz = Class.forName(className);
        start(clazz);

    }

    public static void start(Class clazz) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object classObj = clazz.newInstance();
        Method[] methods = clazz.getDeclaredMethods();
        Method beforeMethods = null;
        Method afterMethods = null;
        List<Method> methodList = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeMethods == null) {
                    beforeMethods = method;
                } else {
                    throw new RuntimeException("Методов с аннотацией BeforeSuite больше одного");
                }
            } else if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterMethods == null) {
                    afterMethods = method;
                } else {
                    throw new RuntimeException("Методов с аннотацией AfterSuite больше одного");
                }
            } else {
                methodList.add(method);
            }
        }
        Collections.sort(methodList, Comparator.comparingInt(o -> o.getAnnotation(Test.class).priority()));
        if (beforeMethods != null) {
            beforeMethods.invoke(classObj);
        }
        for (Method method : methodList) {
            method.invoke(classObj);
        }
        if (afterMethods != null) {
            afterMethods.invoke(classObj);
        }
    }
}
