package com.lav.libtool.util;

import java.util.Objects;

public class NormalizerIsbn {

    public static String normalize(String isbn) {
        Objects.requireNonNull(isbn, "ISBN cannot be null");
        return isbn.replace("-", "")
                .replace(" ", "");
    }

}
