package correcter;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String sendPath = "send.txt";
        String receivedPath = "received.txt";
        writeFile(makeErrors(readFile(sendPath)), receivedPath);
    }

    private static byte[] readFile (String fileName) {
        byte[] result = new byte[0];
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
                result = bufferedInputStream.readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void writeFile (byte[] bytesWithErrors, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytesWithErrors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] makeErrors (byte[] sourceBytes) {
        Random random = new Random();
        byte bitToChange;
        for (int i = 0; i < sourceBytes.length; i++) {
            bitToChange = (byte) random.nextInt(8);
            sourceBytes[i] ^= 1 << bitToChange;
        }
        return sourceBytes;
    }


}
