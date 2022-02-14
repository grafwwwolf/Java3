package testClasses;

import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

public class FirstTestClass {

    @BeforeSuite
    public void before() {
        System.out.println("Metgods start");
    }

    @AfterSuite
    public void after() {
        System.out.println("Metgods finish");
    }

    @Test(priority = 1)
    public void firstMethod() {
        System.out.println("firstMethod");
    }

    @Test(priority = 2)
    public void secondMethod() {
        System.out.println("secondMethod");
    }

    @Test
    public void FifthMethod() {
        System.out.println("FifthMethod");
    }

    @Test(priority = 3)
    public void thirdMethod() {
        System.out.println("thirdMethod");
    }

    @Test(priority = 6)
    public void sixthMethod() {
        System.out.println("sixthMethod");
    }

    @Test(priority = 4)
    public void fourthMethod() {
        System.out.println("fourthMethod");
    }
}
