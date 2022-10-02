package com.Encoder_Decoder;

import java.io.FileOutputStream;

public class NoiseHandler {
    public static String addNoiseHandler(String finalFilePath, byte[] compressed) {
        finalFilePath = finalFilePath.substring(0,finalFilePath.length()-4) + ".ecc";
        FileOutputStream fw = null;
        try {
            fw = new FileOutputStream(finalFilePath);
            fw.write(compressed);
            fw.close();
        } catch(Exception e) {System.out.println(e);}
        System.out.println("Done...");
        return finalFilePath;
    }

    public static byte[] removeNoiseHandler(byte[] bytes) {
        return bytes;
    }
}
