package com.Encoder_Decoder;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.*;
import java.text.*;
import java.nio.charset.StandardCharsets;

public class Writer {

    private String filePath;
    private String content;
    private String algorithm;
    private String k;
    private byte[] finalContent;
    private String finalFilePath;
    private byte[] header = new byte[2];

    public Writer(String filePath, String content, String algorithm, String k) {
        this.filePath = filePath;
        this.content = content;
        this.algorithm = algorithm;
        this.k = k;
    }

    public String encode(){
        switch(this.algorithm){
            case "Golomb":
                //Header
                header[0] = Byte.valueOf("00");
                header[1] = Byte.valueOf(k);
                //Golomb golomb = new Golomb(content, k, content[1]);
                //finalContent = golomb.encode();
                finalContent = content.getBytes(StandardCharsets.US_ASCII);
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return finalFilePath;
            case "Elias-Gamma":
                //EliasGamma eliasGamma = new EliasGamma(content);
                //finalContent = eliasGamma.encode();
                writeFile(".cod", true);
                return finalFilePath;
            case "Fibonacci":
                //Fibonacci fibonacci = new Fibonacci(content);
               // finalContent = fibonacci.encode();
                writeFile(".cod", true);
                return finalFilePath;
            case "Unária":
                //Unaria unaria = new Unaria(content);
                //finalContent = unaria.encode();
                writeFile(".cod", true);
                return finalFilePath;
            case "Delta":
                //Delta delta = new Delta(content);
                //finalContent = delta.encode();
                writeFile(".cod", true);
                return finalFilePath;
        }
        return "";
    }

    public String decode(){
        findAlgorithm();
        switch(algorithm){
            case "Golomb":
                //Golomb golomb = new Golomb(content, k, content[1]);
                //finalContent = golomb.decode();
                finalContent = content.getBytes(StandardCharsets.US_ASCII);
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Elias-Gamma":
                //EliasGamma eliasGamma = new EliasGamma(content);
                //finalContent = eliasGamma.decode();
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Fibonacci":
                //Fibonacci fibonacci = new Fibonacci(content);
                //finalContent = fibonacci.decode();
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Unária":
                //Unaria unaria = new Unaria(content);
                //finalContent = unaria.decode();
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Delta":
                //Delta delta = new Delta(content);
                //finalContent = delta.decode();
                writeFile("Decoded.txt", false);
                return finalFilePath;
        }
        return "";
    }
    private void writeFile(String end, Boolean hasHeader){
        finalFilePath = filePath.substring(0,filePath.length()-4) + end;
        String result = "";
        if(hasHeader) result += new String(header, StandardCharsets.UTF_8);
        result += new String(finalContent, StandardCharsets.US_ASCII);
        try {
            FileWriter fw = new FileWriter(finalFilePath);
            if(end == "_debug.txt") fw.write(printBinary(result, 8, " | "));
            else fw.write(result);
            fw.close();
        } catch(Exception e) {System.out.println(e);}
        System.out.println("Done...");
    }
    private static String printBinary(String binary, int blockSize, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }

        return result.stream().collect(Collectors.joining(separator));
    }
    private void findAlgorithm(){
        System.out.println("findAlgorithm");
        byte[] temp = content.getBytes();
        header[0] = temp[0];
        header[1] = temp[1];

        if(header[0] == Byte.valueOf("00")) this.algorithm = "Golomb";
        if(header[0] == Byte.valueOf("01")) this.algorithm = "Elias-Gamma";
        if(header[0] == Byte.valueOf("10")) this.algorithm = "Fibonacci";
        if(header[0] == Byte.valueOf("11")) this.algorithm = "Unária";
        if(header[0] == Byte.valueOf("100")) this.algorithm = "Delta";

        temp = ("" + header[1]).getBytes();

        this.k = new String(temp, StandardCharsets.UTF_8);

        content = content.substring(header.length,content.length());
    }
}