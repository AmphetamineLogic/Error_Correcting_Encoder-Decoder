package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String trebled = treble(input);
        String withErrors = makeErrors(trebled);
        String corrected = corrected(withErrors);

        System.out.println(input);
        System.out.println(trebled);
        System.out.println(withErrors);
        System.out.println(corrected);
    }

    private static String makeErrors (String input) {
        Random random = new Random();
        char[] chars = input.toCharArray();
        String alphabet = " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int positionToChange;
        char characterToInsert;
        for (int i = 0; i < chars.length; i += 3) {
            positionToChange = i + random.nextInt(3);
            while (positionToChange > chars.length) {
                positionToChange--;
            }

            characterToInsert = alphabet.charAt(random.nextInt(alphabet.length()));

            while (chars[positionToChange] == characterToInsert) {
                characterToInsert = alphabet.charAt(random.nextInt(alphabet.length()));
            }
            chars[positionToChange] = characterToInsert;
        }
        return new String(chars);
    }

    private static String treble (String input) {
        char[] chars = input.toCharArray();
        char[] result = new char[chars.length * 3];
        for (int i = 0; i < chars.length; i++) {
            result[i * 3] = chars[i];
            result[i * 3 + 1] = chars[i];
            result[i * 3 + 2] = chars[i];
        }
        return new String(result);
    }

    private static String corrected (String input) {
        char[] chars = input.toCharArray();
        char correct;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chars.length; i += 3) {
            if (chars[i] == chars[i + 1] || chars[i] == chars[i + 2]) {
                correct = chars[i];
            }
            else {
                correct = chars[i+1];
            }
            stringBuilder.append(correct);
        }
        return stringBuilder.toString();
    }
}
