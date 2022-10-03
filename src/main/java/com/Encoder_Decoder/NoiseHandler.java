package com.Encoder_Decoder;

import sun.nio.ch.Util;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class NoiseHandler {
    private static final int LENGTH_OF_BITS_IN_A_BYTE = 8;
    private static final int LENGTH_OF_BITS_IN_A_HAMMING_BYTE = 4;
    private static final int MOST_SIGNIFICANT_BIT = 49;
    private static final String CRC8 = "100000111";
    private static int headerSize = 0;
    private static String finalFilePathStored = "";
    private static Boolean debug;

    public static String addNoiseHandler(String finalFilePath, byte[] compressed, boolean isDebug) {
        finalFilePath = finalFilePath.substring(0,finalFilePath.length()-4) + ".ecc";
        debug = isDebug;
        finalFilePathStored = finalFilePath;
        FileOutputStream fw;
        try {
            System.out.println("Adicionando tratamento de ruido");
            fw = new FileOutputStream(finalFilePath);
            fw.write(addNoiseHandler(compressed));
            fw.close();
        } catch(Exception e) {System.out.println(e);}
        System.out.println("ECC Created...");
        return finalFilePath;
    }

    public static byte[] removeNoiseHandler(byte[] bytes) throws Exception {
        byte[] result = new byte[((bytes.length/2) - 1) + 2];
        byte[] header = new byte[2];
        header[0] = bytes[0];
        header[1] = bytes[1];
        if(bytes[2] != CRC8_coding(header)) {
            System.out.println("Header foi corrompido");
            throw new Exception("Header foi corrompido");
        }
        result[0] = bytes[0];
        result[1] = bytes[1];

        int increase = 0;
        for(int i = 2; i < result.length; i++){
            result[i] = removeHamming(bytes[increase+i+1],bytes[increase+i+2]);
            ++increase;
        }
        return result;
    }

    private static byte CRC8_coding(byte[] bytes) throws IOException {
        String result = Utils.convertByteToBits(bytes[0]);
        result += Utils.convertByteToBits(bytes[1]);
        headerSize = 2;
        result += Utils.integerToStringBinary(0,LENGTH_OF_BITS_IN_A_BYTE * headerSize);
        String temp = result;
        int msb = getIndexMostSignificantBit(result);
        while(isPossibleToAlignWithCRC8(msb , temp)) {
            String xor = xor(alignWithCRC8(msb, temp));
            temp = adjustString(msb, temp, xor);
            msb = getIndexMostSignificantBit(temp);
        }
        result = temp.substring(0,temp.length());

        return Utils.convertBitsToByte(result);
    }
    private static byte[] addNoiseHandler(byte[] bytes) throws IOException {
        byte[] result = new byte[(bytes.length * 2) - 1];
        byte[] header = new byte[2];
        header[0] = bytes[0];
        header[1] = bytes[1];
        result[0] = bytes[0];
        result[1] = bytes[1];
        result[2] = CRC8_coding(header);

        int increase = 0;
        for(int i = 2; i < bytes.length; i++){
            byte[] hamming = Hamming_coding(bytes[i]);
            result[(i+1+increase)] = hamming[0];
            result[(i+2+increase)] = hamming[1];
            ++increase;
        }

        if(debug){
            String resultString = "";
            byte[] temp = result;
            for (int i = 0; i < temp.length; i++) {
                resultString += Utils.convertByteToBits(temp[i]);
            }

            String finalFilePath = finalFilePathStored.substring(0,finalFilePathStored.length()-4) + "_debug_ecc.txt";
            FileWriter fw = new FileWriter(finalFilePath);
            fw.write(Handler.printBinary(resultString, " | "));
            fw.close();
        }
        return result;
    }
    private static String xor(String a){
        String ans = "";

        for (int i = 0; i < CRC8.length(); i++)
        {
            if (a.charAt(i) == CRC8.charAt(i)) ans += "0";
            else ans += "1";
        }
        return ans;
    }
    private static String xor(String a, String b){
        String ans = "";

        for (int i = 0; i < b.length(); i++)
        {
            if (a.charAt(i) == b.charAt(i)) ans += "0";
            else ans += "1";
        }
        return ans;
    }
    private static int getIndexMostSignificantBit(String bits){
        char[] characters = bits.toCharArray();
        for(int i = 0; i < characters.length; i++){
            if(characters[i] == ((char) MOST_SIGNIFICANT_BIT)) return i;
        }
        return 0;
    }
    private static String alignWithCRC8(int index, String bits){
        return bits.substring(index, index + CRC8.length());
    }
    private static Boolean isPossibleToAlignWithCRC8(int index, String bits){
        if(index + CRC8.length() > bits.length()) return false;
        return true;
    }
    private static String adjustString(int index, String bits, String xor){
        String result = bits.substring(0, index);
        result += xor;
        result += bits.substring(result.length(), bits.length());
        return result;
    }
    private static byte[] Hamming_coding(byte bits) {
        String result = Utils.convertByteToBits(bits);
        String byte1 = result.substring(0, LENGTH_OF_BITS_IN_A_HAMMING_BYTE);
        String byte2 = result.substring(LENGTH_OF_BITS_IN_A_HAMMING_BYTE, LENGTH_OF_BITS_IN_A_BYTE);
        byte1 = xorByte(byte1);
        byte2 = xorByte(byte2);

        byte[] bytes = new byte[2];
        bytes[0] = Utils.convertBitsToByte(byte1);
        bytes[1] = Utils.convertBitsToByte(byte2);
        return bytes;
    }
    private static String xorByte(String bits) {
        String x3 = String.valueOf(bits.charAt(0));
        String x2 = String.valueOf(bits.charAt(1));
        String x1 = String.valueOf(bits.charAt(2));
        String x0 = String.valueOf(bits.charAt(3));
        String p1 = xorBits(x3,x2,x0);
        String p2 = xorBits(x3,x1,x0);
        String p4 = xorBits(x2,x1,x0);
        return 0 + p1 + p2 + x3 + p4 + x2 + x1 + x0;
    }
    private static String xorBits(String bit0, String bit1, String bit2) {
        String result = "";
        result = xor(bit0,bit1);
        return xor(result,bit2);
    }
    private static String xorBits(String bit0, String bit1, String bit2, String bit3) {
        String result = "";
        result = xor(bit0,bit1);
        result = xor(result,bit2);
        return  xor(result,bit3);
    }
    private static byte removeHamming(byte byte1, byte byte2) {
        String result = getBits(Utils.convertByteToBits(byte1));
        result += getBits(Utils.convertByteToBits(byte2));
        return Utils.convertBitsToByte(result);
    }
    private static String getBits(String bits) {
        String p1 = String.valueOf(bits.charAt(1));
        String p2 = String.valueOf(bits.charAt(2));
        String x3 = String.valueOf(bits.charAt(3));
        String p4 = String.valueOf(bits.charAt(4));
        String x2 = String.valueOf(bits.charAt(5));
        String x1 = String.valueOf(bits.charAt(6));
        String x0 = String.valueOf(bits.charAt(7));

        String check1 = xorBits(p1,x3,x2,x0);
        String check2 = xorBits(p2,x3,x1,x0);
        String check3 = xorBits(p4,x2,x1,x0);

        while(!check1.equals("0") || !check2.equals("0") || !check3.equals("0")){
            String bit = check3 + check2 + check1;
            System.out.println("Bit " + bit + " foi flipado");
            switch (Integer.parseInt(bit, 2)){
                case 1:
                    p1 = bitFlip(p1);
                case 2:
                    p2 = bitFlip(p2);
                case 3:
                    x3 = bitFlip(x3);
                case 4:
                    p4 = bitFlip(p4);
                case 5:
                    x2 = bitFlip(x2);
                case 6:
                    x1 = bitFlip(x1);
                case 7:
                    x0 = bitFlip(x0);
            }
            check1 = xorBits(p1,x3,x2,x0);
            check2 = xorBits(p2,x3,x1,x0);
            check3 = xorBits(p4,x2,x1,x0);

        }
        return x3 + x2 + x1 + x0;
    }
    private static String bitFlip (String bit){
        if(bit.equals("0")) return "1";
        else return "0";
    }
}
