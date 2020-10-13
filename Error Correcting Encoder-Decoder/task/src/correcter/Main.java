package correcter;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String sendPath = "send.txt";           // source file
        String encodedPath = "encoded.txt";     // file encoded with parity bits
        String receivedPath = "received.txt";   // file with simulated errors (1 per byte)
        String decodedPath = "decoded.txt";     // decoded file with corrected errors

        Scanner scanner = new Scanner(System.in);
//        System.out.println("Write a mode: ");
        switch (scanner.nextLine()) {
            case "encode":
                writeFile(convertBinaryStringToByteArray(encodeHamming(readFile(sendPath))), encodedPath);
//                writeFile(convertBinaryStringToByteArray(encodeParityBits(readFile(sendPath))), encodedPath);
                break;
            case "send":
                writeFile(makeErrors(readFile(encodedPath)), receivedPath);
                break;
            case "decode":
                writeFile(convertBinaryStringToByteArray(decodeParityBits(readFile(receivedPath))), decodedPath);
                break;
            case "test":
                writeFile(convertBinaryStringToByteArray(encodeParityBits(readFile(sendPath))), encodedPath);
                writeFile(makeErrors(readFile(encodedPath)), receivedPath);
                writeFile(convertBinaryStringToByteArray(decodeParityBits(readFile(receivedPath))), decodedPath);
                break;
        }
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

    private static void writeFile (byte[] bytesArray, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytesArray);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] convertBinaryStringToByteArray (String binaryString) {
        byte[] result = new byte[binaryString.length() / 8];

        String currentBits;
        for (int i = 0; i < result.length; i++) {
            currentBits = binaryString.substring(i * 8, i * 8 + 8);
            Integer byteAsInt = Integer.parseInt(currentBits, 2);
            result[i] = byteAsInt.byteValue();
        }
        return result;
    }

    private static String convertByteArrayToBinaryString (byte[] sourceBytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String currentByteAsBinaryString;
        for (byte b: sourceBytes
        ) {
            currentByteAsBinaryString = Integer.toBinaryString((b & 0xFF) + 0x100).substring(1);
            stringBuilder.append(currentByteAsBinaryString);
        }
        return stringBuilder.toString();
    }

    private static byte[] makeErrors (byte[] sourceBytes) {
        Random random = new Random();
        byte bitToChange;
        for (int i = 0; i < sourceBytes.length; i++) {
            bitToChange = (byte) random.nextInt(8);
            sourceBytes[i] ^= 1 << bitToChange;
        }
        System.out.println("Received:  " + convertByteArrayToBinaryString(sourceBytes));
        return sourceBytes;
    }

    private static String encodeParityBits(byte[] sourceBytes) {
        String byteArrayAsBinaryString = convertByteArrayToBinaryString(sourceBytes);
        System.out.println("Source:    " + byteArrayAsBinaryString);

        StringBuilder stringBuilder = new StringBuilder();

        boolean bit1;
        boolean bit2;
        boolean bit3;

        int lastProcessedBitPosition = 0;

        for (int i = 0; i <= byteArrayAsBinaryString.length() - 3; i+=3) {
            bit1 = byteArrayAsBinaryString.charAt(i) != '0';
            bit2 = byteArrayAsBinaryString.charAt(i + 1) != '0';
            bit3 = byteArrayAsBinaryString.charAt(i + 2) != '0';
            stringBuilder.append(bit1 ? '1' : '0');
            stringBuilder.append(bit1 ? '1' : '0');
            stringBuilder.append(bit2 ? '1' : '0');
            stringBuilder.append(bit2 ? '1' : '0');
            stringBuilder.append(bit3 ? '1' : '0');
            stringBuilder.append(bit3 ? '1' : '0');
            stringBuilder.append(bit1 ^ bit2 ^ bit3 ? '1' : '0');
            stringBuilder.append(bit1 ^ bit2 ^ bit3 ? '1' : '0');
            lastProcessedBitPosition = i + 2;
        }

        if (byteArrayAsBinaryString.length() - 1 - lastProcessedBitPosition == 2) {
            bit1 = byteArrayAsBinaryString.charAt(lastProcessedBitPosition + 1) != '0';
            bit2 = byteArrayAsBinaryString.charAt(lastProcessedBitPosition + 2) != '0';
            stringBuilder.append(bit1 ? '1' : '0');
            stringBuilder.append(bit1 ? '1' : '0');
            stringBuilder.append(bit2 ? '1' : '0');
            stringBuilder.append(bit2 ? '1' : '0');
            stringBuilder.append('0');
            stringBuilder.append('0');
            stringBuilder.append(bit1 ^ bit2 ^ false ? '1' : '0');
            stringBuilder.append(bit1 ^ bit2 ^ false ? '1' : '0');

        }
        if (byteArrayAsBinaryString.length() - 1 - lastProcessedBitPosition == 1) {
            bit1 = byteArrayAsBinaryString.charAt(lastProcessedBitPosition + 1) != '0';
            stringBuilder.append(bit1 ? '1' : '0');
            stringBuilder.append(bit1 ? '1' : '0');
            stringBuilder.append('0');
            stringBuilder.append('0');
            stringBuilder.append('0');
            stringBuilder.append('0');
            stringBuilder.append(bit1 ^ false ^ false ? '1' : '0');
            stringBuilder.append(bit1 ^ false ^ false ? '1' : '0');

        }
        System.out.println("Encoded:   " + stringBuilder);
        return stringBuilder.toString();
    }

    private static String encodeHamming(byte[] sourceBytes) {
        String byteArrayAsBinaryString = convertByteArrayToBinaryString(sourceBytes);
        System.out.println("Source:    " + byteArrayAsBinaryString);

        boolean resultingBits[] = new boolean[8];
        StringBuilder stringBuilder = new StringBuilder();

        String current4bitsAsString;
        for (int i = 0; i < byteArrayAsBinaryString.length() / 4; i++) {
            current4bitsAsString = byteArrayAsBinaryString.substring(i * 4, i * 4 + 4);
            resultingBits[2] = current4bitsAsString.charAt(0) != '0';
            resultingBits[4] = current4bitsAsString.charAt(1) != '0';
            resultingBits[5] = current4bitsAsString.charAt(2) != '0';
            resultingBits[6] = current4bitsAsString.charAt(3) != '0';
            resultingBits[0] = resultingBits[2] ^ resultingBits[4] ^ resultingBits[6];
            resultingBits[1] = resultingBits[2] ^ resultingBits[5] ^ resultingBits[6];
            resultingBits[3] = resultingBits[4] ^ resultingBits[5] ^ resultingBits[6];
            resultingBits[7] = false;
//            System.out.println(Arrays.toString(resultingBits));
            for (int j = 0; j < 8; j++) {
                stringBuilder.append(resultingBits[j] == false ? '0' : '1');
            }
        }
        System.out.println("Encoded:   " + stringBuilder);
        return stringBuilder.toString();
    }

    private static String decodeParityBits(byte[] sourceBytes) {
        String byteArrayAsBinaryString = convertByteArrayToBinaryString(sourceBytes);
        boolean[] bits = new boolean[8];
        String currentByteAsString;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < byteArrayAsBinaryString.length() / 8; i++) {
            currentByteAsString = byteArrayAsBinaryString.substring(i * 8, i * 8 + 8);
            for (int j = 0; j < 8; j++) {
                bits[j] = currentByteAsString.charAt(j) != '0';
            }
            stringBuilder.append(correctByteParityBits(bits));
        }

        String result = stringBuilder.toString().substring(0, stringBuilder.toString().length() / 8 * 8);
        System.out.println("Corrected: " + result);
        return result;
    }

    private static String correctByteParityBits(boolean[] bits) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i+=2) {
            if (bits[i] == bits[i+1]) {
                stringBuilder.append(bits[i] ? '1' : '0');
            }
            else {
                // identifying the correct value using the parity bit
                if (bits[(i + 2) % 6] ^ bits[(i + 4) % 6] ^ true == bits[7]) {
                    stringBuilder.append('1');
                }
                else {
                    stringBuilder.append('0');
                }
            }
        }
        return stringBuilder.toString();
    }

}
