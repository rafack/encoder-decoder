package com.Encoder_Decoder;

public class Utils {
    public static String integerToStringBinary(int integer, int finalLengthOfBinary) {
        String binary = Integer.toBinaryString(integer);
        while (binary.length() != finalLengthOfBinary) {
            binary = "0" + binary;
        }
        return binary;
    }
}