package com.Encoder_Decoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Delta {
    private byte[] content;

    public Delta(byte[] content) {
        this.content = content;
    }

    public String encode(){
        ArrayList<Byte> resultBytes = new ArrayList<>();
        byte delta = 0;
        int largestDelta = 0;
        addHeaderValues(resultBytes);

        ArrayList<Byte> deltas = new ArrayList<>();

        //descobre o delta de cada byte
        for(int i = 0; i < content.length-1; i++){
            delta = (byte) (content[i+1] - content[i]);
            deltas.add(delta);

            if (Math.abs(delta) > Math.abs(largestDelta)) largestDelta = delta;

            if (i == 0) resultBytes.add(content[i]);
        }

        int byteLength = 0;

        //tentativa de calcular o codeword de cada salto
        for(int i = 0; i < deltas.size()-1; i++){
            if (deltas.get(i) != deltas.get(i+1)){
                byteLength = Integer.toBinaryString(deltas.get(i)).length();
                delta = (byte) (deltas.get(i) | (1<<byteLength));
            }
            resultBytes.add((byte) (delta));
        }

        byte[] result = new byte[resultBytes.size()];

        for(int i = 0; i < result.length; i++) {
            result[i] = resultBytes.get(i);
        }

        String byteToString = new String(result, StandardCharsets.UTF_8);
        return byteToString;
    }

    public String decode(){
        //Implementar
        return "";
    }

    private void addHeaderValues(ArrayList<Byte> resultBytes){
        resultBytes.add((byte) 4);
        resultBytes.add((byte) 0);
    }
}