package com.lav.libtool.dto.reader;

import java.time.LocalDate;

public record ReaderResponseDTO(

        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate registrationDate

) {}
