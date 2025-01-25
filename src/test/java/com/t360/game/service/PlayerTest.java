package com.t360.game.service;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PlayerTest {

    public static void main(String[] args) {
        String[] ar = new String[]{
            "message 0",
            "message 0 1",
            "message 0 1 1",
            "message 0 1 1 2",
            "message a 0 1 5",
            "message a 0 1 5 ABC"
        };
        Arrays.stream(ar).forEach((m) -> System.out.println("%s => %s".formatted(m,reply(m))) );
        String st = ar[0];
        System.out.println(st);


    }

//    @org.junit.jupiter.api.Test
    static String reply(String message) {
        Pattern pattern2No = Pattern.compile("[\\w\\s]+(\\d+)\\s+(\\d+)$");
        Pattern pattern1No = Pattern.compile("[\\w\\s]+(\\d+)$");
        Matcher m = pattern2No.matcher(message);
        if(m.find()){
            return message+" "+(Integer.parseInt(m.group(1))+1);
        }else{
            m = pattern1No.matcher(message);
            if(m.find()) {
                return message+" 1";
            }else{
                System.err.println("Not found!");
                throw new IllegalArgumentException("The input message is not match with declared pattern!");
            }
        }
    }
}