package HomeWorkMethods;


public class SecondMethod {

    public static boolean checkOnesAndFours(int[] array) {
        int countOfOnes = 0;
        int countOfFours = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) {
                countOfOnes++;
            }
            if (array[i] == 4) {
                countOfFours++;
            }
        }
        return ((countOfOnes) > 0 && (countOfFours > 0));
    }
}
