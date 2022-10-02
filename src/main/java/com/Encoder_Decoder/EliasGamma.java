package com.Encoder_Decoder;

import java.util.*;
import com.Encoder_Decoder.Utils;

public class EliasGamma {
    private static final byte STOP_BIT = 1;
    private String content;

    public EliasGamma(String content) {
        this.content = content;
    }

    public String encode(){
        String result = "";

        for (char character: content.toCharArray()) {
            if (character == 1) {
                result += STOP_BIT;
            } else {
                int unaryNumber = Utils.calculateLog2(character);
                String unaryString = Utils.createStreamOnZeros(unaryNumber);
                int rest = (int) (character - (Math.pow(2, unaryNumber)));

                String restInBinary = Utils.integerToStringBinary(rest, unaryNumber);

                String codewards = unaryString + STOP_BIT + restInBinary;
                result += codewards;
            }
        }
        return result;
    }

    public String decode(){
        String result = "";
        boolean alreadyFoundStopBit = false;
        int prefixLength = 0;
        int index = 0;
        char[] contentCharArray = content.toCharArray();

        while (index < contentCharArray.length) {
            if (!alreadyFoundStopBit) {
                if ((contentCharArray[index] - '0') == STOP_BIT) {
                    alreadyFoundStopBit = true;
                } else {
                    prefixLength++;
                }
            } else {
                String restInBinary = "";
                restInBinary += contentCharArray[index];
                for (int i = 1; i < prefixLength; i++) {
                    ++index;
                    int nextChar = Integer.parseInt(Character.toString(contentCharArray[index]));
                    restInBinary += nextChar;
                }

                int rest = Integer.parseInt(restInBinary, 2);
                char finalNumber = (char) ((int) Math.pow(2, prefixLength) + rest);
                result += finalNumber;
                prefixLength = 0;
                alreadyFoundStopBit = false;
            }
            ++index;
        }
        return result;
    }
}