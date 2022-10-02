package com.Encoder_Decoder;

import java.util.*;

public class Delta {
    private static final int QUANTITY_OF_DIGITS_SIZE = 5;
    private static final int FIRST_BINARY_SIZE = 8;
    private static final String CHANGED = "1";
    private static final String NO_CHANGES = "0";
    private static final char NEGATIVE = '1';
    private static final char POSITIVE = '0';
    private int biggestIncrease = 0;
    private int biggestDecrease = 0;
    private int quantOfDigits;
    private String content;

    public Delta(String content) {
        this.content = content;
    }

    public String encode() {
        String result = "";
        List<Integer> characters = new ArrayList<Integer>();
        char[] contentCharArray = content.toCharArray();
        int index = 0;

        while (index < contentCharArray.length) {
            int currentCharacter = contentCharArray[index];
            characters.add(currentCharacter);
            if(index+1 < contentCharArray.length){
                int nextCharacter = contentCharArray[index+1];
                updateBiggests(currentCharacter, nextCharacter);
            }
            ++index;
        }
        int currentCharacter;

        int biggest = Math.max(biggestDecrease, biggestIncrease);
        this.quantOfDigits = (int) Math.ceil(Utils.logBase2ToDouble(biggest));

        String quantityOfDigits = Utils.integerToStringBinary(this.quantOfDigits + 1, QUANTITY_OF_DIGITS_SIZE);
        result += quantityOfDigits;

        currentCharacter = characters.get(0);
        String firstNumberInBinary = Utils.integerToStringBinary(currentCharacter, FIRST_BINARY_SIZE);

        result += firstNumberInBinary;

        for (int i = 1; i < characters.size(); i++) {
            int nextCharacter = characters.get(i);

            int difference = Math.abs(currentCharacter - nextCharacter);
            String codeword = NO_CHANGES;
            if (difference != 0) {
                char signal = POSITIVE;
                if (currentCharacter > nextCharacter) {
                    signal = NEGATIVE;
                }
                codeword = CHANGED + signal + Utils.integerToStringBinary(difference - 1, this.quantOfDigits);
            }
            currentCharacter = nextCharacter;

            result += codeword;
        }
        return result;
    }
    public String decode(int zerosAdded) {
        String result = "";
        content = content.substring(0,content.length() - zerosAdded);
        char[] contentCharArray = content.toCharArray();

        String quantOfDigitsString = getCodeword(0, QUANTITY_OF_DIGITS_SIZE);
        this.quantOfDigits = Integer.parseInt(quantOfDigitsString, 2);

        String firstNumberString = getCodeword(QUANTITY_OF_DIGITS_SIZE, FIRST_BINARY_SIZE);

        char firstNumber = (char) Integer.parseInt(firstNumberString, 2);
        char lastCharacter = firstNumber;
        result += firstNumber;

        int index = QUANTITY_OF_DIGITS_SIZE + FIRST_BINARY_SIZE;

        while (index < contentCharArray.length) {
            if (contentCharArray[index] != '0') {
                String codeword = getCodeword(index+1, quantOfDigits);
                lastCharacter = discoverCharacter(codeword, lastCharacter);
            }
            result += lastCharacter;
            index += 3;
        }

        return result;
    }
    private void updateBiggests(int currentCharacter, int nextCharacter) {
        int difference = Math.abs(currentCharacter - nextCharacter);
        if (currentCharacter > nextCharacter) {
            if (difference > biggestDecrease) {
                biggestDecrease = difference;
            }
        } else if (currentCharacter < nextCharacter) {
            if (difference > biggestIncrease) {
                biggestIncrease = difference;
            }
        }
    }
    private char discoverCharacter(String codeword, char lastSimbol) {
        char signal = codeword.charAt(0);
        String restOfString = codeword.substring(1);
        int difference = Integer.parseInt(restOfString, 2) + 1;
        return signal == NEGATIVE ? (char) (lastSimbol - difference) : (char) (lastSimbol + difference);
    }
    private String getCodeword(int start, int quantity) {
        char[] contentCharArray = content.toCharArray();
        String codeword = "";
        for (int i = start; i < start+quantity; i++) {
            int nextChar = contentCharArray[i];
            codeword += nextChar - '0';
        }
        return codeword;
    }
}
