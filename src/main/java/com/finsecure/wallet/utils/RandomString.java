package com.finsecure.wallet.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RandomString {
    public static final String upper = "ABCDEFGHIJKLMNOPRSTUVWXYZ";
    public static final String lower;
    public static final String digits = "123456789";
    public static final String alphanum = "ABCDEFGHIJKLMNOPRSTUVWXYZ123456789";
    private final Random random;
    private final char[] symbols;
    private final char[] buf;

    public String nextString() {
        for(int idx = 0; idx < this.buf.length; ++idx) {
            this.buf[idx] = this.symbols[this.random.nextInt(this.symbols.length)];
        }

        return new String(this.buf);
    }

    public RandomString(int length, Random random, String symbols) {
        if (length < 1) {
            throw new IllegalArgumentException();
        } else if (symbols.length() < 2) {
            throw new IllegalArgumentException();
        } else {
            this.random = (Random)Objects.requireNonNull(random);
            this.symbols = symbols.toCharArray();
            this.buf = new char[length];
        }
    }

    public RandomString(int length, Random random) {
        this(length, random, "ABCDEFGHIJKLMNOPRSTUVWXYZ123456789");
    }

    public RandomString(int length) {
        this(length, new SecureRandom());
    }

    public RandomString() {
        this(21);
    }

    static {
        lower = "ABCDEFGHIJKLMNOPRSTUVWXYZ".toLowerCase(Locale.ROOT);
    }
}
