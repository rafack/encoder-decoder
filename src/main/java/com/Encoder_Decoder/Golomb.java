package com.Encoder_Decoder;

import com.Encoder_Decoder.Utils;

public class Golomb {
    private static final byte STOP_BIT = 1;
    private String content;
    private int k;

    public Golomb(String content, int k) {
        this.content = content;
        this.k = k;
        System.out.println(content);
    }

    public String encode(){
        String result = "";

        for (char character: content.toCharArray()) {
            int restOfDivision = character % this.k;
            int digitsToRepresentTheRest = calculateLog2(this.k);
            String restBinary = Utils.integerToStringBinary(restOfDivision, digitsToRepresentTheRest);

            int division = character / this.k;
            String zeros = new String(new char[division]).replace("\0", "0");
            String codewards = zeros + STOP_BIT + restBinary;

            result += codewards;
        }
        return result;
    }

    public String decode(){
        String result = "";
        boolean alreadyFoundStopBit = false;
        int digitsOnRest = calculateLog2(this.k);
        int quocient = 0;
        int index = 0;
        char[] contentCharArray = content.toCharArray();
        System.out.println(contentCharArray);

        while (contentCharArray[index] != 65535) {
            System.out.println(contentCharArray[index]);
            if (!alreadyFoundStopBit) {
                if ((contentCharArray[index] - '0') == STOP_BIT) {
                    alreadyFoundStopBit = true;
                } else {
                    quocient++;
                }
            } else {
                String restInBinary = "";
                restInBinary += contentCharArray[index];
                for (int i = 1; i < digitsOnRest; i++) {
                    ++index;
                    int nextChar = Integer.parseInt(Character.toString(contentCharArray[index]));
                    restInBinary += nextChar - '0';
                }
                int rest = Integer.parseInt(restInBinary, 2);
                result += (char) ((quocient * this.k) + rest);
                quocient = 0;
                alreadyFoundStopBit = false;
            }
            ++index;
        }
        return result;
    }
    private int calculateLog2(int value){
        return (int) (Math.log(value) / Math.log(2));
    }

}