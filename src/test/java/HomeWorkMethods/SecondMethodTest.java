package HomeWorkMethods;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SecondMethodTest {

    @Test
    public void checkOnesAndFoursFirstTest() {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8};
        Assertions.assertTrue(SecondMethod.checkOnesAndFours(array));
    }

    @Test
    public void checkOnesAndFoursSecondTest() {
        int[] array = {1, 1, 1, 1};
        Assertions.assertFalse(SecondMethod.checkOnesAndFours(array));
    }

    @Test
    public void checkOnesAndFoursThirdTest() {
        int[] array = {4};
        Assertions.assertFalse(SecondMethod.checkOnesAndFours(array));
    }

    @Test
    public void checkOnesAndFoursFourthTest() {
        int[] array = {10, 2, 3, 14, 5, 6, 7, 8};
        Assertions.assertFalse(SecondMethod.checkOnesAndFours(array));
    }
}
