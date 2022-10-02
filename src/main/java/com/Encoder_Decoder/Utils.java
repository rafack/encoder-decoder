package com.Encoder_Decoder;

public class Utils {
    public static String integerToStringBinary(int integer, int finalLengthOfBinary) {
        String binary = Integer.toBinaryString(integer);
        while (binary.length() != finalLengthOfBinary) {
            binary = "0" + binary;
        }
        return binary;
    }
    public static int calculateLog2(int value){
        return (int) (Math.log(value) / Math.log(2));
    }
    public static String createStreamOnZeros(int howManyZeros) {
        return new String(new char[howManyZeros]).replace("\0", "0");
    }
    public static String createStreamOnOnes(int howManyOnes) {
        return new String(new char[howManyOnes]).replace("\0", "1");
    }
    public static double logBase2ToDouble(int logaritmando) {
        return logaritmando == 1 ? 1 : (Math.log10(logaritmando) / Math.log10(2));
    }
}