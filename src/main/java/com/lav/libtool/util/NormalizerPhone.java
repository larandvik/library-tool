package com.lav.libtool.util;

import java.util.Objects;

public class NormalizerPhone {

    public static String normalize(String phone) {
        Objects.requireNonNull(phone, "phone cannot be null");
        return phone.replace("-", "")
                .replace(" ", "")
                .replace("+", "");
    }

}
