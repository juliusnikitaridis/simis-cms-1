package com.simisinc.platform.rest.services.carfix;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomToken {

    /**
     * Julius Nikitaridis
     * 07 Oct 2021
     * Generate a random string.
     * this should be moved into utilities or something
     */
    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }

    public static void main (String ...args) {
        System.out.println("Random token generated");
        System.out.println(new RandomToken(6).nextString());
    }

    public static final String upper = "ABCDEFGHIJKLMNPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "123456789";

    //public static final String alphanum = upper + lower + digits;
    public static final String alphanum = upper + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    public RandomToken(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomToken(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomToken(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomToken() {
        this(21);
    }

}