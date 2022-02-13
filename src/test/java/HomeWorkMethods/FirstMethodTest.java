package HomeWorkMethods;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class FirstMethodTest {

    @Test
    public void getNewArrayAfterLastFourFromArrayFirstTest() {
        int[] intArray = {1, 2, 3, 4, 5, 4, 3, 2, 1};
        int[] result = {3, 2, 1};
        Assertions.assertArrayEquals(result, FirstMethod.getNewArrayAfterLastFourFromArray(intArray), "Test failed");
    }

    @Test
    public void getNewArrayAfterLastFourFromArraySecondTest() {
        int[] intArray = {5, 6, 7, 8, 9, 10};
        try {
            FirstMethod.getNewArrayAfterLastFourFromArray(intArray);
            Assertions.fail("Test failed");
        } catch (Exception ignored) {

        }
    }

    @Test
    public void getNewArrayAfterLastFourFromArrayThirdTest() {
        int[] intArray = {4};
        FirstMethod.getNewArrayAfterLastFourFromArray(intArray);
        Assertions.assertTrue(FirstMethod.getNewArrayAfterLastFourFromArray(intArray).length == 0);
    }
}
