package shareit.utils;

import net.bytebuddy.utility.RandomString;

public class DataGenerator {

    public static String generateEmail() {
        return generateRandomString(5) + "@" + generateRandomString(4);
    }

    public static String generateRandomString(int length) {
        return RandomString.make(length);
    }
}
