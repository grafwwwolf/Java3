package HomeWorkMethods;

import java.util.Arrays;

public class FirstMethod {

    public static int[] getNewArrayAfterLastFourFromArray(int[] array) throws RuntimeException {
        int lastFour = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 4) {
                lastFour = i;
            }
        }
        if (lastFour == -1) {
            throw new RuntimeException("В переданном массиве отсутствуют четверки");
        }
        if (lastFour == array.length - 1)  {
            return new int[0];
        }
        return Arrays.copyOfRange(array, lastFour + 1, array.length);
    }
}
