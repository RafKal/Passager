package com.example.passager;

import java.security.SecureRandom;
import java.util.Random;

public class password_generator {

    public  String generatePassword (int length, boolean lowercases_b, boolean uppercase_b,
                                                 boolean numbers_b, boolean symbols_b) {
        //minimum length of 6
        if (length < 1) {
            length = 6;
        }

        final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
        final char[] numbers = "0123456789".toCharArray();
        final char[] symbols = "^$?!@#%&".toCharArray();
        final char[] allAllowed = "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray();

        StringBuilder allowed = new StringBuilder();
        if (lowercases_b){
            allowed.append(lowercase);
        }
        if (uppercase_b){
            allowed.append(uppercase);
        }
        if (numbers_b){
            allowed.append(numbers);
        }
        if (symbols_b){
            allowed.append(symbols);
        }

        String allowed_s = allowed.toString();
        final char[] allowed_c= allowed_s.toCharArray();


        //Use cryptographically secure random number generator
        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(allowed_c[random.nextInt(allowed_c.length)]);
        }

        //Ensure password policy is met by inserting required random chars in random positions
       /* password.insert(random.nextInt(password.length()), lowercase[random.nextInt(lowercase.length)]);
        password.insert(random.nextInt(password.length()), uppercase[random.nextInt(uppercase.length)]);
        password.insert(random.nextInt(password.length()), numbers[random.nextInt(numbers.length)]);
        password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);
*/
        return password.toString();
    }

}
