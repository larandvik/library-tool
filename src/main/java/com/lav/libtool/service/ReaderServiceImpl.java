package com.lav.libtool.service;

import com.lav.libtool.dto.reader.ReaderCreateRequestDTO;
import com.lav.libtool.dto.reader.ReaderResponseDTO;
import com.lav.libtool.dto.reader.ReaderUpdateRequestDTO;
import com.lav.libtool.entity.Reader;
import com.lav.libtool.exceptions.ReaderAlreadyExistsException;
import com.lav.libtool.exceptions.ReaderNotFoundException;
import com.lav.libtool.mappers.ReaderMapper;
import com.lav.libtool.repository.ReaderRepository;
import com.lav.libtool.util.NormalizerEmail;
import com.lav.libtool.util.NormalizerPhone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository repository;

    @Override
    public ReaderResponseDTO create(ReaderCreateRequestDTO newReader) {
        var normEmail = NormalizerEmail.normalize(newReader.email());
        var normPhone = NormalizerPhone.normalize(newReader.phone());
        if(repository.existsByEmail(normEmail)) throw new ReaderAlreadyExistsException(normEmail);

        Reader reader = new Reader(
                newReader.firstName(),
                newReader.lastName(),
                normEmail,
                normPhone);
        repository.save(reader);

        return ReaderMapper.toReaderResponseDTO(reader);
    }

    @Override
    public ReaderResponseDTO findById(long id) {
        return repository.findById(id)
                .map(ReaderMapper::toReaderResponseDTO)
                .orElseThrow(() -> new ReaderNotFoundException(id));
    }

    @Override
    public List<ReaderResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(ReaderMapper::toReaderResponseDTO)
                .toList();
    }

    @Override
    public ReaderResponseDTO update(long id, ReaderUpdateRequestDTO updateReader) {
        var reader = repository.findById(id).orElseThrow(() -> new ReaderNotFoundException(id));
        var normEmail = NormalizerEmail.normalize(updateReader.email());

        reader.updateDetails(updateReader.firstName(), updateReader.lastName(), normEmail, updateReader.phone());
        repository.save(reader);
        return ReaderMapper.toReaderResponseDTO(reader);
    }

    @Override
    public void delete(long id) {
        if (!repository.existsById(id)) throw new ReaderNotFoundException(id);
        repository.deleteById(id);
    }

}
