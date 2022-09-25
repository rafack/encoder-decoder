package com.Encoder_Decoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

public class Fibonacci {
    private byte[] content;
    private final int zeroValue = 235;

    public Fibonacci(byte[] content) {
        this.content = content;
    }

    public String encode(){
        ArrayList<Byte> resultBytes = new ArrayList<>();
        int totalBitsUsed = 0;

        for (byte b : content) {
            if (b == 0) {
                b = (byte)zeroValue;
            }

            int value = Byte.toUnsignedInt(b);
            int rest = value;
            ArrayList<Integer> fibonacciNumbers = getFibonacciNumbersByValue(value);
            fibonacciNumbers.sort(Collections.reverseOrder());
            // totalBytes = round up (fibonacciNumbersSize + stop bit / number of bits)
            totalBitsUsed += (fibonacciNumbers.size() + 1);
            int totalBytes = (int) Math.ceil(totalBitsUsed / 8.00);

            //add empty bytes
            while (resultBytes.size() < totalBytes) {
                resultBytes.add((byte) 0);
            }

            int bytePosition = resultBytes.size() - 1;
            byte resultByte = resultBytes.get(bytePosition);

            int bitPosition = 8 - totalBitsUsed % 8;

            if (bitPosition >= 8) {
                bitPosition = 8 - bitPosition;
            }

            //start with stop bit (1)
            resultByte = (byte) (resultByte | (1 << bitPosition));
            bitPosition++;

            int i = 0;
            for (Integer fibonacciNumber : fibonacciNumbers) {
                if (bitPosition >= 8) {
                    resultBytes.set(bytePosition, resultByte);
                    bytePosition--;
                    bitPosition = 0;
                    resultByte = resultBytes.get(Math.max(bytePosition, 0));
                }

                if (rest - fibonacciNumber >= 0) {
                    rest -= fibonacciNumber;
                    //resultByte add 1
                    resultByte = (byte) (resultByte | (1 << bitPosition));
                }

                bitPosition++;
                i++;
            }

            resultBytes.set(bytePosition, resultByte);
        }

        byte[] result = new byte[resultBytes.size() + 2];

        addHeaderValues(result);

        for (int i = 2; i < result.length; i++) {
            result[i] = resultBytes.get(i - 2);
        }

        String byteToString = new String(result, StandardCharsets.UTF_8);
        return byteToString;
    }

    public String decode(){
        ArrayList<Integer> decoded = new ArrayList<>();
        int currentNumber = 1;
        int nextNumber = 2;

        int decodeValue = 0;

        int bitPosition = 7;
        for (int bytePosition = 2; bytePosition < content.length; ) {
            boolean bit = (content[bytePosition] & (1 << bitPosition)) > 0;
            boolean nextBit = !(bitPosition - 1 < 0 && bytePosition + 1 > content.length - 1) && (content[bitPosition - 1 < 0 ? (bytePosition + 1) : bytePosition] & (1 << (bitPosition - 1 < 0 ? 7 : (bitPosition - 1)))) > 0;

            if (bit) {
                decodeValue += currentNumber;
            }

            int temp = currentNumber;
            currentNumber = nextNumber;
            nextNumber = temp + nextNumber;

            bitPosition--;

            if (bit && nextBit) {
                currentNumber = 1;
                nextNumber = 2;
                if (decodeValue == zeroValue) {
                    decodeValue = 0;
                }

                decoded.add(decodeValue);
                decodeValue = 0;
                //pass stop bit
                bitPosition--;
            }

            if (bitPosition < 0) {
                bitPosition = 7 + bitPosition + 1;
                bytePosition++;
            }
        }

        byte[] decodedBytes = new byte[decoded.size()];
        for (int i = 0; i < decodedBytes.length; i++) {
            int ascii = decoded.get(i);
            decodedBytes[i] = (byte)ascii;
        }

        String byteToString = new String(decodedBytes, StandardCharsets.UTF_8);
        return byteToString;
    }

    public ArrayList<Integer> getFibonacciNumbersByValue(int value) {
        ArrayList<Integer> fibonacciNumbers = new ArrayList<>();
        int currentNumber = 1;
        int nextNumber = 2;
        fibonacciNumbers.add(currentNumber);

        while (nextNumber <= value) {
            fibonacciNumbers.add(nextNumber);
            int temp = currentNumber;
            currentNumber = nextNumber;
            nextNumber = temp + nextNumber;
        }

        return fibonacciNumbers;
    }

    private void addHeaderValues(byte[] result) {
        result[0] = (byte) Algorithms.FIBONACCI.getIdentifier();
        result[1] = (byte) 0;
    }
}