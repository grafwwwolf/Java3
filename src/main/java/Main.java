import testClasses.FirstTestClass;
import testrunners.TestRunner;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {

        TestRunner.start(FirstTestClass.class);
        TestRunner.start("testClasses.FirstTestClass");
    }
}

