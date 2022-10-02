package com.Encoder_Decoder;

import com.Encoder_Decoder.Utils;

public class Unaria {
    private String content;

    public Unaria(String content) {
        this.content = content;
    }

    public String encode(){
        String result = "";
        int bit = 0;
        for (char character: content.toCharArray()) {
            String codeword = "";
            if (bit == 0) {
                codeword = Utils.createStreamOnZeros(character);
                bit = 1;
            } else {
                codeword = Utils.createStreamOnOnes(character);
                bit = 0;
            }
            result += codeword;
        }
        return result;
    }

    public String decode(int zerosAdded) {
        String result = "";
        int counter = 1;
        int index = 0;
        char[] contentCharArray = content.toCharArray();
        int last = contentCharArray[index];
        ++index;

        while (index < contentCharArray.length) {
            if (contentCharArray[index] != last) {
                char character = (char) counter;
                result += character;
                last = contentCharArray[index];
                counter = 1;
            } else {
                counter++;
            }
            ++index;
        }
        char character = (char) (counter - zerosAdded);
        result += character;
        return result;
    }
}