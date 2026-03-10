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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository repository;

    @Override
    public ReaderResponseDTO create(ReaderCreateRequestDTO newReader) {
        log.info("Creating new reader: {} {}", newReader.firstName(), newReader.lastName());
        var normEmail = NormalizerEmail.normalize(newReader.email());
        var normPhone = NormalizerPhone.normalize(newReader.phone());
        if(repository.existsByEmail(normEmail)) throw new ReaderAlreadyExistsException(normEmail);

        Reader reader = new Reader(
                newReader.firstName(),
                newReader.lastName(),
                normEmail,
                normPhone);
        var savedReader = repository.save(reader);

        log.info("Reader created successfully with ID: {}", savedReader.getId());
        return ReaderMapper.toReaderResponseDTO(reader);
    }

    @Override
    @Transactional(readOnly = true)
    public ReaderResponseDTO findById(long id) {
        log.debug("Fetching reader with ID: {}", id);

        return repository.findById(id)
                .map(ReaderMapper::toReaderResponseDTO)
                .orElseThrow(() -> new ReaderNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReaderResponseDTO> findAll() {
        log.debug("Fetching all readers");

        return repository.findAll().stream()
                .map(ReaderMapper::toReaderResponseDTO)
                .toList();
    }

    @Override
    public ReaderResponseDTO update(long id, ReaderUpdateRequestDTO updateReader) {
        log.info("Updating reader with ID: {}", id);

        var reader = repository.findById(id).orElseThrow(() -> new ReaderNotFoundException(id));
        var normEmail = NormalizerEmail.normalize(updateReader.email());

        reader.updateDetails(updateReader.firstName(), updateReader.lastName(), normEmail, updateReader.phone());
        repository.save(reader);

        log.info("Reader updated successfully with ID: {}", id);
        return ReaderMapper.toReaderResponseDTO(reader);
    }

    @Override
    public void delete(long id) {
        log.info("Deleting reader with ID: {}", id);

        if (!repository.existsById(id)) throw new ReaderNotFoundException(id);

        repository.deleteById(id);
        log.info("Reader deleted successfully with ID: {}", id);
    }

}
