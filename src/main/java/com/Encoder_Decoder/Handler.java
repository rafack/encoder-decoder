package com.Encoder_Decoder;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Handler {

    private String filePath;
    private String content;
    private String algorithm;
    private String k;
    private String finalContent;
    private String finalFilePath;
    private String header;
    private String zerosAdded;
    public static final int LENGTH_OF_BITS_IN_A_BYTE = 8;
    private byte[] finalCompressed;
    private Boolean debug = false;

    public Handler(String filePath, String content, String algorithm, String k) {
        this.filePath = filePath;
        this.content = content;
        this.algorithm = algorithm;
        this.k = k;
    }

    public String encode(){
        switch(this.algorithm){
            case "Golomb":
                //Header
                header = Utils.integerToStringBinary(0, 8);
                header += Utils.integerToStringBinary(Integer.parseInt(k), 8);
                //encode
                Golomb golomb = new Golomb(content, Integer.parseInt(k));
                finalContent = golomb.encode();
                //output
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return NoiseHandler.addNoiseHandler(finalFilePath, finalCompressed, debug);
            case "Elias-Gamma":
                //Header
                header = Utils.integerToStringBinary(1, 8);
                header += Utils.integerToStringBinary(0, 8);
                //encode
                EliasGamma eliasGamma = new EliasGamma(content);
                finalContent = eliasGamma.encode();
                //output
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return NoiseHandler.addNoiseHandler(finalFilePath, finalCompressed, debug);
            case "Fibonacci":
                //Header
                header = Utils.integerToStringBinary(2, 8);
                header += Utils.integerToStringBinary(0, 8);
                //encode
                Fibonacci fibonacci = new Fibonacci(content);
                finalContent = fibonacci.encode();
                //output
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return NoiseHandler.addNoiseHandler(finalFilePath, finalCompressed, debug);
            case "Unária":
                //Header
                header = Utils.integerToStringBinary(3, 8);
                header += Utils.integerToStringBinary(0, 8);
                //encode
                Unaria unaria = new Unaria(content);
                finalContent = unaria.encode();
                //output
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return NoiseHandler.addNoiseHandler(finalFilePath, finalCompressed, debug);
            case "Delta":
                //Header
                header = Utils.integerToStringBinary(4, 8);
                header += Utils.integerToStringBinary(0, 8);
                //encode
                Delta delta = new Delta(content);
                finalContent = delta.encode();
                //output
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return NoiseHandler.addNoiseHandler(finalFilePath, finalCompressed, debug);
        }
        return "";
    }

    public String decode() throws Exception{
        findAlgorithm();
        switch(algorithm){
            case "Golomb":
                //decode
                Golomb golomb = new Golomb(content, Integer.parseInt(k));
                finalContent = golomb.decode();
                //output
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Elias-Gamma":
                //decode
                EliasGamma eliasGamma = new EliasGamma(content);
                finalContent = eliasGamma.decode();
                //output
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Fibonacci":
                //decode
                Fibonacci fibonacci = new Fibonacci(content);
                finalContent = fibonacci.decode();
                //output
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Unária":
                //decode
                Unaria unaria = new Unaria(content);
                finalContent = unaria.decode(Integer.parseInt(zerosAdded));
                //output
                writeFile("Decoded.txt", false);
                return finalFilePath;
            case "Delta":
                //decode
                Delta delta = new Delta(content);
                finalContent = delta.decode(Integer.parseInt(zerosAdded));
                //output
                writeFile("Decoded.txt", false);
                return finalFilePath;
        }
        return "";
    }
    private void writeFile(String end, Boolean hasHeader){
        finalFilePath = filePath.substring(0,filePath.length()-4) + end;
        String result = "";
       if(hasHeader) result += header;
        result += finalContent;
        try {
            if(end == "_debug.txt" && debug) {
                FileWriter fw = new FileWriter(finalFilePath);
                fw.write(printBinary(result, " | "));
                fw.close();
                System.out.println("Debug Created...");
            }
            else if(end == "Decoded.txt") {
                FileWriter fw = new FileWriter(finalFilePath);
                fw.write(result);
                fw.close();
                System.out.println("Decoded Created...");
            }
            else {
                FileOutputStream fw = new FileOutputStream(finalFilePath);
                write8bitsOrConcatZerosToComplete(fw, result);
                fw.close();
                System.out.println("Cod Created...");
            }
        } catch(Exception e) {System.out.println(e);}
    }
    public static String printBinary(String binary, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()) {
            result.add(binary.substring(index, Math.min(index + LENGTH_OF_BITS_IN_A_BYTE, binary.length())));
            index += LENGTH_OF_BITS_IN_A_BYTE;
        }

        return result.stream().collect(Collectors.joining(separator));
    }
    private void write8bitsOrConcatZerosToComplete(FileOutputStream fw, String bytes) throws IOException {
        int resto = (bytes.length() % LENGTH_OF_BITS_IN_A_BYTE);
        int divisorMenosResto = LENGTH_OF_BITS_IN_A_BYTE - resto;
        if(!bytes.substring(0,8).equals(Utils.integerToStringBinary(0, 8))){
            bytes = bytes.substring(0,8) + Utils.integerToStringBinary(divisorMenosResto, 8) + bytes.substring(16, bytes.length());
        }
        if (resto != 0) {
            for (int i = 0; i < divisorMenosResto; i++) {
                bytes = bytes.concat("0");
            }
        }
        finalCompressed = toByteArray(bytes);

        fw.write(finalCompressed);
    }
    public static byte[] toByteArray(String input) {
        List<String> codewardsSplit = new ArrayList<>();
        int index = 0;
        while (index < input.length()) {
            codewardsSplit.add(input.substring(index, Math.min(index + LENGTH_OF_BITS_IN_A_BYTE, input.length())));
            index += LENGTH_OF_BITS_IN_A_BYTE;
        }
        byte[] bitMontados = new byte[codewardsSplit.size()];
        for (int i = 0; i < codewardsSplit.size(); i++) {
            bitMontados[i] = Utils.convertBitsToByte(codewardsSplit.get(i));
        }
        return bitMontados;
    }
    private void findAlgorithm() throws Exception{
        System.out.println("findAlgorithm");
        List<String> codewardsSplit = new ArrayList<>();
        String result = "";
        byte[] temp = NoiseHandler.removeNoiseHandler(content.getBytes());
        for (int i = 0; i < temp.length; i++) {
            codewardsSplit.add(Utils.convertByteToBits(temp[i]));
            result += Utils.convertByteToBits(temp[i]);
        }

        if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(0, 8))){
            this.algorithm = "Golomb";
            this.k = Integer.toString(Integer.parseInt(codewardsSplit.get(1), 2));
        }
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(1, 8))) this.algorithm = "Elias-Gamma";
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(2, 8))) this.algorithm = "Fibonacci";
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(3, 8))){
            this.algorithm = "Unária";
            this.zerosAdded = Integer.toString(Integer.parseInt(codewardsSplit.get(1), 2));
        }
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(4, 8))){
            this.algorithm = "Delta";
            this.zerosAdded = Integer.toString(Integer.parseInt(codewardsSplit.get(1), 2));
        }
        System.out.println(algorithm);
        this.content = result.substring(LENGTH_OF_BITS_IN_A_BYTE*2, result.length());
    }
}