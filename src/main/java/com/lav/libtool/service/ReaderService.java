package com.lav.libtool.service;

import com.lav.libtool.dto.reader.ReaderCreateRequestDTO;
import com.lav.libtool.dto.reader.ReaderResponseDTO;
import com.lav.libtool.dto.reader.ReaderUpdateRequestDTO;

import java.util.List;

public interface ReaderService {

    ReaderResponseDTO create(ReaderCreateRequestDTO newReader);

    ReaderResponseDTO findById(long id);

    List<ReaderResponseDTO> findAll();

    ReaderResponseDTO update(long id, ReaderUpdateRequestDTO updateReader);

    void delete(long id);
}