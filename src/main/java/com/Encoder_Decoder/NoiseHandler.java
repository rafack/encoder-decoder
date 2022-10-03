package com.Encoder_Decoder;

import java.io.FileOutputStream;

public class NoiseHandler {
    private static final int LENGTH_OF_BITS_IN_A_BYTE = 8;
    private static final int MOST_SIGNIFICANT_BIT = 49;
    private static final String CRC8 = "100000111";
    private static int headerSize = 0;

    public static String addNoiseHandler(String finalFilePath, byte[] compressed) {
        finalFilePath = finalFilePath.substring(0,finalFilePath.length()-4) + ".ecc";
        FileOutputStream fw;
        try {
            fw = new FileOutputStream(finalFilePath);
            fw.write(addNoiseHandler(compressed));
            fw.close();
        } catch(Exception e) {System.out.println(e);}
        System.out.println("Done...");
        return finalFilePath;
    }

    public static byte[] removeNoiseHandler(byte[] bytes) throws Exception {
        byte[] result = new byte[bytes.length - 1];
        byte[] header = new byte[2];
        header[0] = bytes[0];
        header[1] = bytes[1];
        if(bytes[2] != CRC8_coding(header)) {
            System.out.println("Header foi corrompido");
            throw new Exception("Header foi corrompido");
        }
        result[0] = bytes[0];
        result[1] = bytes[1];

        for(int i = 3; i < bytes.length; i++){
            result[i-1] = bytes[i];
        }

        byte[] temp = result;
        String result1 = "";

        for (int i = 0; i < temp.length; i++) {
            result1 += Utils.convertByteToBits(temp[i]);
        }
        System.out.println(result1);

        return result;
    }

    private static byte CRC8_coding(byte[] bytes) {
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
        temp = temp.substring(msb,temp.length());
        result += temp;
        return Utils.convertBitsToByte(result);
    }
    private static byte[] addNoiseHandler(byte[] bytes){
        byte[] result = new byte[bytes.length + 1];
        byte[] header = new byte[2];
        header[0] = bytes[0];
        header[1] = bytes[1];
        result[0] = bytes[0];
        result[1] = bytes[1];
        result[2] = CRC8_coding(header);

        for(int i = 2; i < bytes.length; i++){
            result[i+1] = bytes[i];
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
}
