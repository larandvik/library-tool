package com.lav.libtool.util;

import java.util.Objects;

public class NormalizerEmail {

    public static String normalize(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        return email.toLowerCase()
                .strip();
    }

}
