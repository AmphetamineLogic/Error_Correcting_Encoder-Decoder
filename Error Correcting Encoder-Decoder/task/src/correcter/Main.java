package correcter;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(makeErrors(input));
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
}
