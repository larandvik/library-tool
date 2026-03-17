package com.lav.libtool.util;

public class NormalizerEmail {

    public static String normalize(String email) {
        return email.toLowerCase()
                .strip();
    }

}
