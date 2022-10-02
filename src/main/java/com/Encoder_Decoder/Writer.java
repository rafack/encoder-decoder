package com.Encoder_Decoder;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.*;
import java.text.*;
import java.nio.charset.StandardCharsets;
import com.Encoder_Decoder.Utils;
import sun.nio.cs.UTF_8;

public class Writer {

    private String filePath;
    private String content;
    private String algorithm;
    private String k;
    private String finalContent;
    private String finalFilePath;
    private String header;
    public static final int LENGTH_OF_BITS_IN_A_BYTE = 8;

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
                header = Utils.integerToStringBinary(0, 8);
                header += Utils.integerToStringBinary(Integer.parseInt(k), 8);
                //encode
                Golomb golomb = new Golomb(content, Integer.parseInt(k));
                finalContent = golomb.encode();
                //output
                writeFile("_debug.txt", true);
                writeFile(".cod", true);
                return finalFilePath;
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
       if(hasHeader) result += header;//new String(new BigInteger(header,2).toByteArray(), StandardCharsets.US_ASCII);
        result += finalContent;// new String(new BigInteger(finalContent,2).toByteArray(), StandardCharsets.US_ASCII);
        try {
            if(end == "_debug.txt") {
                FileWriter fw = new FileWriter(finalFilePath);
                fw.write(printBinary(result, " | "));
                fw.close();
            }
            else if(end == "Decoded.txt") {
                FileWriter fw = new FileWriter(finalFilePath);
                fw.write(result);
                fw.close();
            }
            else {
                FileOutputStream fw = new FileOutputStream(finalFilePath);
                write8bitsOrConcatZerosToComplete(fw, result);
                fw.close();
            }
        } catch(Exception e) {System.out.println(e);}
        System.out.println("Done...");
    }
    private static String printBinary(String binary, String separator) {

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
        if (resto != 0) {
            for (int i = 0; i < divisorMenosResto; i++) {
                bytes = bytes.concat("0");
            }
        }
        fw.write(toByteArray(bytes));
    }
    private static byte convertBitsToByte(String bits) {
        return (byte) Integer.parseInt(bits, 2);
    }
    private static String convertByteToBits(byte bytes) {
        int i = Byte.toUnsignedInt(bytes);
        return Utils.integerToStringBinary(i,LENGTH_OF_BITS_IN_A_BYTE);
    }
    private static
    byte[] toByteArray(String input) {
        List<String> codewardsSplit = new ArrayList<>();
        int index = 0;
        while (index < input.length()) {
            codewardsSplit.add(input.substring(index, Math.min(index + LENGTH_OF_BITS_IN_A_BYTE, input.length())));
            index += LENGTH_OF_BITS_IN_A_BYTE;
        }
        byte[] bitMontados = new byte[codewardsSplit.size()];
        for (int i = 0; i < codewardsSplit.size(); i++) {
            bitMontados[i] = convertBitsToByte(codewardsSplit.get(i));
        }
        return bitMontados;
    }
    private void findAlgorithm(){
        System.out.println("findAlgorithm");
        List<String> codewardsSplit = new ArrayList<>();
        String result = "";
        byte[] temp = content.getBytes();
        for (int i = 0; i < temp.length; i++) {
            codewardsSplit.add(convertByteToBits(temp[i]));
            result += convertByteToBits(temp[i]);
        }

        if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(0, 8))) this.algorithm = "Golomb";
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(1, 8))) this.algorithm = "Elias-Gamma";
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(2, 8))) this.algorithm = "Fibonacci";
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(3, 8))) this.algorithm = "Unária";
        else if(codewardsSplit.get(0).equals(Utils.integerToStringBinary(4, 8))) this.algorithm = "Delta";

        this.k = Integer.toString(Integer.parseInt(codewardsSplit.get(1), 2));
        System.out.println(algorithm);
        System.out.println(k);
        this.content = result.substring(LENGTH_OF_BITS_IN_A_BYTE*2, result.length());
        System.out.println(content);
    }
}