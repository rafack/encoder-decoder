package com.Encoder_Decoder;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.*;
import java.text.*;

public class Writer {

    private String filePath;
    private String content;
    private String algorithm;
    private String k;
    private String finalContent;
    private String finalFilePath;

    public Writer(String filePath, String content, String algorithm, String k) {
        this.filePath = filePath;
        this.content = content;
        this.algorithm = algorithm;
        this.k = k;

        finalContent = "000000";
    }

    public String encode(){
        switch(this.algorithm){
            case "Golomb":
                //Header
                NumberFormat formatter = new DecimalFormat("00000000");
                this.finalContent += "01" + formatter.format(Integer.parseInt(this.k));
                //Golomb golomb = new Golomb(content, k, content[1]);
                //finalContent = golomb.encode();
                writeFileToDebug();
                return finalFilePath;
            case "Elias-Gamma":
                //EliasGamma eliasGamma = new EliasGamma(content);
                //finalContent = eliasGamma.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Fibonacci":
                //Fibonacci fibonacci = new Fibonacci(content);
               // finalContent = fibonacci.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Unária":
                //Unaria unaria = new Unaria(content);
                //finalContent = unaria.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Delta":
                //Delta delta = new Delta(content);
                //finalContent = delta.encode();
                writeFile(".cod");
                return finalFilePath;
        }
        return "";
    }

    public String decode(){
        switch(algorithm){
            case "Golomb":
                //Golomb golomb = new Golomb(content, k, content[1]);
                //finalContent = golomb.decode();
                writeFileToDebug();
                return finalFilePath;
            case "Elias-Gamma":
                //EliasGamma eliasGamma = new EliasGamma(content);
                //finalContent = eliasGamma.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Fibonacci":
                //Fibonacci fibonacci = new Fibonacci(content);
                //finalContent = fibonacci.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Unária":
                //Unaria unaria = new Unaria(content);
                //finalContent = unaria.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Delta":
                //Delta delta = new Delta(content);
                //finalContent = delta.decode();
                writeFile("New.txt");
                return finalFilePath;
        }
        return "";
    }
    private void writeFile(String end){
        finalFilePath = filePath.substring(0,filePath.length()-4) + end;
        try {
            FileWriter fw = new FileWriter(finalFilePath);
            fw.write(finalContent);
            fw.close();
        } catch(Exception e) {System.out.println(e);}
        System.out.println("Done...");
    }
    private void writeFileToDebug(){
        finalFilePath = filePath.substring(0,filePath.length()-4);
        try {
            FileWriter fw = new FileWriter(finalFilePath + "_debug.txt");
            fw.write(printBinary(finalContent, 8, " | "));
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
}