package com.Encoder_Decoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;

public class Golomb {

    final int divisor;
    final int suffixSize;
    private byte[] content;
    private String k;

    public Golomb(byte[] content, String k, int divisor) {
        this.content = content;
        this.k = k;

        this.divisor = divisor;
        this.suffixSize = calculateLog2(divisor);
    }

    public String encode(){
        ArrayList<Byte> resultBytes = new ArrayList<>();
        byte resultByte = 0;
        int bitPosition = 0;

        int value, rest, valToShift, aux;

        addHeaderValues(resultBytes);

        for(byte b : content) {
            if(b<0){
                aux=256+b;
            } else{
                aux=b;
            }

            value = aux / divisor;
            rest = aux - (value * divisor);

            //add value size in zeroes
            for(int i = 0; i < value; i++) {
                if (bitPosition >= 8) {
                    //byte is complete, add to array and start over
                    resultBytes.add(resultByte);
                    resultByte = 0;
                    bitPosition = 0;
                }
                bitPosition++;
            }

            if (bitPosition >= 8) {
                //byte is complete, add to array and start over
                resultBytes.add(resultByte);
                resultByte = 0;
                bitPosition = 0;
            }

            //resultByte add stopbit (1)
            valToShift = 7-bitPosition;
            resultByte = (byte) (resultByte | (1<<valToShift));
            bitPosition++;

            //add rest in binary
            BitSet bitsOfRest = BitSet.valueOf(new long[] { rest });
            for(int i = suffixSize-1; i >= 0; i--){
                if (bitPosition >= 8) {
                    //byte is complete, add to array and start over
                    resultBytes.add(resultByte);
                    resultByte = 0;
                    bitPosition = 0;
                }

                if(bitsOfRest.get(i)) {
                    valToShift = 7-bitPosition;
                    resultByte = (byte) (resultByte | (1<<valToShift));
                }

                bitPosition++;
            }
        }

        if (bitPosition > 0) {
            resultBytes.add(resultByte);
        }

        byte[] result = new byte[resultBytes.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = resultBytes.get(i);
        }

        //return result;
        String byteToString = new String(result, StandardCharsets.UTF_8);
        return byteToString;
    }

    public String decode(){
        ArrayList<Byte> decoded = new ArrayList<>();
        BitSet byteSuffix = new BitSet();
        boolean binaryArea = false;
        int countPrefix = 0;
        int suffixSizeHelp = suffixSize;
        int value, rest, result;

        for(int index = 2; index < content.length; index++) {
            BitSet bits = BitSet.valueOf(new long[] { content[index] });
            for(int i = 7; i >= 0; i--){
                if(!binaryArea) {
                    if(!bits.get(i)){
                        countPrefix++;
                    } else {
                        binaryArea = true;
                    }
                } else {
                    if(bits.get(i)) {
                        byteSuffix.set(suffixSizeHelp - 1);
                    }
                    suffixSizeHelp--;
                    if(suffixSizeHelp <= 0) {
                        value = countPrefix * divisor;
                        rest = !byteSuffix.isEmpty() ? byteSuffix.toByteArray()[0] : 0;
                        result = value + rest;
                        decoded.add((byte)result);
                        countPrefix = 0;
                        binaryArea = false;
                        byteSuffix.clear();
                        suffixSizeHelp = suffixSize;
                    }
                }
            }
        }

        byte[] decodedBytes = new byte[decoded.size()];
        for (int i = 0; i < decodedBytes.length; i++) {
            decodedBytes[i] = decoded.get(i);
        }

        String byteToString = new String(decodedBytes, StandardCharsets.UTF_8);
        return byteToString;
    }

    private int calculateLog2(int value){
        return (int) (Math.log(value) / Math.log(2) + 1e-10);
    }

    private void addHeaderValues(ArrayList<Byte> resultBytes){
        resultBytes.add((byte) 0);
        resultBytes.add((byte) divisor);
    }
}