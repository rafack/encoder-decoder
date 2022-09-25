package com.Encoder_Decoder;

import java.io.FileWriter;

public class Writer {

    private String filePath;
    private byte[] content;
    private String algorithm;
    private String k;
    private String finalContent;

    private String finalFilePath;

    public Writer(String filePath, byte[] content, String algorithm, String k) {
        this.filePath = filePath;
        this.content = content;
        this.algorithm = algorithm;
        this.k = k;
    }

    public String encode(){
        switch(algorithm){
            case "Golomb":
                Golomb golomb = new Golomb(content, k, content[1]);
                finalContent = golomb.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Elias-Gamma":
                EliasGamma eliasGamma = new EliasGamma(content);
                finalContent = eliasGamma.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Fibonacci":
                Fibonacci fibonacci = new Fibonacci(content);
                finalContent = fibonacci.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Unária":
                Unaria unaria = new Unaria(content);
                finalContent = unaria.encode();
                writeFile(".cod");
                return finalFilePath;
            case "Delta":
                Delta delta = new Delta(content);
                finalContent = delta.encode();
                writeFile(".cod");
                return finalFilePath;
        }
        return "";
    }

    public String decode(){
        switch(algorithm){
            case "Golomb":
                Golomb golomb = new Golomb(content, k, content[1]);
                finalContent = golomb.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Elias-Gamma":
                EliasGamma eliasGamma = new EliasGamma(content);
                finalContent = eliasGamma.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Fibonacci":
                Fibonacci fibonacci = new Fibonacci(content);
                finalContent = fibonacci.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Unária":
                Unaria unaria = new Unaria(content);
                finalContent = unaria.decode();
                writeFile("New.txt");
                return finalFilePath;
            case "Delta":
                Delta delta = new Delta(content);
                finalContent = delta.decode();
                writeFile("New.txt");
                return finalFilePath;
        }
        return "";
    }
    private void writeFile(String end){
        finalFilePath = filePath.substring(0,filePath.length()-4) + end;
        try {
            FileWriter fw = new FileWriter(finalFilePath);
            fw.write(new String(content, "UTF-8"));
            fw.close();
        } catch(Exception e) {System.out.println(e);}
        System.out.println("Done...");
    }
}