import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger beautiful3 = new AtomicInteger(0);
    public static AtomicInteger beautiful4 = new AtomicInteger(0);
    public static AtomicInteger beautiful5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        String[] texts = generateNicknames(100_000, "abc", 3, 6);

        Thread palindromes = new Thread(
                () -> {
                    for (String text : texts) {
                        if (isPalindrome(text) && !isSingleLetterRepeated(text)) {
                            increment(text);
                        }
                    }
                }
        );
        palindromes.start();

        Thread singles = new Thread(
                () -> {
                    for (String text : texts) {
                        if (isSingleLetterRepeated(text)) {
                            increment(text);
                        }
                    }
                }
        );
        singles.start();

        Thread ascendings = new Thread(
                () -> {
                    for (String text : texts) {
                        if (isAscending(text)) {
                            increment(text);
                        }
                    }
                }
        );
        ascendings.start();

        palindromes.join();
        singles.join();
        ascendings.join();

        System.out.println("Красивых слов с длиной 3: "+beautiful3+" шт");
        System.out.println("Красивых слов с длиной 4: "+beautiful4+" шт");
        System.out.println("Красивых слов с длиной 5: "+beautiful5+" шт");
    }

    public static boolean isPalindrome(String text) {
        int l = 0;
        int r = text.length() - 1;
        while (l < r) {
            if (text.charAt(l) != text.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }

    public static boolean isSingleLetterRepeated(String text){
        char c = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != c) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAscending(String text) {
        char last = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current < last) {
                return false;
            }
            last = current;
        }
        return true;
    }

    public static void increment(String text) {
        switch (text.length()) {
            case 3 -> beautiful3.getAndIncrement();
            case 4 -> beautiful4.getAndIncrement();
            case 5 -> beautiful5.getAndIncrement();
        }
    }

    public static String[] generateNicknames(int count, String letters, int minLength, int maxLength) {
        String[] result = new String[count];
        for (int i = 0; i < result.length; i++) {
            result[i] = generateText(letters, minLength + new Random().nextInt(maxLength - minLength));
        }
        return result;
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(new Random().nextInt(letters.length())));
        }
        return text.toString();
    }
}