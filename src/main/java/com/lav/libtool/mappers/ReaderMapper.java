package com.lav.libtool.mappers;

import com.lav.libtool.dto.reader.ReaderResponseDTO;
import com.lav.libtool.entity.Reader;

public final class ReaderMapper {

    private ReaderMapper() {
    }

    public static ReaderResponseDTO toReaderResponseDTO(Reader reader) {
        return new ReaderResponseDTO(
                reader.getId(),
                reader.getFirstName(),
                reader.getLastName(),
                reader.getEmail(),
                reader.getPhone(),
                reader.getRegistrationDate()
        );
    }

}
