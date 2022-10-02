package com.Encoder_Decoder;

import com.Encoder_Decoder.Utils;

public class Fibonacci {
    private String content;
    private int[] fibonacci_sequence;

    public Fibonacci(String content) {
        this.content = content;
        this.fibonacci_sequence = this.createStructureFibonacci();
    }
    public String encode(){
        String result = "";

        for (char character: content.toCharArray()) {
            int n = character;
            String binary = "1";
            boolean isFirstOccurrence = false;
            int necessaryLength = 0;

            for (int i = this.fibonacci_sequence.length - 1; i >= 0; i--) {
                if (n == 0) break;

                if (this.fibonacci_sequence[i] <= n) {
                    if (necessaryLength == 0) {
                        necessaryLength = i + 1;
                    }
                    binary = "1" + binary;
                    n = n - this.fibonacci_sequence[i];
                    isFirstOccurrence = true;
                } else if (isFirstOccurrence) {
                    binary = "0" + binary;
                }
            }

            for (int i = necessaryLength - (binary.length() - 1); i > 0; i--) {
                binary = "0" + binary;
            }
            result += binary;
        }
        return result;
    }
    public String decode(){
        String result = "";
        String storedOccurrence = "";
        char numberOne = Integer.toString(1).charAt(0);
        int index = 0;
        char[] contentCharArray = content.toCharArray();

        while (index < contentCharArray.length) {
            char lastChar = storedOccurrence.length() > 0 ? storedOccurrence.charAt(storedOccurrence.length() - 1) : 0;
            if (lastChar == numberOne && contentCharArray[index] == numberOne) {
                int ascii = this.decodeStringFibonacci(storedOccurrence);
                char teste = (char) ascii;
                result += teste;
                storedOccurrence = "";
            } else {
                storedOccurrence = storedOccurrence + contentCharArray[index];
            }
            ++index;
        }
        return result;
    }
    private int[] createStructureFibonacci() {
        int[] structure = new int[12];
        structure[0] = 1;
        structure[1] = 2;
        for (int i = 2; i < structure.length; i++) {
            structure[i] = structure[i - 1] + structure[i - 2];
        }
        return structure;
    }
    private int decodeStringFibonacci(String codeword) {
        char numberOne = Integer.toString(1).charAt(0);
        int total = 0;

        for (int i = 0; i < this.fibonacci_sequence.length && i < codeword.length(); i++) {
            if (codeword.charAt(i) == numberOne) {
                total += this.fibonacci_sequence[i];
            }
        }

        return total;
    }
}