package com.lav.libtool.dto.reader;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record ReaderUpdateRequestDTO(
        String firstName,
        String lastName,
        @Email
        @Nullable
        String email,
        String phone
) {}