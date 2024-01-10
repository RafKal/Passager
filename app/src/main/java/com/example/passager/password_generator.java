package com.example.passager;


/* Quelle:
https://stackoverflow.com/a/55885045
*/

import android.util.Log;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class password_generator {

    public  String generatePassword (int length, boolean lowercase_b, boolean uppercase_b,
                                                 boolean numbers_b, boolean symbols_b) {

        final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
        final char[] numbers = "0123456789".toCharArray();
        final char[] symbols = "^$?!@#%&".toCharArray();


        List<Integer> char_type = new ArrayList<>();
        StringBuilder allowed = new StringBuilder();
        if (lowercase_b){
            allowed.append(lowercase);
            char_type.add(0);
        }
        if (uppercase_b){
            allowed.append(uppercase);
            char_type.add(1);
        }
        if (numbers_b){
            allowed.append(numbers);
            char_type.add(2);
        }
        if (symbols_b){
            allowed.append(symbols);
            char_type.add(3);
        }

        String allowed_s = allowed.toString();
        final char[] allowed_c= allowed_s.toCharArray();



        //Use cryptographically secure random number generator
        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(allowed_c[random.nextInt(allowed_c.length)]);
        }

        List<Integer> string_positions = new ArrayList<>();
        for (int i = 0; i <= length-1; i++) {
            string_positions.add(i);
        }
        Collections.shuffle(string_positions);
        string_positions.toArray();
        Log.v("positions", string_positions.toString());


        Collections.shuffle(char_type);
        char_type.toArray();
        Log.v("positions", char_type.toString());


        for (int i = 0; i < char_type.size(); i++) {
           int type = char_type.get(i);
           switch (type){
               case 0:
                   password.replace(string_positions.get(i), string_positions.get(i)+1 ,  String.valueOf(lowercase[random.nextInt(lowercase.length)]));
                   break;
               case 1:
                   password.replace(string_positions.get(i), string_positions.get(i)+1 ,  String.valueOf(uppercase[random.nextInt(uppercase.length)]));
                   break;
               case 2:
                   password.replace(string_positions.get(i), string_positions.get(i)+1 ,  String.valueOf(numbers[random.nextInt(numbers.length)]));
                   break;
               case 3:
                   password.replace(string_positions.get(i), string_positions.get(i)+1 ,  String.valueOf(symbols[random.nextInt(symbols.length)]));
                   break;
           }
        }

        return password.toString();
    }

}
